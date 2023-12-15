/**
 * Copyright (C) 2020, 1C-Soft LLC
 */
package ru.akpaev.dt.openapi;

import org.eclipse.osgi.util.NLS;

/**
 * Данный класс - представитель локализации механизма строк в Eclipse.
 */
final class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.akpaev.dt.openapi.messages"; //$NON-NLS-1$

    public static String DataProcessingHandler_Error_not_document_object_module;
    public static String DataProcessingHandler_Error_no_accumulation_registers;
    public static String DataProcessingHandler_Error;

    public static String DataProcessingHandlerDialog_dialog_title;
    public static String DataProcessingHandlerDialog_dialog_message;
    public static String DataProcessingHandlerDialog_dialog_text;
    public static String DataProcessingHandlerDialog_Registers;
    public static String DataProcessingHandlerDialog_Document_attributes;
    public static String DataProcessingHandlerDialog_Field;
    public static String DataProcessingHandlerDialog_Expressions;

    public static String CreateFromOpenApiSpecHandler_FailedToWriteHttpServiceModuleContent;

    public static String CreateFromOpenApiSpecHandler_GeneratingOfHttpService;

    public static String CreateFromOpenApiSpecHandler_SelectOpenAPIDialogMessage;

    public static String CreateFromOpenApiSpecHandler_WritingOfHttpServiceModule;

    public static String ImportOpenApiSettingsDialog_CreateMethodsHandlers;

    public static String ImportOpenApiSettingsDialog_ImportSettings;

    public static String ImportOpenApiSettingsDialog_Name;

    public static String ImportOpenApiSettingsDialog_RootURL;

    public static String ImportOpenApiSpecWizard_0;

    public static String SettingsPage_Name;

    public static String SettingsPage_RootURL;

    public static String SettingsPage_SettingsPageTitle;

    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
