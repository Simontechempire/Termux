package com.facelesstech.termux;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import com.termux.terminal.*;
import java.io.File;

public class TerminalService extends Service {
    public class TerminalBinder extends Binder { TerminalService getService() { return TerminalService.this; } }
    private final IBinder binder = new TerminalBinder();
    @Override public IBinder onBind(Intent i) { return binder; }

    public TerminalSession createSession() {
        File prefix = new File(getFilesDir(), "usr");
        File home = new File(getFilesDir(), "home");
        if(!home.exists()) home.mkdirs();
        String[] args = {prefix.getAbsolutePath() + "/bin/bash", "-i"};
        String[] env = {
            "TERM=xterm-256color",
            "HOME=" + home.getAbsolutePath(),
            "PREFIX=" + prefix.getAbsolutePath(),
            "PATH=" + prefix.getAbsolutePath() + "/bin:" + prefix.getAbsolutePath() + "/bin/applets:/system/bin",
            "LANG=en_US.UTF-8"
        };
        return new TerminalSession(
            new File(args[0]), home.getAbsolutePath(), args, env,
            TerminalEmulator.DEFAULT_TERMINAL_TRANSCRIPT_ROWS,
            new TerminalSessionClient()
        );
    }

    static class TerminalSessionClient implements TerminalSessionClientInterface {
        public void onTextChanged(TerminalSession s) {}
        public void onTitleChanged(TerminalSession s) {}
        public void onSessionFinished(TerminalSession s) {}
        public void onCopyTextToClipboard(TerminalSession s, String t) {}
        public void onPasteTextFromClipboard(TerminalSession s) {}
        public void onBell(TerminalSession s) {}
        public void onColorsChanged(TerminalSession s) {}
        public void onTerminalCursorStateChange(boolean b) {}
    }
}
