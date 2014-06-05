package com.jdml.cucumber.js.execution;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil;
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CucumberRunProfileState
        implements RunProfileState
{
    private final CucumberRunSettings _settings;
    private final ExecutionEnvironment _environment;
    private final CucumberRunConfiguration _configuration;

    public CucumberRunProfileState(
            @NotNull CucumberRunConfiguration configuration,
            @NotNull CucumberRunSettings settings,
            @NotNull ExecutionEnvironment environment)
    {
        _configuration = configuration;
        _settings = settings;
        _environment = environment;
    }

    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner)
            throws ExecutionException
    {
        GeneralCommandLine commandLine = new CucumberCommandLineBuilder(_configuration.getProject())
                .build(_settings);
        Process process = commandLine.createProcess();

        OSProcessHandler processHandler = new CucumberProcessHandler(process,
                commandLine.getCommandLineString());
        ConsoleView console = createConsole(executor, processHandler);
        ProcessTerminatedListener.attach(processHandler);

        return new DefaultExecutionResult(console, processHandler);
    }

    @Nullable
    private ConsoleView createConsole(@NotNull Executor executor, ProcessHandler processHandler)
            throws ExecutionException
    {
        if (executor == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/run/CucumberJavaScriptRunningState", "createConsole" }));
        }
        SMTRunnerConsoleProperties consoleProperties = new SMTRunnerConsoleProperties(
                _configuration, "cucumber", executor);
        ConsoleView testRunnerConsole = SMTestRunnerConnectionUtil
                .createAndAttachConsole("cucumber", processHandler, consoleProperties, _environment);
        Disposer.register(_configuration.getProject(), testRunnerConsole);
        return testRunnerConsole;
    }
}
