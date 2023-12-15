/**
 *
 */
package ru.akpaev.dt.openapi.models;

/**
 * @author akpaev.e
 *
 */
public class OpenApiInfo
{
    private String version;
    private String title;
    private String description;

    public String getVersion()
    {
        return version;
    }
    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
}
