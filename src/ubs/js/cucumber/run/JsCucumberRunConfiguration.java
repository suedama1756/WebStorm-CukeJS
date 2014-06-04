package ubs.js.cucumber.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ubs.js.cucumber.run.ui.JsCucumberConfigurationEditorForm;


public class JsCucumberRunConfiguration
        extends LocatableConfigurationBase
{
    private final static String errorPath = JsCucumberRunConfiguration.class.getCanonicalName().replace('.', '/');
    private final JsCucumberRunnerParameters parameters = new JsCucumberRunnerParameters();

    protected JsCucumberRunConfiguration(Project project, ConfigurationFactory factory, String name)
    {
        super(project, factory, name);
    }


    @NotNull
    public SettingsEditor<? extends com.intellij.execution.configurations.RunConfiguration> getConfigurationEditor()
    {
        return new JsCucumberConfigurationEditorForm(getProject());
    }

    @Nullable
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env)
            throws ExecutionException
    {
        if (executor == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null",
                    "0", errorPath, "getState"));
        }
        if (env == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null",
                    "1", errorPath, "getState" ));
        }
        Project project = env.getProject();
        String path = this.parameters.getFilePath();
        if ((path == null) || (VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(path)) == null)) {
            throw new ExecutionException("Can't find file: " + path);
        }
        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(path));
        assert (virtualFile != null);
        Module module = ModuleUtilCore.findModuleForFile(virtualFile, project);
        if (module == null) {
            throw new ExecutionException("Can't find module for file");
        }
        return new JsCucumberRunningState(env, this.parameters, this);
    }

    public void checkConfiguration()
            throws RuntimeConfigurationException
    {
        String path = this.parameters.getFilePath();
        if ((path == null) || (VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(path)) == null)) {
            throw new RuntimeConfigurationException("Can't find file: " + path);
        }
    }

    public void writeExternal(Element element)
            throws WriteExternalException
    {
        super.writeExternal(element);
        XmlSerializer.serializeInto(this.parameters, element);
    }

    public void readExternal(Element element)
            throws InvalidDataException
    {
        super.readExternal(element);
        XmlSerializer.deserializeInto(this.parameters, element);
    }

    public JsCucumberRunnerParameters getRunnerParameters()
    {
        return this.parameters;
    }
}
