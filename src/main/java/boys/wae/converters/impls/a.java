package boys.wae.converters.impls;

import java.util.Map;

import it.uniroma2.art.coda.exception.ConverterException;
import it.uniroma2.art.coda.interfaces.CODAContext;
import it.uniroma2.art.coda.interfaces.Converter;
import it.uniroma2.art.coda.interfaces.annotations.converters.FeaturePathArgument;
import it.uniroma2.art.coda.interfaces.annotations.converters.Parameter;
import it.uniroma2.art.coda.interfaces.annotations.converters.RequirementLevels;
import it.uniroma2.art.owlart.model.ARTLiteral;
import it.uniroma2.art.owlart.model.ARTNodeFactory;
import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.model.impl.ARTNodeFactoryImpl;
import boys.wae.converters.contracts.a;

/**
 * An implementation of the contract {@code a}
 */
public class a implements a {

	public static final String CONVERTER_URI = "a";

	private ARTNodeFactory fact = new ARTNodeFactoryImpl();
	
	@FeaturePathArgument(requirementLevel = RequirementLevels.REQUIRED)
	public ARTURIResource produceURI(CODAContext ctx, String value) throws ConverterException {
		return fact.createURIResource(ctx.getDefaultNamespace() + value);
	}

	@FeaturePathArgument(requirementLevel = RequirementLevels.REQUIRED)
	public ARTLiteral produceLiteral(CODAContext ctx, String datatype, String lang, String value) throws ConverterException {
		if (datatype != null) {
			return fact.createLiteral(value, fact.createURIResource(datatype));
		} else if (lang != null) {
			return fact.createLiteral(value, lang);
		} else {
			return fact.createLiteral(value);
		}
	}
}
