package com.facelesstech.termux;

import com.termux.terminal.TerminalSession;
import java.util.ArrayList;

public class SessionManager {
    public static ArrayList<TerminalSession> sessions = new ArrayList<>();
    public static int current = 0;

    public static void add(TerminalSession s) { sessions.add(s); current = sessions.size()-1; }
    public static TerminalSession getCurrent() { if(sessions.isEmpty()) return null; return sessions.get(current); }
    public static void remove(TerminalSession s) { sessions.remove(s); if(current>=sessions.size()) current = sessions.size()-1; }
    public static void next() { if(!sessions.isEmpty()) current = (current+1)%sessions.size(); }
}
