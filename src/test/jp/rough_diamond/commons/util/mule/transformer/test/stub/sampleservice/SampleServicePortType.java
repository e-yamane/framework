
package jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.0.7
 * Sun Sep 21 18:05:24 JST 2008
 * Generated source version: 2.0.7
 * 
 */

@WebService(targetNamespace = "http://sample.rough_diamond.jp/", name = "SampleServicePortType")

public interface SampleServicePortType {

    @RequestWrapper(localName = "doIt", targetNamespace = "http://sample.rough_diamond.jp/", className = "jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice.DoIt")
    @ResponseWrapper(localName = "doItResponse", targetNamespace = "http://sample.rough_diamond.jp/", className = "jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice.DoItResponse")
    @WebMethod
    public void doIt(
        @WebParam(name = "arg0", targetNamespace = "http://sample.rough_diamond.jp/")
        jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice.ParentBean arg0
    );
}