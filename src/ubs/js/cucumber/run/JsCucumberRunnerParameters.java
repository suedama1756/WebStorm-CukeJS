package ubs.js.cucumber.run;

public class JsCucumberRunnerParameters
{
    private String filePath = null;
    private String cucumberJsArguments;
    private String executablePath;

    public JsCucumberRunnerParameters() {}

    public String getFilePath()
    {
        return this.filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getCucumberJsArguments()
    {
        return this.cucumberJsArguments;
    }

    public void setCucumberJsArguments(String cucumberJsArguments)
    {
        this.cucumberJsArguments = cucumberJsArguments;
    }

    public String getExecutablePath()
    {
        return this.executablePath;
    }

    public void setExecutablePath(String executablePath)
    {
        this.executablePath = executablePath;
    }
}
