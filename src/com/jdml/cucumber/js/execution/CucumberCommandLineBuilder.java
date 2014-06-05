package com.jdml.cucumber.js.execution;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CucumberCommandLineBuilder {
    private final Project _project;

    public CucumberCommandLineBuilder(@NotNull Project project) {
        _project = project;
    }

    private String getWorkingDirectory(CucumberRunSettings runSettings) {
        String featurePath = runSettings.getFeaturePath();

        File file = new File(featurePath);
        return file.exists() && file.isDirectory() ?
                file.getPath() :
                file.getParentFile().getPath();
    }

    public GeneralCommandLine build(CucumberRunSettings runSettings) {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setWorkDirectory(getWorkingDirectory(runSettings));
        commandLine.setExePath("node.exe");

        //  must be first parameter after node
        if (runSettings.getDebugPort() != -1) {
            commandLine.addParameter("--debug-brk=" + runSettings.getDebugPort());
        }

        //  add path to cucumber.js
        String parameter = FileUtil.join(runSettings.getCucumberPath(),
                "bin/cucumber.js");
        commandLine.addParameter(parameter);

        //  add feature path
        parameter = runSettings.getFeaturePath();
        commandLine.addParameter(parameter);

        //  pull in default require options
        commandLine.addParameter("--require");
        commandLine.addParameter(".");
        commandLine.addParameter("--require");
        commandLine.addParameter(CucumberTeamcityHook.getPath());

        //  add additional options
        parameter = runSettings.getArguments().trim();
        if (StringUtil.isNotEmpty(parameter)) {
            commandLine.addParameters(ParametersList.parse(parameter));
        }

        return commandLine;
    }
}
