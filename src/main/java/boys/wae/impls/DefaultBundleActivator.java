package boys.wae.impls;

import static it.uniroma2.art.coda.osgi.utils.CODAOSGiRegistrationUtils.registerConverter;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import boys.wae.converters.impls.a;

/**
 * Default {@code BundleActivator} registering the converters that are defined in the current bundle.
 * 
 * TODO remember to register newly defined conveters
 */
public class DefaultBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		registerConverter(context, a.class);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// Nothing to do
	}

}