package com.jdml.cucumber.js.execution;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import icons.CucumberIcons;

public class CucumberConfigurationType
        extends ConfigurationTypeBase
{
    protected CucumberConfigurationType()
    {
        super("cucumber-javascript-test-runner", "CucumberJS", "CucumberJS", CucumberIcons.Cucumber);
        addFactory(new CucumberJavaScriptConfigurationFactory(this));
    }

    public static CucumberConfigurationType getInstance()
    {
        return Extensions.findExtension(CONFIGURATION_TYPE_EP, CucumberConfigurationType.class);
    }

    public static class CucumberJavaScriptConfigurationFactory
            extends ConfigurationFactory
    {
        protected CucumberJavaScriptConfigurationFactory(CucumberConfigurationType type)
        {
            super(type);
        }

        @Override
        public RunConfiguration createTemplateConfiguration(Project project) {
            return new CucumberRunConfiguration(project, this, "CukeJS");
        }

        @Override
        public boolean isConfigurationSingletonByDefault() {
            return false;
        }

        @Override
        public boolean canConfigurationBeSingleton() {
            return false;
        }
    }
}
