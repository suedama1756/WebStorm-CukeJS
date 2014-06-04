package ubs.js.cucumber;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.lang.javascript.psi.JSRecursiveElementVisitor;
import com.intellij.lang.javascript.psi.impl.JSFileImpl;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.StepDefinitionCreator;
import org.jetbrains.plugins.cucumber.psi.GherkinFile;
import org.jetbrains.plugins.cucumber.psi.GherkinKeywordTable;
import org.jetbrains.plugins.cucumber.psi.PlainGherkinKeywordProvider;
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition;
import org.jetbrains.plugins.cucumber.steps.NotIndexedCucumberExtension;

public class CucumberJsExtension
        extends NotIndexedCucumberExtension
{
    public CucumberJsExtension() {}

    protected void loadStepDefinitionRootsFromLibraries(Module module, List<PsiDirectory> roots, Set<String> directories) {}

    protected Collection<AbstractStepDefinition> getStepDefinitions(@NotNull PsiFile file)
    {
        if (file == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "getStepDefinitions" }));
        }
        PlainGherkinKeywordProvider keywordProvider = new PlainGherkinKeywordProvider();
        final Collection<String> stepKeywords = keywordProvider.getKeywordsTable("en").getStepKeywords();

        final Collection<AbstractStepDefinition> result = new ArrayList();
        file.accept(new JSRecursiveElementVisitor()
        {
            public void visitJSCallExpression(JSCallExpression node)
            {
                String calledMethodName = node.getMethodExpression().getText();
                if (calledMethodName.startsWith("this.")) {
                    calledMethodName = calledMethodName.substring("this.".length());
                }
                if ((stepKeywords.contains(calledMethodName)) &&
                        (node.getArguments().length == 2))
                {
                    PsiElement argument = node.getArguments()[0];
                    if (((argument instanceof JSLiteralExpression)) && (((JSLiteralExpression)argument).isRegExpLiteral()))
                    {
                        //result.add(new JavaScriptStepDefinition(node));
                        return;
                    }
                }
                super.visitJSCallExpression(node);
            }
        });
        return result;
    }

    protected void collectAllStepDefsProviders(@NotNull List<VirtualFile> providers, @NotNull Project project)
    {
        if (providers == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "collectAllStepDefsProviders" }));
        }
        if (project == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "collectAllStepDefsProviders" }));
        }
    }

    public void findRelatedStepDefsRoots(@NotNull final Module module, @NotNull PsiFile featureFile, @NotNull final List<PsiDirectory> newStepDefinitionsRoots, @NotNull final Set<String> processedStepDirectories)
    {
        if (module == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "findRelatedStepDefsRoots" }));
        }
        if (featureFile == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "findRelatedStepDefsRoots" }));
        }
        if (newStepDefinitionsRoots == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "2", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "findRelatedStepDefsRoots" }));
        }
        if (processedStepDirectories == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "3", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "findRelatedStepDefsRoots" }));
        }
        VirtualFile file = featureFile.getVirtualFile();
        if (file == null) {
            return;
        }
        VirtualFile contentRoot = findContentRoot(module, file);
        if (contentRoot == null) {
            return;
        }
        ProjectRootManager.getInstance(featureFile.getProject()).getFileIndex().iterateContentUnderDirectory(contentRoot, new ContentIterator()
        {
            public boolean processFile(VirtualFile stepDefsRoot)
            {
//                if (CucumberJavaScriptUtil.isStepDefinitionsRoot(stepDefsRoot)) {
//                    CucumberJavaScriptExtension.addStepDefsRootIfNecessary(stepDefsRoot, newStepDefinitionsRoots, processedStepDirectories, module.getProject());
//                }
                return true;
            }
        });
    }

    public boolean isStepLikeFile(@NotNull PsiElement child, @NotNull PsiElement parent)
    {
        if (child == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "isStepLikeFile" }));
        }
        if (parent == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "isStepLikeFile" }));
        }
        return ((parent instanceof PsiDirectory)) && ((child instanceof JSFileImpl));
    }

    public boolean isWritableStepLikeFile(@NotNull PsiElement child, @NotNull PsiElement parent)
    {
        if (child == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "isWritableStepLikeFile" }));
        }
        if (parent == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "1", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "isWritableStepLikeFile" }));
        }
        return isStepLikeFile(child, parent);
    }

    @NotNull
    public FileType getStepFileType()
    {
        JavaScriptFileType tmp3_0 = JavaScriptFileType.INSTANCE;
        if (tmp3_0 == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "getStepFileType" }));
        }
        return tmp3_0;
    }

    @NotNull
    public StepDefinitionCreator getStepDefinitionCreator()
    {
//        CucumberJavaScriptStepDefinitionCreatorvoid tmp7_4 = new CucumberJavaScriptStepDefinitionCreator();
//        if (tmp7_4 == null) {
//            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "getStepDefinitionCreator" }));
//        }
//        return tmp7_4;
        return null;
    }

    @NotNull
    public Collection<String> getGlues(@NotNull GherkinFile file, Set<String> gluesFromOtherFiles)
    {
        if (file == null) {
            throw new IllegalArgumentException(String.format("Argument %s for @NotNull parameter of %s.%s must not be null", new Object[] { "0", "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "getGlues" }));
        }
        List tmp43_40 = Collections.emptyList();
        if (tmp43_40 == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "org/jetbrains/plugins/cucumber/javascript/CucumberJavaScriptExtension", "getGlues" }));
        }
        return tmp43_40;
    }
}
