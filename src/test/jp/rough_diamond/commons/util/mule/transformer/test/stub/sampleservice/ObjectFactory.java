
package jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DoItResponse_QNAME = new QName("http://sample.rough_diamond.jp/", "doItResponse");
    private final static QName _DoIt_QNAME = new QName("http://sample.rough_diamond.jp/", "doIt");
    private final static QName _ChildBeanZzz_QNAME = new QName("http://test.transformer.mule.util.commons.rough_diamond.jp", "zzz");
    private final static QName _ChildBeanYyy_QNAME = new QName("http://test.transformer.mule.util.commons.rough_diamond.jp", "yyy");
    private final static QName _ParentBeanXxx_QNAME = new QName("http://test.transformer.mule.util.commons.rough_diamond.jp", "xxx");
    private final static QName _ParentBeanChild_QNAME = new QName("http://test.transformer.mule.util.commons.rough_diamond.jp", "child");
    private final static QName _ParentBeanArray_QNAME = new QName("http://test.transformer.mule.util.commons.rough_diamond.jp", "array");
    private final static QName _ParentBeanInt1_QNAME = new QName("http://test.transformer.mule.util.commons.rough_diamond.jp", "int1");
    private final static QName _ParentBeanCal_QNAME = new QName("http://test.transformer.mule.util.commons.rough_diamond.jp", "cal");
    private final static QName _ParentBeanList_QNAME = new QName("http://test.transformer.mule.util.commons.rough_diamond.jp", "list");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DoItResponse }
     * 
     */
    public DoItResponse createDoItResponse() {
        return new DoItResponse();
    }

    /**
     * Create an instance of {@link ChildBean }
     * 
     */
    public ChildBean createChildBean() {
        return new ChildBean();
    }

    /**
     * Create an instance of {@link DoIt }
     * 
     */
    public DoIt createDoIt() {
        return new DoIt();
    }

    /**
     * Create an instance of {@link ParentBean }
     * 
     */
    public ParentBean createParentBean() {
        return new ParentBean();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoItResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.rough_diamond.jp/", name = "doItResponse")
    public JAXBElement<DoItResponse> createDoItResponse(DoItResponse value) {
        return new JAXBElement<DoItResponse>(_DoItResponse_QNAME, DoItResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoIt }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.rough_diamond.jp/", name = "doIt")
    public JAXBElement<DoIt> createDoIt(DoIt value) {
        return new JAXBElement<DoIt>(_DoIt_QNAME, DoIt.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.transformer.mule.util.commons.rough_diamond.jp", name = "zzz", scope = ChildBean.class)
    public JAXBElement<String> createChildBeanZzz(String value) {
        return new JAXBElement<String>(_ChildBeanZzz_QNAME, String.class, ChildBean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.transformer.mule.util.commons.rough_diamond.jp", name = "yyy", scope = ChildBean.class)
    public JAXBElement<String> createChildBeanYyy(String value) {
        return new JAXBElement<String>(_ChildBeanYyy_QNAME, String.class, ChildBean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.transformer.mule.util.commons.rough_diamond.jp", name = "xxx", scope = ParentBean.class)
    public JAXBElement<String> createParentBeanXxx(String value) {
        return new JAXBElement<String>(_ParentBeanXxx_QNAME, String.class, ParentBean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChildBean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.transformer.mule.util.commons.rough_diamond.jp", name = "child", scope = ParentBean.class)
    public JAXBElement<ChildBean> createParentBeanChild(ChildBean value) {
        return new JAXBElement<ChildBean>(_ParentBeanChild_QNAME, ChildBean.class, ParentBean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.transformer.mule.util.commons.rough_diamond.jp", name = "array", scope = ParentBean.class)
    public JAXBElement<ArrayOfString> createParentBeanArray(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_ParentBeanArray_QNAME, ArrayOfString.class, ParentBean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.transformer.mule.util.commons.rough_diamond.jp", name = "int1", scope = ParentBean.class)
    public JAXBElement<Integer> createParentBeanInt1(Integer value) {
        return new JAXBElement<Integer>(_ParentBeanInt1_QNAME, Integer.class, ParentBean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.transformer.mule.util.commons.rough_diamond.jp", name = "cal", scope = ParentBean.class)
    public JAXBElement<XMLGregorianCalendar> createParentBeanCal(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ParentBeanCal_QNAME, XMLGregorianCalendar.class, ParentBean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.transformer.mule.util.commons.rough_diamond.jp", name = "list", scope = ParentBean.class)
    public JAXBElement<ArrayOfString> createParentBeanList(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_ParentBeanList_QNAME, ArrayOfString.class, ParentBean.class, value);
    }

}
