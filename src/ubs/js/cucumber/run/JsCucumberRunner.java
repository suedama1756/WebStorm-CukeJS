package ubs.js.cucumber.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.DefaultProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunContentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class JsCucumberRunner
        extends DefaultProgramRunner
{
    public JsCucumberRunner() {}

    @NotNull
    public String getRunnerId()
    {
        return "JsCucumberRunner";
    }

    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile)
    {
        if (executorId == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/run/JsCucumberRunner", "canRun" }));
        }
        if (profile == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/run/JsCucumberRunner", "canRun" }));
        }
        return (executorId.equals(DefaultRunExecutor.EXECUTOR_ID)) && ((profile instanceof JsCucumberRunConfiguration));
    }

    protected RunContentDescriptor doExecute(@NotNull Project project, @NotNull RunProfileState state, RunContentDescriptor contentToReuse, @NotNull ExecutionEnvironment env)
            throws ExecutionException
    {
        if (project == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/run/JsCucumberRunner", "doExecute" }));
        }
        if (state == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/run/JsCucumberRunner", "doExecute" }));
        }
        if (env == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "3", "org/jetbrains/plugins/cucumber/javascript/run/JsCucumberRunner", "doExecute" }));
        }
        FileDocumentManager.getInstance().saveAllDocuments();
        if (state.execute(env.getExecutor(), this) == null) {
            return null;
        }
        RunContentBuilder contentBuilder = new RunContentBuilder(this, state.execute(env.getExecutor(), this), env);
        return contentBuilder.showRunContent(contentToReuse);
    }
}
