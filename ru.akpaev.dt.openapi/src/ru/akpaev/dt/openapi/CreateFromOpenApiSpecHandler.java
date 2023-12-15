/**
 *
 */
package ru.akpaev.dt.openapi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com._1c.g5.v8.bm.core.IBmObject;
import com._1c.g5.v8.bm.core.IBmPlatformTransaction;
import com._1c.g5.v8.bm.integration.IBmPlatformTask;
import com._1c.g5.v8.dt.core.naming.ITopObjectFqnGenerator;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.core.platform.IConfigurationProvider;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.metadata.mdclass.HTTPService;
import com._1c.g5.v8.dt.platform.services.ui.SelectionContextProject;
import com.google.inject.Inject;

import ru.akpaev.dt.openapi.models.OpenApiRoot;

/**
 * @author akpaev.e
 *
 */
public class CreateFromOpenApiSpecHandler
    extends AbstractHandler
{
    @Inject
    private ITopObjectFqnGenerator fqnGenerator;

    @Inject
    private IBmModelManager modelManager;

    @Inject
    private IConfigurationProvider configurationProvider;

    @Inject
    private IResourceLookup resourceLookup;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IWorkbenchWindow windowInfo = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        IProject project = SelectionContextProject.getContextProject(windowInfo.getActivePage());

        var selectedPath = getOpenApiFilePath();
        if (selectedPath == null)
            return null;

        var dialog = new ImportOpenApiSettingsDialog(Display.getCurrent().getActiveShell());
        dialog.create();
        if (dialog.open() == Window.OK)
        {
            OpenApiRoot root = null;

            try
            {
                root = OpenApiRoot.deserializeFromFile(selectedPath);
            }
            catch (Exception e)
            {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", //$NON-NLS-1$
                    e.getLocalizedMessage());
            }

            if (root == null)
                return null;

            var importSettings = dialog.getSettings();

            try
            {
                importSettings.validate();
            }
            catch (Exception e)
            {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", //$NON-NLS-1$
                    e.getLocalizedMessage());
            }

            var service = createNewHttpService(importSettings, root, project);

            if (importSettings.getCreateHandlers())
            {
                try
                {
                    writeHttpServiceModule(service, root);
                }
                catch (Exception e)
                {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", //$NON-NLS-1$
                        e.getLocalizedMessage());
                }
            }
        }

        return null;
    }

    private HTTPService createNewHttpService(ImportOpenApiSpecSettings settings, OpenApiRoot root, IProject project)
    {
        final HTTPService service = HttpServiceUtil.generateFromModel(settings.getName(), settings.getRootUrl(), root);

        var configuration = configurationProvider.getConfiguration(project);
        var bmNamespace = modelManager.getBmNamespace(project);

        return modelManager.getGlobalEditingContext()
            .execute(Messages.CreateFromOpenApiSpecHandler_GeneratingOfHttpService, null, null,
                new IBmPlatformTask<HTTPService>()
                {
                    @Override
                    public HTTPService execute(IBmPlatformTransaction transaction)
                    {
                        transaction.toTransactionObject(configuration).getHttpServices().add(service);

                        var fqn = fqnGenerator.generateStandaloneObjectFqn(service.eClass(), settings.getName());
                        transaction.attachTopObject(bmNamespace, (IBmObject)service, fqn);

                        return service;
                    }
                });

    }

    private void writeHttpServiceModule(HTTPService service, OpenApiRoot root) throws Exception
    {
        var moduleContent = HttpServiceUtil.generateHttpServiceModuleText(service, root);

        Job.create(Messages.CreateFromOpenApiSpecHandler_WritingOfHttpServiceModule, monitor -> {
            IFile serviceResource = resourceLookup.getPlatformResource(service.getModule());
            if (!serviceResource.exists())
            {
                try (InputStream stream = new ByteArrayInputStream(moduleContent.getBytes(StandardCharsets.UTF_8)))
                {
                    serviceResource.create(stream, true, monitor);
                    serviceResource.touch(monitor);
                }
                catch (IOException | CoreException e)
                {
                    Activator.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
                        Messages.CreateFromOpenApiSpecHandler_FailedToWriteHttpServiceModuleContent, e));
                }
            }
        }).schedule();
    }

    private String getOpenApiFilePath()
    {
        var dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setText(Messages.CreateFromOpenApiSpecHandler_SelectOpenAPIDialogMessage);
        dialog.setFilterExtensions(new String[] { "*.yml" }); //$NON-NLS-1$

        return dialog.open();
    }
}
