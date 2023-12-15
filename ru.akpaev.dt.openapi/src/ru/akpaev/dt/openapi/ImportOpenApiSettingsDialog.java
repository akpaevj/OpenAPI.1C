/**
 *
 */
package ru.akpaev.dt.openapi;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author akpaev.e
 *
 */
public class ImportOpenApiSettingsDialog
    extends TitleAreaDialog
{
    private Text txtRootURL;
    private Text txtName;
    private Button btnCreateHandlers;

    private ImportOpenApiSpecSettings settings = new ImportOpenApiSpecSettings();

    public ImportOpenApiSettingsDialog(Shell parentShell)
    {
        super(parentShell);
    }

    @Override
    public void create()
    {
        super.create();
        setTitle(Messages.ImportOpenApiSettingsDialog_ImportSettings);
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite area = (Composite)super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        createRootUrlInput(container);
        createNameInput(container);
        createCreateHandlersCheckbox(container);

        return area;
    }

    private void createRootUrlInput(Composite container)
    {
        Label label = new Label(container, SWT.NONE);
        label.setText(Messages.ImportOpenApiSettingsDialog_RootURL);

        GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = GridData.FILL;
        txtRootURL = new Text(container, SWT.BORDER);
        txtRootURL.setLayoutData(gd);
    }

    private void createNameInput(Composite container)
    {
        Label label = new Label(container, SWT.NONE);
        label.setText(Messages.ImportOpenApiSettingsDialog_Name);

        GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = GridData.FILL;
        txtName = new Text(container, SWT.BORDER);
        txtName.setLayoutData(gd);
    }

    private void createCreateHandlersCheckbox(Composite container)
    {
        btnCreateHandlers = new Button(container, SWT.CHECK);
        btnCreateHandlers.setText(Messages.ImportOpenApiSettingsDialog_CreateMethodsHandlers);
        btnCreateHandlers.setSelection(true);

        GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalSpan = 2;
        gd.horizontalAlignment = GridData.FILL;
        btnCreateHandlers.setLayoutData(gd);
    }

    @Override
    protected boolean isResizable()
    {
        return true;
    }

    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private void saveInput()
    {
        settings.setRootUrl(txtRootURL.getText());
        settings.setName(txtName.getText());
        settings.setCreateHandlers(btnCreateHandlers.getSelection());
    }

    @Override
    protected void okPressed()
    {
        saveInput();

        super.okPressed();
    }

    public ImportOpenApiSpecSettings getSettings()
    {
        return settings;
    }
}
