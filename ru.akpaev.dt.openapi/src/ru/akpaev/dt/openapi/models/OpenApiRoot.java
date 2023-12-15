/**
 *
 */
package ru.akpaev.dt.openapi.models;

import java.io.FileInputStream;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Charsets;

import ru.akpaev.dt.openapi.Activator;
import ru.akpaev.dt.openapi.StringUtil;

/**
 * @author akpaev.e
 *
 */
public class OpenApiRoot
{
    private static Pattern inFileRefPartsPattern = Pattern.compile("(?<=/).+?(?=(/|$))"); //$NON-NLS-1$

    private String fileContent;

    private OpenApiInfo info;
    private Map<String, OpenApiPath> paths;
    private OpenApiComponents components;

    public static OpenApiRoot deserializeFromFile(String path)
    {
        var root = new OpenApiRoot();

        try (var stream = new FileInputStream(path))
        {
            var fileContent = new String(stream.readAllBytes(), Charsets.UTF_8);

            var mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.findAndRegisterModules();

            root = mapper.readValue(fileContent, OpenApiRoot.class);
            root.setFileContent(fileContent);
        }
        catch (Exception e)
        {
            Activator.logError(e);
        }

        return root;
    }

    public Map<String, OpenApiPath> getPaths()
    {
        return paths;
    }

    public void setPaths(Map<String, OpenApiPath> paths)
    {
        this.paths = paths;
    }

    public OpenApiInfo getInfo()
    {
        return info;
    }

    public void setInfo(OpenApiInfo info)
    {
        this.info = info;
    }

    public OpenApiComponents getComponents()
    {
        return components;
    }

    public void setComponents(OpenApiComponents components)
    {
        this.components = components;
    }

    public <T extends IReferencedNode> T resolveReference(String ref, Class<T> type) throws Exception
    {
        // this is in-file reference
        if (ref.startsWith("#/")) //$NON-NLS-1$
        {
            var pathParts = StringUtil.allMatches(inFileRefPartsPattern, ref);

            var mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.findAndRegisterModules();

            var node = mapper.readTree(fileContent);
            for (var pathPart : pathParts)
                node = node.get(pathPart);

            var nodeText = node.toPrettyString();
            T value = mapper.readValue(nodeText, type);
            if (value.isRef())
                return resolveReference(value.getRef(), type);
            else
                return value;
        }
        else
            throw new Exception("Currently only in-file references are supported"); //$NON-NLS-1$
    }

    protected void setFileContent(String content)
    {
        fileContent = content;
    }
}
