package com.jdml.cucumber.js.execution;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class CucumberRunConfigurationParametersSerializer
{
    private static final String FEATURE_DIRECTORY_KEY = "feature-directory";
    private static final String ARGUMENTS_KEY = "arguments";
    private static final String CUCUMBER_DIRECTORY_KEY = "cucumber-directory";
    private static final String TAGS_KEY = "tags";


    private CucumberRunConfigurationParametersSerializer() {}

    @NotNull
    public static void readInto(@NotNull Element parent, @NotNull CucumberRunConfigurationParameters parameters)
    {
        String featurePath = readTag(parent, FEATURE_DIRECTORY_KEY);
        parameters.setFeaturePath(FileUtil.toSystemDependentName(featurePath));

        String arguments = readTag(parent, ARGUMENTS_KEY);
        parameters.setArguments(arguments);

        String tags = readTag(parent, TAGS_KEY);
        parameters.setTags(tags);

        String cucumberPath = readTag(parent, CUCUMBER_DIRECTORY_KEY);
        parameters.setCucumberPath(FileUtil.toSystemDependentName(cucumberPath));
    }

    public static void write(@NotNull Element parent, @NotNull CucumberRunConfigurationParameters parameters)
    {
        String featurePath = FileUtil.toSystemIndependentName(parameters.getFeaturePath());
        writeTag(parent, FEATURE_DIRECTORY_KEY, featurePath);

        String arguments = parameters.getArguments();
        writeTag(parent, ARGUMENTS_KEY, arguments);

        String tags = parameters.getTags();
        writeTag(parent, TAGS_KEY, tags);

        String cucumberPath = FileUtil.toSystemIndependentName(parameters.getCucumberPath());
        writeTag(parent, CUCUMBER_DIRECTORY_KEY, cucumberPath);
    }

    @NotNull
    private static String readTag(@NotNull Element parent, @NotNull String tagName)
    {
        Element child = parent.getChild(tagName);
        return child != null ? StringUtil.notNullize(child.getText()) : "";
    }

    private static void writeTag(@NotNull Element parent, @NotNull String tagName, @NotNull String value)
    {
        Element element = new Element(tagName);
        element.setText(value);
        parent.addContent(element);
    }
}
