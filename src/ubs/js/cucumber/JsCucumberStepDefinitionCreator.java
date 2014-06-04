//package org.jetbrains.plugins.cucumber.javascript;
//
//import com.intellij.codeInsight.template.TemplateBuilder;
//import com.intellij.codeInsight.template.TemplateBuilderFactory;
//import com.intellij.lang.ASTNode;
//import com.intellij.lang.javascript.JavaScriptFileType;
//import com.intellij.lang.javascript.psi.JSBlockStatement;
//import com.intellij.lang.javascript.psi.JSCallExpression;
//import com.intellij.lang.javascript.psi.JSExpression;
//import com.intellij.lang.javascript.psi.JSFile;
//import com.intellij.lang.javascript.psi.JSFunction;
//import com.intellij.lang.javascript.psi.JSFunctionExpression;
//import com.intellij.lang.javascript.psi.JSSourceElement;
//import com.intellij.lang.javascript.psi.JSVarStatement;
//import com.intellij.lang.javascript.psi.JSVariable;
//import com.intellij.lang.javascript.psi.impl.JSChangeUtil;
//import com.intellij.lang.javascript.psi.util.JSUtils;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.fileEditor.FileEditorManager;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.util.TextRange;
//import com.intellij.openapi.util.text.StringUtil;
//import com.intellij.psi.PsiDirectory;
//import com.intellij.psi.PsiDocumentManager;
//import com.intellij.psi.PsiElement;
//import com.intellij.psi.PsiFile;
//import com.intellij.psi.PsiWhiteSpace;
//import com.intellij.psi.codeStyle.CodeStyleManager;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.plugins.cucumber.AbstractStepDefinitionCreator;
//import org.jetbrains.plugins.cucumber.CucumberUtil;
//import org.jetbrains.plugins.cucumber.psi.GherkinStep;
//
//public class CucumberJavaScriptStepDefinitionCreator
//        extends AbstractStepDefinitionCreator
//{
//    private static final List<String> FORBIDDEN_STEP_KEYWORD = Arrays.asList(new String[] { "And", "But", "*" });
//    private static final String JAVASCRIPT_FILE_EXTENSION = '.' + JavaScriptFileType.INSTANCE.getDefaultExtension();
//    private static final String DEFAULT_ARGUMENT_NAME = "arg";
//    public static final String STRING_ARGUMENT_REGEX = "(\"[^\"]*\")";
//    public static final String NUMBER_ARGUMENT_REGEX = "(?:^|[ ])(\\d+)(?:[ ]|$)";
//    private static Pattern PARAM_REGEXP = Pattern.compile("(\"[^\"]*\")|(?:^|[ ])(\\d+)(?:[ ]|$)");
//
//    public CucumberJavaScriptStepDefinitionCreator() {}
//
//    @NotNull
//    public PsiFile createStepDefinitionContainer(@NotNull PsiDirectory dir, @NotNull String name)
//    {
//        if (dir == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "createStepDefinitionContainer" }));
//        }
//        if (name == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "createStepDefinitionContainer" }));
//        }
//        PsiFile tmp106_101 = dir.createFile(name + JAVASCRIPT_FILE_EXTENSION);
//        if (tmp106_101 == null) {
//            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "createStepDefinitionContainer" }));
//        }
//        return tmp106_101;
//    }
//
//    private static JSFunction findWrapper(JSFile jsFile)
//    {
//        for (JSSourceElement st : jsFile.getStatements()) {
//            if ((st instanceof JSVarStatement))
//            {
//                JSVariable[] variables = ((JSVarStatement)st).getVariables();
//                if (variables.length == 1)
//                {
//                    JSExpression varInitializer = variables[0].getInitializer();
//                    if ((varInitializer != null) && ((varInitializer instanceof JSFunction))) {
//                        return (JSFunction)varInitializer;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    private static PsiElement generateStepDefinition(@NotNull GherkinStep step, @NotNull PsiFile file)
//    {
//        if (step == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "generateStepDefinition" }));
//        }
//        if (file == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "generateStepDefinition" }));
//        }
//        String keyword;
//        String keyword;
//        if (FORBIDDEN_STEP_KEYWORD.contains(step.getKeyword().getText()))
//        {
//            PsiElement prevStep = step.getPrevSibling();
//            while ((((prevStep instanceof GherkinStep)) && (FORBIDDEN_STEP_KEYWORD.contains(((GherkinStep)prevStep).getKeyword().getText()))) || ((prevStep instanceof PsiWhiteSpace))) {
//                prevStep = prevStep.getPrevSibling();
//            }
//            String keyword;
//            if ((prevStep instanceof GherkinStep)) {
//                keyword = ((GherkinStep)prevStep).getKeyword().getText();
//            } else {
//                keyword = "Given";
//            }
//        }
//        else
//        {
//            keyword = step.getKeyword().getText();
//        }
//        String template = "this.%s(/^%s$/, function (%scallback) {\n  callback.pending();\n});\n";
//
//
//
//
//        String params = getArguments(step);
//        String stepRegex = getStepRegex(step);
//        String elementText = String.format("this.%s(/^%s$/, function (%scallback) {\n  callback.pending();\n});\n", new Object[] { keyword, stepRegex, params });
//        ASTNode expression = JSChangeUtil.createStatementFromText(file.getProject(), elementText, JSUtils.getDialect(file));
//        return expression.getPsi();
//    }
//
//    private static String getStepRegex(GherkinStep step)
//    {
//        String result = step.getStepName();
//        result = CucumberUtil.prepareStepRegexp(result);
//        return result;
//    }
//
//    private static String getArguments(GherkinStep step)
//    {
//        List<String> arguments = new ArrayList();
//        Matcher m = PARAM_REGEXP.matcher(step.getStepName());
//        int i = 0;
//        while (m.find())
//        {
//            String argument = m.group();
//            if (argument.startsWith("<"))
//            {
//                arguments.add(argument.substring(1, argument.length() - 1));
//            }
//            else
//            {
//                i++;
//                arguments.add("arg" + i);
//            }
//        }
//        if (step.getPystring() != null) {
//            arguments.add("text");
//        }
//        String result = StringUtil.join(arguments, ", ");
//        if (result.length() > 0) {
//            result = result + ", ";
//        }
//        return result;
//    }
//
//    private static JSFunction createStepDefsWrapper(@NotNull PsiFile file)
//    {
//        if (file == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "createStepDefsWrapper" }));
//        }
//        String text = "var myStepDefinitionsWrapper = function () {\n};";
//
//
//
//        ASTNode varStatementNode = JSChangeUtil.createStatementFromText(file.getProject(), "var myStepDefinitionsWrapper = function () {\n};", JSUtils.getDialect(file));
//        JSVarStatement varStatement = (JSVarStatement)varStatementNode.getPsi();
//        varStatement = (JSVarStatement)file.add(varStatement);
//
//        String exportModuleText = "module.exports = myStepDefinitionsWrapper;";
//        ASTNode exportModuleNode = JSChangeUtil.createStatementFromText(file.getProject(), "module.exports = myStepDefinitionsWrapper;", JSUtils.getDialect(file));
//        PsiElement exportModuleElement = exportModuleNode.getPsi();
//        file.add(exportModuleElement);
//
//        JSVariable variable = varStatement.getVariables()[0];
//        return (JSFunctionExpression)variable.getInitializer();
//    }
//
//    public boolean createStepDefinition(@NotNull GherkinStep step, @NotNull PsiFile file)
//    {
//        if (step == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "createStepDefinition" }));
//        }
//        if (file == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "createStepDefinition" }));
//        }
//        if (!(file instanceof JSFile)) {
//            return false;
//        }
//        Project project = file.getProject();
//        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
//        assert (editor != null);
//
//        closeActiveTemplateBuilders(file);
//
//        JSFunction stepDefContainer = findWrapper((JSFile)file);
//        if (stepDefContainer == null) {
//            stepDefContainer = createStepDefsWrapper(file);
//        }
//        PsiElement stepDefinition = generateStepDefinition(step, file);
//        if (stepDefinition != null)
//        {
//            PsiElement blockCandidate = stepDefContainer.getLastChild();
//            if ((blockCandidate instanceof JSBlockStatement))
//            {
//                if (blockCandidate.getChildren().length > 0) {
//                    JSChangeUtil.addWs(blockCandidate.getNode(), blockCandidate.getLastChild().getNode(), "\n");
//                }
//                stepDefinition = JSChangeUtil.doAddBefore(blockCandidate, stepDefinition, blockCandidate.getLastChild());
//                CodeStyleManager.getInstance(project).reformatNewlyAddedElement(blockCandidate.getNode(), stepDefinition.getNode());
//
//                editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
//                assert (editor != null);
//
//                TemplateBuilder builder = TemplateBuilderFactory.getInstance().createTemplateBuilder(stepDefinition);
//
//                JSCallExpression callExpression = (JSCallExpression)stepDefinition.getFirstChild();
//                JSExpression[] argumentList = callExpression.getArguments();
//                if (argumentList.length > 1)
//                {
//                    JSExpression regex = argumentList[0];
//                    if (regex.getTextLength() > 4)
//                    {
//                        TextRange regexRange = new TextRange(2, regex.getTextLength() - 2);
//                        builder.replaceElement(regex, regexRange, regexRange.substring(regex.getText()));
//                    }
//                    JSExpression stepFunction = argumentList[1];
//                    if ((stepFunction instanceof JSFunction))
//                    {
//                        JSSourceElement[] body = ((JSFunction)stepFunction).getBody();
//                        if (body.length > 0)
//                        {
//                            JSSourceElement stepDefBody = body[0];
//                            PsiElement[] stepDefElements = stepDefBody.getChildren();
//                            if (stepDefElements.length > 0)
//                            {
//                                PsiElement pendingElement = stepDefElements[0];
//                                TextRange pendingRange = new TextRange(0, pendingElement.getTextLength() - 1);
//                                builder.replaceElement(pendingElement, pendingRange, pendingRange.substring(pendingElement.getText()));
//                            }
//                        }
//                    }
//                }
//                PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
//                documentManager.doPostponedOperationsAndUnblockDocument(editor.getDocument());
//
//                builder.run(editor, false);
//            }
//        }
//        return false;
//    }
//
//    public boolean validateNewStepDefinitionFileName(@NotNull Project project, @NotNull String fileName)
//    {
//        if (project == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "validateNewStepDefinitionFileName" }));
//        }
//        if (fileName == null) {
//            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "validateNewStepDefinitionFileName" }));
//        }
//        return true;
//    }
//
//    @NotNull
//    public String getDefaultStepFileName()
//    {
//        String tmp3_0 = "my_steps";
//        if (tmp3_0 == null) {
//            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptStepDefinitionCreator", "getDefaultStepFileName" }));
//        }
//        return tmp3_0;
//    }
//}
