package jp.rough_diamond.commons.util.mule.transformer;

import jp.rough_diamond.commons.util.PropertyUtils;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

abstract public class PropertyBaseTransformer extends AbstractTransformer {
	
	@Override
	protected Object doTransform(Object src, String encoding) throws TransformerException {
		Object dest = newTransformObject();
		PropertyUtils.copyProperties(src, dest);
		afterProcess(src, dest, encoding);
		return dest;
	}

	protected void afterProcess(Object src, Object dest, String encoding){ }
	
	abstract protected Object newTransformObject();
}
