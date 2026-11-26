package com.facelesstech.termux;

import android.content.*;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.termux.view.TerminalView;
import com.termux.terminal.TerminalSession;

public class MainActivity extends AppCompatActivity {
    TerminalView terminalView;
    TerminalSession session;
    ExtraKeysView extraKeysView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        terminalView = findViewById(R.id.terminalView);

        BootstrapInstaller.installIfNeeded(this, () -> {
            Intent intent = new Intent(this, TerminalService.class);
            startService(intent);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        });
    }

    ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName n, IBinder b) {
            TerminalService.TerminalBinder binder = (TerminalService.TerminalBinder) b;
            session = binder.getService().createSession();
            terminalView.attachSession(session);
            terminalView.setTerminalCursorBlinkerState(true, 0);
            extraKeysView = new ExtraKeysView(MainActivity.this, findViewById(R.id.extraKeysLayout), terminalView);
            extraKeysView.setup();
        }
        public void onServiceDisconnected(ComponentName n) {}
    };

    @Override protected void onDestroy() { super.onDestroy(); try{unbindService(connection);}catch(Exception e){} }
}
