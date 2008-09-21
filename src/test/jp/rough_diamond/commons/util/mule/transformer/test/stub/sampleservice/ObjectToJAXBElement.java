package jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice;

import jp.rough_diamond.commons.util.mule.transformer.AbstractObjectToJAXBElement;

public class ObjectToJAXBElement extends AbstractObjectToJAXBElement {
    public final static ObjectToJAXBElement INSTANCE = new ObjectToJAXBElement();

    @Override
    protected String getOperation() {
        return "doIt";
    }

    @Override
    protected Class<?> getPortType() {
        return SampleServicePortType.class;
    }
}
