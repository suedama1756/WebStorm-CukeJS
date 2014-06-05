package com.jdml.cucumber.js.execution;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.PathUtil;

import java.io.File;

public class CucumberTeamcityHook
{
    private static final String jarPath = PathUtil.getJarPathForClass(CucumberTeamcityHook.class);

    public static final String WINDOWS_FORMATTER_FILE_NAME = "cucumberjs_windows.js";
    public static final String NIX_FORMATTER_FILE_NAME = "cucumberjs_nix.js";

    public static String getPath()
    {
        File jarFile = new File(jarPath);
        String formatterFileName = SystemInfo.isWindows ? WINDOWS_FORMATTER_FILE_NAME : NIX_FORMATTER_FILE_NAME;
        if (jarPath.endsWith(".jar")) {
            return FileUtil.toSystemIndependentName(jarFile.getParentFile() + "/" + formatterFileName);
        }

        //  debugging
        return FileUtil.toSystemIndependentName(jarFile.getParentFile() + "/classes/com/jdml/cucumber/js/execution/" + formatterFileName);
    }
}
