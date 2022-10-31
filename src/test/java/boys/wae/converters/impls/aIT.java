package org.example.converters.impls;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.XMLInputSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.uniroma2.art.coda.core.CODACore;
import it.uniroma2.art.coda.standalone.CODAStandaloneFactory;
import it.uniroma2.art.coda.structures.ARTTriple;
import it.uniroma2.art.coda.structures.SuggOntologyCoda;
import it.uniroma2.art.owlart.exceptions.ModelUpdateException;
import it.uniroma2.art.owlart.model.ARTNode;
import it.uniroma2.art.owlart.model.ARTNodeFactory;
import it.uniroma2.art.owlart.model.impl.ARTNodeFactoryImpl;
import it.uniroma2.art.owlart.models.OWLArtModelFactory;
import it.uniroma2.art.owlart.models.RDFModel;
import it.uniroma2.art.owlart.models.impl.RDFModelImpl;
import it.uniroma2.art.owlart.rdf4jimpl.factory.ARTModelFactoryRDF4JImpl;
import it.uniroma2.art.owlart.rdf4jimpl.models.conf.RDF4JModelConfiguration;
import it.uniroma2.art.owlart.utilities.ModelUtilities;
import it.uniroma2.art.owlart.vocabulary.RDF;
import it.uniroma2.art.owlart.vocabulary.XmlSchema;

public class aIT {

	public static final String BUNDLES_DIRECTORY_PATH = "target/it-bundles";
	public static final String OSGI_CACHE_DIRECTORY_PATH = "target/it-cache";
	
	public static final String BASE_URI = "http://example.org/";
	public static final String DEFAULT_NS = ModelUtilities.createDefaultNamespaceFromBaseURI(BASE_URI);

	private static CODACore codaCore;
	private static RDFModel rdfModel;
	
	// Initializes CODA
	@BeforeClass
	public static void setup() throws Exception {
		File bundlesDirectory = new File(BUNDLES_DIRECTORY_PATH);
		File cacheDirectory = new File(OSGI_CACHE_DIRECTORY_PATH);

		OWLArtModelFactory<RDF4JModelConfiguration> fact = OWLArtModelFactory
				.createModelFactory(new ARTModelFactoryRDF4JImpl());
		rdfModel = new RDFModelImpl(fact.createLightweightRDFModel());
		rdfModel.setDefaultNamespace(BASE_URI);

		codaCore = CODAStandaloneFactory.getInstance(bundlesDirectory, cacheDirectory);
		codaCore.initialize(rdfModel, fact);
	}
	
	// Tear down CODA
	@AfterClass
	public static void teardown() throws ModelUpdateException {
		if (codaCore != null) {
			codaCore.stopAndClose();
		} else if (rdfModel != null) {
			rdfModel.close();
		}
	}
	
	@Test
	public void testProduceURI() throws Exception {
		
		/// Initialize the input CAS
		
		// Load the type system descriptor
		// (located at src/test/boys.wae/converters/impls/typeSystemDescriptor.xml)
		TypeSystemDescription tsDes = UIMAFramework.getXMLParser().parseTypeSystemDescription(
				new XMLInputSource(aIT.class.getResource("typeSystemDescriptor.xml")));

		// Create a CAS object with the type system above
		CAS aCas = CasCreationUtils.createCas(tsDes, null, null);

		// Get a reference to the type of test annotations
		Type testAnnotationType = aCas.getTypeSystem().getType("boys.wae.TestAnnotation");
		Feature testFeature = testAnnotationType.getFeatureByBaseName("testFeature");

		// Create an annotation of the given type (with a dummy range 0-0)
		FeatureStructure testAnnotationFS = aCas.createAnnotation(testAnnotationType,0,0);
		
		// Set the value of the testFeature 
		testAnnotationFS.setFeatureValueFromString(testFeature, "hello");

		// Add the annotation to the index
		aCas.addFsToIndexes(testAnnotationFS);

		//// Load the test PEARL rule
		try (InputStream is = aIT.class.getResourceAsStream("projectionRules.pr")) {
			codaCore.setProjectionRulesModelAndParseIt(is);
		}
		
		//// Execute CODA
		codaCore.setJCas(aCas.getJCas());
		
		//// Assertions
		SuggOntologyCoda suggOntCoda = codaCore.processNextAnnotation(true);

		assertThat(suggOntCoda, is(notNullValue()));

		List<ARTNode> objects = suggOntCoda.getAllARTTriple().stream()
				.filter(triple -> triple.getPredicate().equals(RDF.Res.VALUE)).map(ARTTriple::getObject)
				.collect(toList());

		ARTNodeFactory nodeFactory = new ARTNodeFactoryImpl();

		assertThat(objects,
				containsInAnyOrder(nodeFactory.createURIResource("http://example.org/hello"),
						nodeFactory.createLiteral("hello", "en"),
						nodeFactory.createLiteral("hello", XmlSchema.Res.STRING)));
	}

}
