/**
 *
 */
package ru.akpaev.dt.openapi.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import ru.akpaev.dt.openapi.StringUtil;

/**
 * @author akpaev.e
 *
 */
public class OpenApiPath
{
    private static Pattern simplePartsPattern = Pattern.compile("(?<=(/))[.[^{}]]+?(?=(/|$))"); //$NON-NLS-1$
    private static Pattern paramPartsPattern = Pattern.compile("(?<=\\{).*?(?=\\})"); //$NON-NLS-1$

    private OpenApiMethod get;
    private OpenApiMethod post;
    private OpenApiMethod put;
    private OpenApiMethod patch;
    private OpenApiMethod delete;
    private OpenApiMethod head;
    private OpenApiMethod options;
    private OpenApiMethod trace;
    private ArrayList<OpenApiParameter> parameters;

    public ArrayList<OpenApiParameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(ArrayList<OpenApiParameter> parameters)
    {
        this.parameters = parameters;
    }

    public OpenApiMethod getGet()
    {
        return get;
    }

    public void setGet(OpenApiMethod get)
    {
        this.get = get;
    }

    public OpenApiMethod getPost()
    {
        return post;
    }

    public void setPost(OpenApiMethod post)
    {
        this.post = post;
    }

    public OpenApiMethod getPut()
    {
        return put;
    }

    public void setPut(OpenApiMethod put)
    {
        this.put = put;
    }

    public OpenApiMethod getPatch()
    {
        return patch;
    }

    public void setPatch(OpenApiMethod patch)
    {
        this.patch = patch;
    }

    public OpenApiMethod getDelete()
    {
        return delete;
    }

    public void setDelete(OpenApiMethod delete)
    {
        this.delete = delete;
    }

    public OpenApiMethod getHead()
    {
        return head;
    }

    public void setHead(OpenApiMethod head)
    {
        this.head = head;
    }

    public OpenApiMethod getOptions()
    {
        return options;
    }

    public void setOptions(OpenApiMethod options)
    {
        this.options = options;
    }

    public OpenApiMethod getTrace()
    {
        return trace;
    }

    public void setTrace(OpenApiMethod trace)
    {
        this.trace = trace;
    }

    public Map<String, OpenApiMethod> getMethods()
    {
        var result = new LinkedHashMap<String, OpenApiMethod>();

        if (get != null)
            result.put("get", getGet()); //$NON-NLS-1$
        if (post != null)
            result.put("post", getPost()); //$NON-NLS-1$
        if (put != null)
            result.put("put", getPut()); //$NON-NLS-1$
        if (patch != null)
            result.put("patch", getPatch()); //$NON-NLS-1$
        if (delete != null)
            result.put("delete", getDelete()); //$NON-NLS-1$
        if (head != null)
            result.put("head", getHead()); //$NON-NLS-1$
        if (options != null)
            result.put("options", getOptions()); //$NON-NLS-1$
        if (trace != null)
            result.put("trace", getTrace()); //$NON-NLS-1$

        return result;
    }

    public static String generateUrlTemplateName(String path)
    {
        var simpleParts = StringUtil.allMatches(simplePartsPattern, path);
        var urlParams = StringUtil.allMatches(paramPartsPattern, path);

        var result = new StringBuilder();

        simpleParts.forEach(part -> {
            result.append(StringUtil.capitalizeFirstChar(part));
        });

        urlParams.forEach(part -> {
            result.append("By" + StringUtil.capitalizeFirstChar(part)); //$NON-NLS-1$
        });

        return result.toString();
    }
}
