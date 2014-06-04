//package org.jetbrains.plugins.cucumber.javascript;
//
//import com.intellij.lang.javascript.psi.JSCallExpression;
//import com.intellij.lang.javascript.psi.JSLiteralExpression;
//import com.intellij.psi.PsiElement;
//import java.util.Collections;
//import java.util.List;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition;
//
//public class JavaScriptStepDefinition
//        extends AbstractStepDefinition
//{
//    public JavaScriptStepDefinition(@NotNull PsiElement element)
//    {
//        super(element);
//    }
//
//    public List<String> getVariableNames()
//    {
//        return Collections.emptyList();
//    }
//
//    @Nullable
//    protected String getCucumberRegexFromElement(PsiElement element)
//    {
//        JSCallExpression call = (JSCallExpression)getElement();
//        PsiElement firstArgument = call.getArguments()[0];
//        JSLiteralExpression regex = (JSLiteralExpression)firstArgument;
//        return regex.getText().substring(1, regex.getTextLength() - 1);
//    }
//}
