package boys.wae.converters.impls;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.junit.Rule;
import org.junit.Test;

import it.uniroma2.art.coda.exception.ConverterException;
import it.uniroma2.art.coda.interfaces.CODAContext;
import it.uniroma2.art.owlart.model.ARTLiteral;
import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.vocabulary.XmlSchema;

/**
 * TODO update test for converter {@code a}
 */
public class aTest {

	@Rule
	public EasyMockRule rule = new EasyMockRule(this);

	@Mock
	private CODAContext mockedCODAContext;

	@Test
	public void testProduceURI() throws ConverterException {
		
		expect(mockedCODAContext.getDefaultNamespace()).andReturn("http://example.org/");
		replay(mockedCODAContext);
		
		a conv = new a();

		ARTURIResource uriResource = conv.produceURI(mockedCODAContext, "hello");

		verify(mockedCODAContext);
		
		assertThat(uriResource.getNominalValue(), is(equalTo("http://example.org/hello")));
	}

	@Test
	public void testProduceLiteral_string() throws ConverterException {
		
		replay(mockedCODAContext);
		
		a conv = new a();

		ARTLiteral literalValue = conv.produceLiteral(mockedCODAContext, XmlSchema.STRING, null, "hello");
		
		verify(mockedCODAContext);

		assertThat(literalValue, both(hasProperty("datatype", equalTo(XmlSchema.Res.STRING)))
				.and(hasProperty("label", equalTo("hello"))));
	}
	
	@Test
	public void testProduceLiteral_en() throws ConverterException {
		
		replay(mockedCODAContext);
		
		a conv = new a();

		ARTLiteral literalValue = conv.produceLiteral(mockedCODAContext, null, "en", "hello");
		
		verify(mockedCODAContext);

		assertThat(literalValue, both(hasProperty("language", equalTo("en")))
				.and(hasProperty("label", equalTo("hello"))));
	}
	
}
