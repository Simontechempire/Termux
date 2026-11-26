package com.facelesstech.termux;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import com.termux.view.TerminalView;

public class ExtraKeysView {
    Context ctx;
    LinearLayout layout;
    TerminalView terminalView;
    String[] keys = {"ESC","TAB","CTRL","ALT","-","/","|","<",">","HOME","END","UP","DOWN","PGUP","PGDN"};

    public ExtraKeysView(Context c, LinearLayout l, TerminalView tv) { ctx=c; layout=l; terminalView=tv; }

    public void setup() {
        for (String k : keys) {
            Button b = new Button(ctx);
            b.setText(k);
            b.setTextColor(0xFFFFFFFF);
            b.setBackgroundColor(0xFF222222);
            b.setPadding(20, 8, 20, 8);
            b.setOnClickListener(v -> sendKey(k));
            layout.addView(b);
        }
    }

    void sendKey(String k) {
        if (terminalView.getCurrentSession() == null) return;
        switch(k) {
            case "ESC": terminalView.getCurrentSession().write("\u001B"); break;
            case "TAB": terminalView.getCurrentSession().write("\t"); break;
            case "CTRL": break; // Hold logic in full version
            case "UP": terminalView.getCurrentSession().write("\u001B[A"); break;
            case "DOWN": terminalView.getCurrentSession().write("\u001B[B"); break;
            default: terminalView.getCurrentSession().write(k); break;
        }
    }
}
