package com.jdml.cucumber.js.debug;

import com.jdml.cucumber.js.execution.CucumberRunConfiguration;
import com.jetbrains.nodejs.debug.BaseNodeJsRunner;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.executors.DefaultDebugExecutor;
import org.jetbrains.annotations.NotNull;

public class CucumberDebugRunner extends BaseNodeJsRunner
{
    public String getRunnerId()
    {
        return "CucumberJSDebugRunner";
    }

    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile)
    {
        return DefaultDebugExecutor.EXECUTOR_ID.equals(executorId) && (profile instanceof CucumberRunConfiguration);
    }
}
