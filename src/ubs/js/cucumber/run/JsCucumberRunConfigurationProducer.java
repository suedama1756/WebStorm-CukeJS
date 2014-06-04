package ubs.js.cucumber.run;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.ConfigurationFromContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.cucumber.psi.GherkinFile;

public class JsCucumberRunConfigurationProducer
        extends RunConfigurationProducer<JsCucumberRunConfiguration>
        implements Cloneable
{
    public static final String FEATURES_FOLDER_NAME = "features";

    protected JsCucumberRunConfigurationProducer()
    {
        super(JsCucumberRunConfigurationType.getInstance());
    }

    private static boolean hasParentCalledFeatures(PsiDirectory directory)
    {
        while (directory != null)
        {
            if (directory.getName().equals("features")) {
                return true;
            }
            directory = directory.getParentDirectory();
        }
        return false;
    }

    protected boolean setupConfigurationFromContext(JsCucumberRunConfiguration configuration, ConfigurationContext context, Ref<PsiElement> sourceElement)
    {
        PsiElement element = (PsiElement)sourceElement.get();
        if ((element != null) && (configuration != null))
        {
            if ((element.getContainingFile() instanceof GherkinFile))
            {
                configuration.setName(element.getContainingFile().getName());
                configuration.getRunnerParameters().setFilePath(FileUtil.toSystemIndependentName(element.getContainingFile().getVirtualFile().getPath()));
                return true;
            }
            if (((element instanceof PsiDirectory)) && (hasParentCalledFeatures((PsiDirectory)element)))
            {
                configuration.setName(((PsiDirectory)element).getName());
                configuration.getRunnerParameters().setFilePath(FileUtil.toSystemIndependentName(((PsiDirectory)element).getVirtualFile().getPath()));
                return true;
            }
        }
        return false;
    }

    public boolean isConfigurationFromContext(JsCucumberRunConfiguration configuration, ConfigurationContext context)
    {
        PsiElement location = context.getPsiLocation();
        if (location != null)
        {
            String configurationFilePath = configuration.getRunnerParameters().getFilePath();
            String contextFilePath = null;
            String contextName = null;
            if ((location.getContainingFile() instanceof GherkinFile))
            {
                contextFilePath = location.getContainingFile().getVirtualFile().getPath();
                contextName = location.getContainingFile().getName();
            }
            else if ((location instanceof PsiDirectory))
            {
                contextFilePath = ((PsiDirectory)location).getVirtualFile().getPath();
                contextName = ((PsiDirectory)location).getName();
            }
            return (contextFilePath != null) && (configurationFilePath.equals(contextFilePath)) && (configuration.getName().equals(contextName));
        }
        return false;
    }

    public boolean isPreferredConfiguration(ConfigurationFromContext self, ConfigurationFromContext other)
    {
        return true;
    }
}
