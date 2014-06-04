package ubs.js.cucumber.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.util.Icons;


public class JsCucumberRunConfigurationType
        extends ConfigurationTypeBase
{
    protected JsCucumberRunConfigurationType()
    {
        super("JsCucumberRunConfigurationType", "JsCucumber", "JsCucumber", Icons.TEST_SOURCE_FOLDER);
        addFactory(new CucumberJavaScriptConfigurationFactory(this));
    }

    public static JsCucumberRunConfigurationType getInstance()
    {
        return Extensions.findExtension(CONFIGURATION_TYPE_EP, JsCucumberRunConfigurationType.class);
    }

    public static class CucumberJavaScriptConfigurationFactory
            extends ConfigurationFactory
    {
        protected CucumberJavaScriptConfigurationFactory(JsCucumberRunConfigurationType type)
        {
            super(type);
        }

        public RunConfiguration createTemplateConfiguration(Project project)
        {
            return new JsCucumberRunConfiguration(project, this, "JsCucumber");
        }
    }
}
