package jp.rough_diamond.tools.servicegen;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class NameSpaceContextImpl implements NamespaceContext {
	//MuleŠÖ˜ANameSpaceURI
	public final static String MULE_NAME_SPACE = "http://www.mulesource.org/schema/mule/core/";
	public final static String CXF_NAME_SPACE = "http://www.mulesource.org/schema/mule/cxf/";
	public final static String VM_NAME_SPACE = "http://www.mulesource.org/schema/mule/vm/";

	//WSDLŠÖ˜ANameSpaceURI
	public final static String WSDL_NAME_SPACE = "http://schemas.xmlsoap.org/wsdl/";
	
	private String muleVersion;
	public NameSpaceContextImpl(String muleVersion) {
		this.muleVersion = muleVersion;
	}
	
	@Override
	public String getNamespaceURI(String prefix) {
		if("mule".equals(prefix)) {
			return MULE_NAME_SPACE + muleVersion;
		} else if("wsdl".equals(prefix)) {
			return WSDL_NAME_SPACE;
		} else {
			return XMLConstants.NULL_NS_URI;
		}
	}

	@Override
	public String getPrefix(String namespaceURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterator getPrefixes(String namespaceURI) {
		throw new UnsupportedOperationException();
	}
}
