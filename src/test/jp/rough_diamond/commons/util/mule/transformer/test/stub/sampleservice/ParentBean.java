
package jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParentBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParentBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="child" type="{http://sample.rough_diamond.jp}ChildBean" minOccurs="0"/>
 *         &lt;element name="xxx" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParentBean", propOrder = {
    "child",
    "xxx"
})
public class ParentBean {

    @XmlElementRef(name = "child", namespace = "http://sample.rough_diamond.jp", type = JAXBElement.class)
    protected JAXBElement<ChildBean> child;
    @XmlElementRef(name = "xxx", namespace = "http://sample.rough_diamond.jp", type = JAXBElement.class)
    protected JAXBElement<String> xxx;

    /**
     * Gets the value of the child property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ChildBean }{@code >}
     *     
     */
    public JAXBElement<ChildBean> getChild() {
        return child;
    }

    /**
     * Sets the value of the child property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ChildBean }{@code >}
     *     
     */
    public void setChild(JAXBElement<ChildBean> value) {
        this.child = ((JAXBElement<ChildBean> ) value);
    }

    /**
     * Gets the value of the xxx property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getXxx() {
        return xxx;
    }

    /**
     * Sets the value of the xxx property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setXxx(JAXBElement<String> value) {
        this.xxx = ((JAXBElement<String> ) value);
    }

}
