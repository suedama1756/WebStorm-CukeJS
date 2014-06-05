package com.jdml.cucumber.js.execution.ui;

import com.intellij.javascript.nodejs.CompletionModuleInfo;
import com.intellij.javascript.nodejs.NodeModuleSearchUtil;
import com.intellij.javascript.nodejs.NodeSettings;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jdml.cucumber.js.execution.CucumberRunConfiguration;
import com.jetbrains.nodejs.settings.NodeSettingsUtil;
import com.jetbrains.nodejs.util.RelativePathUIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.psi.GherkinFileType;

import java.util.ArrayList;

public class CucumberConfigurationEditor
        extends SettingsEditor<CucumberRunConfiguration> {

    private static final String chooseFileToRunMessage = "Feature file or directory";
    private static final String chooseExecutableMessage = "Cucumber installation path";
    private final Project project;

    private JPanel mainPanel;
    private TextFieldWithBrowseButton featurePathField;
    private JTextField argumentsField;
    private TextFieldWithBrowseButton cucumberPathField;

    public CucumberConfigurationEditor(Project project) {
        super();

        this.project = project;

        FileChooserDescriptor fileToRunChooser = new FileChooserDescriptor(true, true, false, false, false, false) {
            public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
                return (file.isDirectory()) || (file.getFileType() == GherkinFileType.INSTANCE);
            }

            public boolean isFileSelectable(VirtualFile file) {
                return isFileVisible(file, false);
            }
        };
        this.featurePathField.addBrowseFolderListener(chooseFileToRunMessage, chooseFileToRunMessage, project, fileToRunChooser);
    }

    private String guessCucumberInstallationPath() {
        NodeSettings nodeSettings = NodeSettingsUtil.getSettings();
        ArrayList<CompletionModuleInfo> modules = new ArrayList<CompletionModuleInfo>();
        NodeModuleSearchUtil.findModulesWithName(modules, "cucumber",
                project.getBaseDir(), nodeSettings, true);
        return modules.size() > 0 ? modules.get(0).getVirtualFile().getPath() : "";
    }

    protected void resetEditorFrom(CucumberRunConfiguration configuration) {
        this.featurePathField.setText(configuration.getRunnerParameters().getFeaturePath());
        this.argumentsField.setText(configuration.getRunnerParameters().getArguments());
        this.cucumberPathField.setText(configuration.getRunnerParameters().getCucumberPath());
        if (this.cucumberPathField.getText().equals("")) {
            this.cucumberPathField.setText(this.guessCucumberInstallationPath());
        }
    }

    protected void applyEditorTo(CucumberRunConfiguration configuration)
            throws ConfigurationException {
        configuration.getRunnerParameters().setFeaturePath(this.featurePathField.getText());
        configuration.getRunnerParameters().setArguments(this.argumentsField.getText());
        configuration.getRunnerParameters().setCucumberPath(this.cucumberPathField.getText());
    }

    @NotNull
    protected JComponent createEditor() {
        return this.mainPanel;
    }

    protected void disposeEditor() {
    }

    private void createUIComponents() {
        this.cucumberPathField = createCucumberPathField(this.project);
    }

    private static TextFieldWithBrowseButton createCucumberPathField(Project project) {
        JTextField workingDir = new JTextField();
        workingDir.setText(project.getBasePath());
        FileChooserDescriptor descriptor =
                FileChooserDescriptorFactory.createSingleFolderDescriptor();
        TextFieldWithBrowseButton result = RelativePathUIUtil.createRelativePathTextFieldAndTrackBaseDirChanges(project,
                descriptor, workingDir.getDocument());
        result.addBrowseFolderListener(null, null, project, descriptor,
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
        return result;
    }
}
