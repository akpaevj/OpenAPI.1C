/**
 *
 */
package ru.akpaev.dt.openapi;

import org.eclipse.core.runtime.Plugin;

import com._1c.g5.v8.dt.core.naming.ITopObjectFqnGenerator;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.core.platform.IConfigurationProvider;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.wiring.AbstractServiceAwareModule;

public class ExternalDependenciesModule
    extends AbstractServiceAwareModule
{

    public ExternalDependenciesModule(Plugin bundle)
    {
        super(bundle);
    }

    @Override
    protected void doConfigure()
    {
        bind(IConfigurationProvider.class).toService();
        bind(IBmModelManager.class).toService();
        bind(ITopObjectFqnGenerator.class).toService();
        bind(IV8ProjectManager.class).toService();
        bind(IResourceLookup.class).toService();
    }

}