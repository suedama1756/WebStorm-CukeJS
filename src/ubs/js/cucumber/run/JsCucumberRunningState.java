package ubs.js.cucumber.run;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil;
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class JsCucumberRunningState
        extends CommandLineState
{
    private final JsCucumberRunnerParameters  myRunnerParameters;
    private final JsCucumberRunConfiguration myRunConfiguration;

    public JsCucumberRunningState(ExecutionEnvironment env, JsCucumberRunnerParameters runnerParameters, JsCucumberRunConfiguration runConfiguration)
    {
        super(env);
        this.myRunnerParameters = runnerParameters;
        this.myRunConfiguration = runConfiguration;
    }

    @NotNull
    protected ProcessHandler startProcess()
            throws ExecutionException
    {
        GeneralCommandLine commandLine = getCommand();

            OSProcessHandler osProcessHandler = new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
        if (osProcessHandler == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "org/jetbrains/plugins/cucumber/javascript/run/CucumberJavaScriptRunningState", "startProcess" }));
        }
        return osProcessHandler;
    }

    public GeneralCommandLine getCommand()
            throws ExecutionException
    {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        if (this.myRunnerParameters.getFilePath() == null) {
            throw new ExecutionException("Can't find feature to run");
        }
        VirtualFile fileToRun = VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(this.myRunnerParameters.getFilePath()));

        String smFormatterPath = ""; //CucumberJavaScriptUtil.getSMFormatterPath();
        if (StringUtil.isEmpty(this.myRunnerParameters.getExecutablePath()))
        {
            if (SystemInfo.isWindows)
            {
                String windowsCucumberPath = "cucumber-js.cmd";
                try
                {
                    Process p = Runtime.getRuntime().exec("where cucumber-js.cmd");
                    p.waitFor();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    String[] lines = sb.toString().split("\n");
                    if (lines.length > 0) {
                        windowsCucumberPath = lines[0];
                    }
                }
                catch (Throwable t) {}
                commandLine.setExePath(windowsCucumberPath);
            }
            else
            {
                commandLine.setExePath("/usr/local/bin/cucumber.js");
            }
        }
        else {
            commandLine.setExePath(this.myRunnerParameters.getExecutablePath());
        }
        if (fileToRun != null)
        {
            if ((fileToRun.getExtension() != null) && (fileToRun.getExtension().equals("feature")))
            {
                commandLine.addParameter("--name");
                commandLine.addParameter(fileToRun.getPath());
            }
            if (!fileToRun.isDirectory()) {
                fileToRun = fileToRun.getParent();
            }
            commandLine.setWorkDirectory(fileToRun.getParent().getPath());
        }
        else
        {
            throw new ExecutionException("Can't find feature to run");
        }
        if (this.myRunnerParameters.getCucumberJsArguments().length() > 0) {
            commandLine.addParameters(this.myRunnerParameters.getCucumberJsArguments().split(" "));
        }
        commandLine.addParameter("-format=summary");
        commandLine.addParameter("--require");
        commandLine.addParameter(fileToRun.getPath());
        commandLine.addParameter("--require");
        commandLine.addParameter(smFormatterPath);

        commandLine.setPassParentEnvironment(true);
        return commandLine;
    }

    @Nullable
    private ConsoleView createConsole(@NotNull Executor executor, ProcessHandler processHandler)
            throws ExecutionException
    {
        if (executor == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/run/CucumberJavaScriptRunningState", "createConsole" }));
        }
        String testFrameworkName = "cucumber";
        SMTRunnerConsoleProperties consoleProperties = new SMTRunnerConsoleProperties(this.myRunConfiguration, "cucumber", executor);

        ConsoleView testRunnerConsole = SMTestRunnerConnectionUtil.createAndAttachConsole("cucumber", processHandler, consoleProperties, getEnvironment());


        Disposer.register(this.myRunConfiguration.getProject(), testRunnerConsole);
        return testRunnerConsole;
    }

    @NotNull
    public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner)
            throws ExecutionException
    {
        if (executor == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/run/CucumberJavaScriptRunningState", "execute" }));
        }
        if (runner == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/run/CucumberJavaScriptRunningState", "execute" }));
        }
        ProcessHandler processHandler = startProcess();
        ConsoleView console = createConsole(executor, processHandler);
        DefaultExecutionResult executionResult = new DefaultExecutionResult(console, processHandler, createActions(console, processHandler, executor));
        if (executionResult == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "org/jetbrains/plugins/cucumber/javascript/run/CucumberJavaScriptRunningState", "execute" }));
        }
        return executionResult;
    }
}
