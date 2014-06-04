package ubs.js.cucumber.run.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.psi.GherkinFileType;
import ubs.js.cucumber.run.JsCucumberRunConfiguration;

public class JsCucumberConfigurationEditorForm
        extends SettingsEditor<JsCucumberRunConfiguration> {
    private JPanel mainPanel;
    private TextFieldWithBrowseButton fileField;
    private JTextField argumentsField;
    private TextFieldWithBrowseButton executableField;

    public JsCucumberConfigurationEditorForm(Project project) {
        super();

        FileChooserDescriptor fileToRunChooser = new FileChooserDescriptor(true, true, false, false, false, false) {
            public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
                return (file.isDirectory()) || (file.getFileType() == GherkinFileType.INSTANCE);
            }

            public boolean isFileSelectable(VirtualFile file) {
                return isFileVisible(file, false);
            }
        };
        String chooseFileToRunMessage = "Orange"; //CucumberJavaScriptBundle.message("choose.gherkin.file.or.directory", new Object[0]);
        this.fileField.addBrowseFolderListener(chooseFileToRunMessage, chooseFileToRunMessage, project, fileToRunChooser);

        FileChooserDescriptor executableChooser = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
        String chooseExecutableMessage = "Banana"; //CucumberJavaScriptBundle.message("cucumber.js.run.choose.executable.path.title", new Object[0]);
        this.executableField.addBrowseFolderListener(chooseExecutableMessage, chooseExecutableMessage, project, executableChooser);
    }

    protected void resetEditorFrom(JsCucumberRunConfiguration configuration) {
        this.fileField.setText(configuration.getRunnerParameters().getFilePath());
        this.argumentsField.setText(configuration.getRunnerParameters().getCucumberJsArguments());
        this.executableField.setText(configuration.getRunnerParameters().getExecutablePath());
    }

    protected void applyEditorTo(JsCucumberRunConfiguration configuration)
            throws ConfigurationException {
        configuration.getRunnerParameters().setFilePath(this.fileField.getText());
        configuration.getRunnerParameters().setCucumberJsArguments(this.argumentsField.getText());
        configuration.getRunnerParameters().setExecutablePath(this.executableField.getText());
    }

    @NotNull
    protected JComponent createEditor() {
        return this.mainPanel;
    }

    protected void disposeEditor() {
    }
}
