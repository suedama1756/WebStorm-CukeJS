//package org.jetbrains.plugins.cucumber.javascript;
//
//import com.intellij.openapi.util.SystemInfo;
//import com.intellij.openapi.util.io.FileUtil;
//import com.intellij.openapi.vfs.VirtualFile;
//import com.intellij.util.PathUtil;
//import java.io.File;
//import org.jetbrains.annotations.NonNls;
//
//public class CucumberJavaScriptUtil
//{
//    @NonNls
//    static final String jarPath = PathUtil.getJarPathForClass(CucumberJavaScriptUtil.class);
//    @NonNls
//    public static final String STEP_DEFINITIONS_DIR_NAME = "step_definitions";
//    public static final String WINDOWS_FORMATTER_FILE_NAME = "cucumberjs_formatter_windows.js";
//    public static final String NIX_FORMATTER_FILE_NAME = "cucumberjs_formatter_nix.js";
//
//    public CucumberJavaScriptUtil() {}
//
//    public static boolean isStepDefinitionsRoot(VirtualFile stepDefsRoot)
//    {
//        return (stepDefsRoot.isDirectory()) && ("step_definitions".equals(stepDefsRoot.getName()));
//    }
//
//    public static String getSMFormatterPath()
//    {
//        File jarFile = new File(jarPath);
//        String formatterFileName = SystemInfo.isWindows ? "cucumberjs_formatter_windows.js" : "cucumberjs_formatter_nix.js";
//        if (jarPath.endsWith(".jar")) {
//            return FileUtil.toSystemIndependentName(jarFile.getParentFile() + "/" + formatterFileName);
//        }
//        return FileUtil.toSystemIndependentName(jarFile.getParentFile() + "/CucumberJavaScript/org/jetbrains/plugins/cucumber/javascript/run/" + formatterFileName);
//    }
//}
