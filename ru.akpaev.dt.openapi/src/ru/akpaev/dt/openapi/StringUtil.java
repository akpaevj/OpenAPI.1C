/**
 *
 */
package ru.akpaev.dt.openapi;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author akpaev.e
 *
 */
public class StringUtil
{
    public static ArrayList<String> allMatches(Pattern pattern, String input)
    {
        var result = new ArrayList<String>();
        Matcher m = pattern.matcher(input);
        while (m.find())
        {
            result.add(m.group());
        }

        return result;
    }

    public static StringBuilder capitalizeFirstChar(String word)
    {
        var result = new StringBuilder();

        result.append(word.substring(0, 1).toUpperCase());
        result.append(word.substring(1).toLowerCase());

        return result;
    }
}
