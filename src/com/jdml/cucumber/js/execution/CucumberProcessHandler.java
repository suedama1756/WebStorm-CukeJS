package com.jdml.cucumber.js.execution;

import com.google.common.base.Charsets;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CucumberProcessHandler
        extends KillableColoredProcessHandler
{
    private static final int TIMEOUT_SEC = 3;
    private static final String FORCE_QUIT_MESSAGE = "\nProcess hasn't been terminated within 3 seconds after the stop attempt. IDE has terminated it forcibly.\n";

    public CucumberProcessHandler(@NotNull Process process, String commandLine)
    {
        super(process, commandLine, Charsets.UTF_8);
    }

    protected void doDestroyProcess()
    {
        super.doDestroyProcess();

        final Alarm alarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD);
        alarm.addRequest(new Runnable()
        {
            public void run()
            {
                CucumberProcessHandler handler = CucumberProcessHandler.this;
                if (!handler.isProcessTerminated() && handler.canKillProcess())
                {
                    handler.notifyTextAvailable(FORCE_QUIT_MESSAGE, ProcessOutputTypes.SYSTEM);
                    handler.killProcess();
                }
                Disposer.dispose(alarm);
            }
        }, TimeUnit.SECONDS.toMillis(TIMEOUT_SEC), ModalityState.any());
    }
}
