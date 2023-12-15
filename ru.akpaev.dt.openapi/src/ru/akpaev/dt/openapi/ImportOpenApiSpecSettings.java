/**
 *
 */
package ru.akpaev.dt.openapi;

public class ImportOpenApiSpecSettings
{
    private String rootUrl;
    private String name;
    private boolean createHandlers = false;

    public String getRootUrl()
    {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl)
    {
        this.rootUrl = rootUrl;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean getCreateHandlers()
    {
        return createHandlers;
    }

    public void setCreateHandlers(boolean createHandlers)
    {
        this.createHandlers = createHandlers;
    }

    public void validate() throws Exception
    {
        if (name == null || name.isEmpty())
            throw new Exception("Name of the service mustn't be null or empty"); //$NON-NLS-1$

        if (rootUrl == null || rootUrl.isEmpty())
            throw new Exception("RootURL of the service mustn't be null or empty"); //$NON-NLS-1$
    }
}