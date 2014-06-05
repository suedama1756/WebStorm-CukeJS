package com.jdml.cucumber.js.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.GenericProgramRunner;
import com.intellij.execution.runners.RunContentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class CucumberRunner extends GenericProgramRunner
{
    public CucumberRunner() {}

    @NotNull
    public String getRunnerId()
    {
        return "CucumberJSRunner";
    }

    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile)
    {
        return (executorId.equals(DefaultRunExecutor.EXECUTOR_ID)) && ((profile instanceof CucumberRunConfiguration));
    }

    protected RunContentDescriptor doExecute(@NotNull Project project, @NotNull RunProfileState state, RunContentDescriptor contentToReuse, @NotNull ExecutionEnvironment env)
            throws ExecutionException
    {
        FileDocumentManager.getInstance().saveAllDocuments();
        ExecutionResult executionResult = state.execute(env.getExecutor(), this);
        if (executionResult == null) {
            return null;
        }
        RunContentBuilder contentBuilder = new RunContentBuilder(this, executionResult, env);
        return contentBuilder.showRunContent(contentToReuse);
    }
}
