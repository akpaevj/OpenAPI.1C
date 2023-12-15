/**
 *
 */
package ru.akpaev.dt.openapi.models;

import java.util.ArrayList;

/**
 * @author akpaev.e
 *
 */
public class OpenApiMethod
{
    private String summary;
    private String operationId;
    private ArrayList<OpenApiParameter> parameters;

    public ArrayList<OpenApiParameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(ArrayList<OpenApiParameter> parameters)
    {
        this.parameters = parameters;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getOperationId()
    {
        return operationId;
    }

    public void setOperationId(String operationId)
    {
        this.operationId = operationId;
    }

    public boolean hasOperationId()
    {
        return operationId != null && operationId.length() > 0;
    }
}
