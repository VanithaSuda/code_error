package boys.wae.converters.contracts;

import it.uniroma2.art.coda.exception.ConverterException;
import it.uniroma2.art.coda.interfaces.CODAContext;
import it.uniroma2.art.coda.interfaces.Converter;
import it.uniroma2.art.coda.interfaces.annotations.converters.FeaturePathArgument;
import it.uniroma2.art.coda.interfaces.annotations.converters.Parameter;
import it.uniroma2.art.coda.interfaces.annotations.converters.RequirementLevels;
import it.uniroma2.art.owlart.model.ARTLiteral;
import it.uniroma2.art.owlart.model.ARTURIResource;

/**
 * TODO: provide a description for the contract a
 */
public interface a extends Converter {

	String CONTRACT_URI = "a";

	/**
	 * TODO: define an appropriate signature
	 */
	@FeaturePathArgument(requirementLevel = RequirementLevels.REQUIRED)
	ARTURIResource produceURI(CODAContext ctx, String value) throws ConverterException;


	/**
	 * TODO: define an appropriate signature
	 */
	@FeaturePathArgument(requirementLevel = RequirementLevels.REQUIRED)
	ARTLiteral produceLiteral(CODAContext ctx, String datatype, String lang, String value) throws ConverterException;

}
