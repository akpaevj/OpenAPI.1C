/**
 *
 */
package ru.akpaev.dt.openapi.models;

import java.util.LinkedHashMap;

/**
 * @author akpaev.e
 *
 */
public class OpenApiComponents
{
    private LinkedHashMap<String, OpenApiSchema> schemas;
    private LinkedHashMap<String, OpenApiParameter> parameters;

    public LinkedHashMap<String, OpenApiSchema> getSchemas()
    {
        return schemas;
    }

    public void setSchemas(LinkedHashMap<String, OpenApiSchema> schemas)
    {
        this.schemas = schemas;
    }

    public LinkedHashMap<String, OpenApiParameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, OpenApiParameter> parameters)
    {
        this.parameters = parameters;
    }
}
