/**
 *
 */
package ru.akpaev.dt.openapi.models;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * @author akpaev.e
 *
 */
public class OpenApiSchema
    implements IReferencedNode
{
    @JsonAlias("$ref")
    private String ref;
    private String type;
    private String format;
    private LinkedHashMap<String, OpenApiSchema> properties;
    private OpenApiSchema items;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public LinkedHashMap<String, OpenApiSchema> getProperties()
    {
        return properties;
    }

    public void setProperties(LinkedHashMap<String, OpenApiSchema> properties)
    {
        this.properties = properties;
    }

    @Override
    public String getRef()
    {
        return ref;
    }

    public void setRef(String ref)
    {
        this.ref = ref;
    }

    @Override
    public boolean isRef()
    {
        return ref != null && ref.length() > 0;
    }

    public OpenApiSchema getItems()
    {
        return items;
    }

    public void setItems(OpenApiSchema items)
    {
        this.items = items;
    }
}
