package com.jdml.cucumber.js.execution;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class CucumberRunConfigurationParametersSerializer
{
    private static final String FEATURE_PATH_KEY = "feature-path";
    private static final String ARGUMENTS_KEY = "arguments";
    private static final String CUCUMBER_PATH_KEY = "cucumber-path";

    private CucumberRunConfigurationParametersSerializer() {}

    @NotNull
    public static void readInto(@NotNull Element parent, @NotNull CucumberRunConfigurationParameters parameters)
    {
        String featurePath = readTag(parent, FEATURE_PATH_KEY);
        parameters.setFeaturePath(FileUtil.toSystemDependentName(featurePath));

        String arguments = readTag(parent, ARGUMENTS_KEY);
        parameters.setArguments(arguments);

        String cucumberPath = readTag(parent, CUCUMBER_PATH_KEY);
        parameters.setCucumberPath(FileUtil.toSystemDependentName(cucumberPath));
    }

    public static void write(@NotNull Element parent, @NotNull CucumberRunConfigurationParameters parameters)
    {
        String featurePath = FileUtil.toSystemIndependentName(parameters.getFeaturePath());
        writeTag(parent, FEATURE_PATH_KEY, featurePath);

        String arguments = parameters.getArguments();
        writeTag(parent, ARGUMENTS_KEY, arguments);

        String cucumberPath = FileUtil.toSystemIndependentName(parameters.getCucumberPath());
        writeTag(parent, CUCUMBER_PATH_KEY, cucumberPath);
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
