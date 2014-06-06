package com.jdml.cucumber.js.execution;

public class CucumberRunConfigurationParameters
{
    private String featurePath = null;
    private String arguments;
    private String executablePath;
    private String tags;

    public CucumberRunConfigurationParameters() {}

    public String getFeaturePath()
    {
        return this.featurePath;
    }

    public void setFeaturePath(String filePath)
    {
        this.featurePath = filePath;
    }

    public String getArguments()
    {
        return this.arguments;
    }

    public void setArguments(String arguments)
    {
        this.arguments = arguments;
    }

    public String getCucumberPath()
    {
        return this.executablePath;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public void setCucumberPath(String executablePath)
    {
        this.executablePath = executablePath;
    }
}
