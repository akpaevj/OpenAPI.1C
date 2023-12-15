package ru.akpaev.dt.openapi;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import org.antlr.stringtemplate.StringTemplate;

import com._1c.g5.v8.dt.metadata.mdclass.HTTPMethod;
import com._1c.g5.v8.dt.metadata.mdclass.HTTPService;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassFactory;
import com._1c.g5.v8.dt.metadata.mdclass.Method;
import com._1c.g5.v8.dt.metadata.mdclass.URLTemplate;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;

import ru.akpaev.dt.openapi.models.OpenApiMethod;
import ru.akpaev.dt.openapi.models.OpenApiParameter;
import ru.akpaev.dt.openapi.models.OpenApiPath;
import ru.akpaev.dt.openapi.models.OpenApiRoot;

public class HttpServiceUtil
{
    private static String methodTemplateContent = readResourceContent("http_method_handler_template.txt"); //$NON-NLS-1$
    private static String pathParamTemplateContent = readResourceContent("get_path_param_template.txt"); //$NON-NLS-1$
    private static String queryParamTemplateContent = readResourceContent("get_query_param_template.txt"); //$NON-NLS-1$

    public static HTTPService generateFromModel(String name, String rootUrl, OpenApiRoot root)
    {
        var service = createHttpService(name, rootUrl);

        root.getPaths().forEach((uri, path) -> {
            var template = createUrlTemplate(uri);
            service.getUrlTemplates().add(template);

            path.getMethods().forEach((methodType, mDesc) -> {
                var method = createMethod(template, methodType, mDesc);
                template.getMethods().add(method);
            });
        });

        return service;
    }

    public static String getMethodHandlerName(URLTemplate urlTemplate, Method method)
    {
        return urlTemplate.getName() + method.getName();
    }

    public static String generateHttpServiceModuleText(HTTPService service, OpenApiRoot root) throws Exception
    {
        var moduleBuilder = new StringBuilder();

        for (var template : service.getUrlTemplates())
        {
            var openApiUrlTemplate = root.getPaths().get(template.getTemplate());
            var openApiMethods = openApiUrlTemplate.getMethods();

            for (var method : template.getMethods())
            {
                var methodTemplate = new StringTemplate(methodTemplateContent);
                methodTemplate.setAttribute("HANDLER_NAME", method.getHandler()); //$NON-NLS-1$

                // generate path and query parameters code block
                var openApiMethod = openApiMethods.get(method.getHttpMethod().name().toLowerCase());
                var paramsBlock = generateParamsBlock(openApiUrlTemplate, openApiMethod, root);
                methodTemplate.setAttribute("PARAMS_BODY", paramsBlock); //$NON-NLS-1$

                moduleBuilder.append(methodTemplate.toString());
                moduleBuilder.append(System.lineSeparator());
                moduleBuilder.append(System.lineSeparator());
            }
        }

        return moduleBuilder.toString();
    }

    private static String generateParamsBlock(OpenApiPath path, OpenApiMethod method, OpenApiRoot root) throws Exception
    {
        var paramsBlockBuilder = new StringBuilder();

        var templateParameters = path.getParameters();
        var methodParameters = method.getParameters();

        var templateHasParameters = templateParameters != null && templateParameters.size() > 0;
        var methodHasParameters = methodParameters != null && methodParameters.size() > 0;
        var hasParameters = templateHasParameters && methodHasParameters;

        if (hasParameters)
            paramsBlockBuilder.append(System.lineSeparator());

        if (templateHasParameters)
        {
            var parametersBlock = generateParametersText(templateParameters, root);
            paramsBlockBuilder.append(parametersBlock);
        }

        if (methodHasParameters)
        {
            var parametersBlock = generateParametersText(methodParameters, root);
            paramsBlockBuilder.append(parametersBlock);
        }

        if (hasParameters)
            paramsBlockBuilder.append(System.lineSeparator());

        return paramsBlockBuilder.toString();
    }

    private static StringBuilder generateParametersText(ArrayList<OpenApiParameter> parameters, OpenApiRoot root)
        throws Exception
    {
        var builder = new StringBuilder();

        for (var openApiParameter : parameters)
        {
            var parameter = openApiParameter;
            if (parameter.isRef())
                parameter = root.resolveReference(openApiParameter.getRef(), openApiParameter.getClass());

            var paramTemplateContent = ""; //$NON-NLS-1$
            var in = parameter.getIn();
            switch (in)
            {
            case "path": //$NON-NLS-1$
                paramTemplateContent = pathParamTemplateContent;
                break;
            case "query": //$NON-NLS-1$
                paramTemplateContent = queryParamTemplateContent;
            default:
                break;
            }

            if (paramTemplateContent.length() > 0)
            {
                var paramTemplate = new StringTemplate(paramTemplateContent);
                paramTemplate.setAttribute("PARAM_NAME", parameter.getName()); //$NON-NLS-1$

                builder.append(paramTemplate.toString());
                builder.append(System.lineSeparator());
            }
        }

        return builder;
    }

    private static HTTPService createHttpService(String name, String rootUrl)
    {
        var service = MdClassFactory.eINSTANCE.createHTTPService();

        service.setUuid(UUID.randomUUID());
        service.setRootURL(rootUrl);
        service.setName(name);

        return service;
    }

    private static URLTemplate createUrlTemplate(String uri)
    {
        var template = MdClassFactory.eINSTANCE.createURLTemplate();

        template.setUuid(UUID.randomUUID());
        template.setName(OpenApiPath.generateUrlTemplateName(uri));
        template.setTemplate(uri);

        return template;
    }

    private static Method createMethod(URLTemplate template, String methodType, OpenApiMethod openApiMethod)
    {
        var method = MdClassFactory.eINSTANCE.createMethod();

        method.setUuid(UUID.randomUUID());
        method.setName(methodType.toLowerCase());
        method.setHttpMethod(HTTPMethod.getByName(methodType.toUpperCase()));

        if (openApiMethod.hasOperationId())
            method.setHandler(openApiMethod.getOperationId());
        else
            method.setHandler(getMethodHandlerName(template, method));

        return method;
    }

    private static String readResourceContent(String path)
    {
        var source = Resources.asCharSource(CreateFromOpenApiSpecHandler.class.getResource("/resources/" + path), //$NON-NLS-1$
            StandardCharsets.UTF_8);

        try (Reader reader = source.openBufferedStream())
        {
            return CharStreams.toString(reader);
        }
        catch (IOException | NullPointerException e)
        {
            return ""; //$NON-NLS-1$
        }
    }
}
