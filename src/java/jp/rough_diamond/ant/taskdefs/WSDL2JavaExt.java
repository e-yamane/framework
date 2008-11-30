package jp.rough_diamond.ant.taskdefs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import jp.rough_diamond.tools.servicegen.NameSpaceContextImpl;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.tools.wsdlto.WSDLToJava;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class WSDL2JavaExt extends Task {
	@Override
	public void execute() throws BuildException {
		try {
			StringBuilder sb = new StringBuilder(wsdlDir.getCanonicalPath());
			sb.append("/");
			sb.append("*.wsdl");
			DirectoryScanner ds = FileScanUtil.getDirectoryScanner(sb.toString());
			String[] names = ds.getIncludedFiles();
			File rootDir = ds.getBasedir();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder muleDB = dbf.newDocumentBuilder();
			this.doc = muleDB.parse(muleConfigFile);
			isMuleConfigEdit = false;

	        for(String name : names) {
				System.out.println(name);
				File wsdl = new File(rootDir, name);
				Document wsdlDoc = muleDB.parse(wsdl);
				generate(wsdl, wsdlDoc);
				addClientConnectorConfig(wsdl, wsdlDoc);
			}
	        if(isMuleConfigEdit) {
	        	output();
	        }
	        deletebackupFile();
		} catch(Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	void deletebackupFile() {
		Delete task = new Delete();
		task.setProject(getProject());
		task.setDir(muleConfigFile.getParentFile());
		task.setIncludes(muleConfigFile.getName() + ".*");
		task.execute();
	}

	private void output() throws Exception {
		System.out.println("出力します。");
		TransformerFactory tfactory = TransformerFactory.newInstance(); 
		Transformer transformer = tfactory.newTransformer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		File bkup = new File(muleConfigFile.getParentFile(), muleConfigFile.getName() + "." + sdf.format(new Date()));
		System.out.println(bkup.getCanonicalPath());
		FileUtils.copyFile(muleConfigFile, bkup);
        transformer.transform(new DOMSource(doc), new StreamResult(muleConfigFile));
//      	StringWriter sw = new StringWriter();
//      	transformer.transform(new DOMSource(doc), new StreamResult(sw));
//      	System.out.println(sw.toString());
	}

	private Document doc;
	private boolean isMuleConfigEdit = false;
	
	void addClientConnectorConfig(File wsdl, Document wsdlDoc) throws Exception {
		String modelName = getConnectorName(wsdlDoc) + "Model";
		System.out.println(modelName);
		Element model = getModelElement(modelName);
		if(hasServiceElement(model, wsdlDoc)) {
			System.out.println("処理をスキップします。");
			return;
		}
		addService(model, wsdl, wsdlDoc);
	}

	void addService(Element model, File wsdl, Document wsdlDoc) throws Exception {
		isMuleConfigEdit = true;
		String connectorName = getConnectorName(wsdlDoc);
		String packageName = getPackage(wsdl);
		System.out.println(connectorName + "を追加します。");
		
		String muleNameSpace = NameSpaceContextImpl.MULE_NAME_SPACE + getVersion();
		Element serviceEL = doc.createElementNS(muleNameSpace, "service");
		serviceEL.setAttribute("name", connectorName);
		Text t = doc.createTextNode("\n    ");
		model.appendChild(t);
		model.appendChild(serviceEL);

		Element inboundEL = doc.createElementNS(muleNameSpace, "inbound");
		t = doc.createTextNode("\n      ");
		serviceEL.appendChild(t);
		serviceEL.appendChild(inboundEL);

		Element inboundEndpointEL = doc.createElementNS(NameSpaceContextImpl.VM_NAME_SPACE + getVersion(), "vm:inbound-endpoint");
		t = doc.createTextNode("\n        ");
		inboundEL.appendChild(t);
		inboundEL.appendChild(inboundEndpointEL);
		t = doc.createTextNode("\n      ");
		inboundEL.appendChild(t);
		inboundEndpointEL.setAttribute("path", connectorName + "In");

		t = doc.createTextNode("\n          ");
		inboundEndpointEL.appendChild(t);
		Element customTransformerEL = doc.createElementNS(muleNameSpace, "custom-transformer");
		inboundEndpointEL.appendChild(customTransformerEL);
		t = doc.createTextNode("\n        ");
		inboundEndpointEL.appendChild(t);
		customTransformerEL.setAttribute("name", connectorName + "InTransformer");
		customTransformerEL.setAttribute("class", packageName + "." + TRANSFORMER_CLASS_NAME);
		
		Element bridgeComponentEL = doc.createElementNS(muleNameSpace, "bridge-component");
		t = doc.createTextNode("\n      ");
		serviceEL.appendChild(t);
		serviceEL.appendChild(bridgeComponentEL);

		Element outboundEL = doc.createElementNS(muleNameSpace, "outbound");
		t = doc.createTextNode("\n      ");
		serviceEL.appendChild(t);
		serviceEL.appendChild(outboundEL);

		Element outboundRouterEL = doc.createElementNS(muleNameSpace, "chaining-router");
		t = doc.createTextNode("\n        ");
		outboundEL.appendChild(t);
		outboundEL.appendChild(outboundRouterEL);
		t = doc.createTextNode("\n      ");
		outboundEL.appendChild(t);
		
		Element outboundEndpointEL = doc.createElementNS(NameSpaceContextImpl.CXF_NAME_SPACE + getVersion(), "cxf:outbound-endpoint");
		t = doc.createTextNode("\n          ");
		outboundRouterEL.appendChild(t);
		outboundRouterEL.appendChild(outboundEndpointEL);
		t = doc.createTextNode("\n        ");
		outboundRouterEL.appendChild(t);
		String serviceName = getServiceName(wsdlDoc);
		outboundEndpointEL.setAttribute("address", "http://${gateway.host}:${gateway.port}/services/" + serviceName);
		outboundEndpointEL.setAttribute("clientClass", packageName + "." + serviceName);
		outboundEndpointEL.setAttribute("wsdlLocation", "file:///${wsdl.dir}/" + wsdl.getName());
		outboundEndpointEL.setAttribute("mtomEnabled", "true");
		outboundEndpointEL.setAttribute("operation", getOperation(wsdlDoc));
		outboundEndpointEL.setAttribute("wsdlPort", getWsdlPort(wsdlDoc));
		
		
		t = doc.createTextNode("\n    ");
		serviceEL.appendChild(t);

		t = doc.createTextNode("\n  ");
		model.appendChild(t);
	}

	boolean hasServiceElement(Element model, Document wsdlDoc) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(new NameSpaceContextImpl(getVersion()));
		String serviceName = getConnectorName(wsdlDoc);
		String xpathStr = "mule:service";
		System.out.println(xpathStr);
		XPathExpression exp = xpath.compile(xpathStr);
		NodeList nodeList = (NodeList)exp.evaluate(model, XPathConstants.NODESET);
		System.out.println(nodeList.getLength());
		for(int i = 0 ; i < nodeList.getLength() ; i++) {
			Element el = (Element)nodeList.item(i);
			if(serviceName.equals(el.getAttribute("name"))) {
				System.out.println("見つかりました。");
				return true;
			}
		}
		System.out.println(serviceName + "が見つかりませんでした。");
		return false;
	}

	private Element getModelElement(String modelName) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(new NameSpaceContextImpl(getVersion()));
		String xpathStr = "/mule:mule/mule:model";
		System.out.println(xpathStr);
		XPathExpression exp = xpath.compile(xpathStr);
		NodeList nodeList = (NodeList)exp.evaluate(doc, XPathConstants.NODESET);
		System.out.println(nodeList.getLength());
		for(int i = 0 ; i < nodeList.getLength() ; i++) {
			Element el = (Element)nodeList.item(i);
			if(modelName.equals(el.getAttribute("name"))) {
				System.out.println(modelName + "が見つかりました。");
				return el;
			}
		}
		isMuleConfigEdit = true;
		System.out.println(modelName + "が見つかりませんでした。作成します。");
		Element ret = doc.createElementNS(NameSpaceContextImpl.MULE_NAME_SPACE + getVersion(), "model");
		Text t = doc.createTextNode("\n  ");
		doc.getDocumentElement().appendChild(t);
		doc.getDocumentElement().appendChild(ret);
		t = doc.createTextNode("\n");
		doc.getDocumentElement().appendChild(t);
		ret.setAttribute("name", modelName);
		
		return ret;
	}

	private String getConnectorName(Document wsdlDoc) throws Exception {
		return getServiceName(wsdlDoc) + "Connector";
	}
	
	private String getWsdlPort(Document wsdlDoc) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(new NameSpaceContextImpl(getVersion()));
		String xpathStr = "//wsdl:port";
		System.out.println(xpathStr);
		XPathExpression exp = xpath.compile(xpathStr);
		Element el = (Element)exp.evaluate(wsdlDoc, XPathConstants.NODE);
		String ret = el.getAttribute("name");
		System.out.println(ret);
		return ret;
	}

	private String getOperation(Document wsdlDoc) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(new NameSpaceContextImpl(getVersion()));
		String xpathStr = "//wsdl:operation";
		System.out.println(xpathStr);
		XPathExpression exp = xpath.compile(xpathStr);
		Element el = (Element)exp.evaluate(wsdlDoc, XPathConstants.NODE);
		String ret = el.getAttribute("name");
		System.out.println(ret);
		return ret;
	}

	private String getPortType(Document wsdlDoc) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(new NameSpaceContextImpl(getVersion()));
		String xpathStr = "//wsdl:portType";
		System.out.println(xpathStr);
		XPathExpression exp = xpath.compile(xpathStr);
		Element el = (Element)exp.evaluate(wsdlDoc, XPathConstants.NODE);
		String ret = el.getAttribute("name");
		System.out.println(ret);
		return ret;
	}

	private String getServiceName(Document wsdlDoc) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(new NameSpaceContextImpl(getVersion()));
		String xpathStr = "/wsdl:definitions/wsdl:service";
		System.out.println(xpathStr);
		XPathExpression exp = xpath.compile(xpathStr);
		Element el = (Element)exp.evaluate(wsdlDoc, XPathConstants.NODE);
		String ret = el.getAttribute("name");
		System.out.println(ret);
		return ret;
	}
	
	private void generate(File wsdl, Document wsdlDoc) throws Exception {
		Java java = new Java();
		java.setProject(getProject());
		java.setClassname(WSDLToJava.class.getName());
		java.setFork(true);
		java.createArg().setValue("-client");
		java.createArg().setValue("-p");
		java.createArg().setValue(getPackage(wsdl));
		java.createArg().setValue("-d");
		java.createArg().setFile(getSrcdir());
		java.createArg().setValue("-verbose");
		java.createArg().setFile(wsdl);
		java.setClasspath((Path)getProject().getReference(getClassPathRef()));
		java.execute();
		
		String packageName = getPackage(wsdl);
		String operation = getOperation(wsdlDoc);
		String portTypeClassName = getPortType(wsdlDoc);
		String transformClassBody = String.format(TRANSFORMER_CLASS_TEMPLATE, packageName, TRANSFORMER_CLASS_NAME, 
				TRANSFORMER_CLASS_NAME, TRANSFORMER_CLASS_NAME, operation, portTypeClassName);
		System.out.println(transformClassBody);
		File packageDir = new File(getSrcdir(), packageName.replace('.', '/'));
		boolean ret = packageDir.mkdirs();
		//FindBug対応
		if(!ret) {
			ret = !ret;
		}
		FileUtils.writeStringToFile(new File(packageDir, TRANSFORMER_CLASS_NAME + ".java"), transformClassBody, "UTF-8");
	}
	
	String getPackage(File wsdl) {
		StringBuilder sb = new StringBuilder();
		sb.append(getRootPackage());
		if(!getRootPackage().endsWith(".")) {
			sb.append(".");
		}
		sb.append(getSubPackageName(wsdl.getName()));
		return sb.toString();
	}
	
	static String getSubPackageName(String wsdlName) {
		return wsdlName.replaceAll("\\.wsdl$", "").toLowerCase();
	}
	
	private File wsdlDir;
	private File srcdir;
	private String rootPackage;
	private String classPathRef;
	private File muleConfigFile;
	private String muleVersion;
	
	public String getVersion() {
		return muleVersion;
	}

	public void setVersion(String version) {
		this.muleVersion = version;
	}

	public File getMuleConfigFile() {
		return muleConfigFile;
	}

	public void setMuleConfigFile(File muleConfigFile) {
		this.muleConfigFile = muleConfigFile;
	}

	public String getClassPathRef() {
		return classPathRef;
	}

	public void setClassPathRef(String classPathRef) {
		this.classPathRef = classPathRef;
	}

	public String getRootPackage() {
		return rootPackage;
	}
	public void setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
	}

	public File getWsdlDir() {
		return wsdlDir;
	}

	public void setWsdlDir(File wsdlDir) {
		this.wsdlDir = wsdlDir;
	}

	public File getSrcdir() {
		return srcdir;
	}

	public void setSrcdir(File srcdir) {
		this.srcdir = srcdir;
	}
	
	static final String TRANSFORMER_CLASS_NAME = "ObjectToJAXBElement";
	static final String TRANSFORMER_CLASS_TEMPLATE = 
		"package %s;\n" +
		"\n" +
		"import jp.rough_diamond.commons.util.mule.transformer.AbstractObjectToJAXBElement;\n" +
		"\n" +
		"public class %s extends AbstractObjectToJAXBElement {\n" +
		"    public final static %s INSTANCE = new %s();\n" +
		"\n" +
		"    @Override\n" +
		"    protected String getOperation() {\n" +
		"        return \"%s\";\n" +
		"    }\n" +
		"\n" +
		"    @Override\n" +
		"    protected Class<?> getPortType() {\n" +
		"        return %s.class;\n" +
		"    }\n" +
		"}\n";
}
