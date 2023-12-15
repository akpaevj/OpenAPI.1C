/**
 *
 */
package ru.akpaev.dt.openapi.models;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * @author akpaev.e
 *
 */
public class OpenApiParameter
    implements IReferencedNode
{
    @JsonAlias("$ref")
    private String ref;
    private String name;
    private String in;
    private boolean required;
    private OpenApiSchema schema;

    @Override
    public String getRef()
    {
        return ref;
    }

    public void setRef(String ref)
    {
        this.ref = ref;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIn()
    {
        return in;
    }

    public void setIn(String in)
    {
        this.in = in;
    }

    @Override
    public boolean isRef()
    {
        return ref != null && ref.length() > 0;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public OpenApiSchema getSchema()
    {
        return schema;
    }

    public void setSchema(OpenApiSchema schema)
    {
        this.schema = schema;
    }
}
