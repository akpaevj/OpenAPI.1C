/**
 *
 */
package ru.akpaev.dt.openapi;

import org.osgi.framework.Bundle;

import com._1c.g5.wiring.AbstractGuiceAwareExecutableExtensionFactory;
import com.google.inject.Injector;

/**
 * @author akpaev.e
 *
 */
public class ExecutableExtensionFactory
    extends AbstractGuiceAwareExecutableExtensionFactory
{

    @Override
    protected Bundle getBundle()
    {
        return Activator.getDefault().getBundle();
    }

    @Override
    protected Injector getInjector()
    {
        return Activator.getDefault().getInjector();
    }

}
