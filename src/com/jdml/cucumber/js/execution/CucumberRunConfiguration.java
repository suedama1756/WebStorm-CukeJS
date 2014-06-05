package com.jdml.cucumber.js.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.net.NetUtils;
import com.intellij.util.xmlb.XmlSerializer;
import com.jdml.cucumber.js.execution.ui.CucumberConfigurationEditor;
import com.jetbrains.nodejs.debug.NodeJSDebuggableConfiguration;
import com.jetbrains.nodejs.debug.NodeJSFileFinder;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;


public class CucumberRunConfiguration
        extends LocatableConfigurationBase
        implements NodeJSDebuggableConfiguration
{
    private final static String errorPath = CucumberRunConfiguration.class.getCanonicalName().replace('.', '/');
    private final CucumberRunConfigurationParameters parameters = new CucumberRunConfigurationParameters();

    protected CucumberRunConfiguration(Project project, ConfigurationFactory factory, String name)
    {
        super(project, factory, name);
    }

    @NotNull
    public SettingsEditor<CucumberRunConfiguration> getConfigurationEditor()
    {
        return new CucumberConfigurationEditor(getProject());
    }

    @Nullable
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env)
            throws ExecutionException
    {
        return createState(env, -1);
    }

    @NotNull
    public RunProfileState createState(@NotNull ExecutionEnvironment environment, int debugPort)
        throws ExecutionException
    {
        checkConfigurationForState();

        CucumberRunSettings settings = new CucumberRunSettings.Builder()
                .setArguments(parameters.getArguments())
                .setCucumberPath(parameters.getCucumberPath())
                .setFeaturePath(parameters.getFeaturePath())
                .setDebugPort(debugPort)
                .build();

        return new CucumberRunProfileState(this, settings, environment);
    }

    private void checkConfigurationForState() throws ExecutionException {
        try {
            checkConfiguration();
        }
        catch (RuntimeConfigurationError e) {
            throw new ExecutionException(e.getMessage(), e);
        }
        catch (RuntimeConfigurationException e) {
            /* ignored in other plugins, not sure why, should investigate */
        }
    }

    public void checkConfiguration()
            throws RuntimeConfigurationException
    {
        String path = this.parameters.getFeaturePath();
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

    public CucumberRunConfigurationParameters getRunnerParameters()
    {
        return this.parameters;
    }

    @Override
    public String getHost() {
        return "127.0.0.1";
    }

    @Override
    public int computeDebugPort() throws ExecutionException {
        try {
            return NetUtils.findAvailableSocketPort();
        }
        catch (IOException e) {
            throw new ExecutionException("Cannot find available port for debugging", e);
        }
    }

    @NotNull
    @Override
    public NodeJSFileFinder createFileFinder() {
        return new NodeJSFileFinder(getProject());
    }
}
