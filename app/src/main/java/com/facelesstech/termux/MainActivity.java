package com.facelesstech.termux;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class MainActivity extends AppCompatActivity {
    TextView output;
    EditText input;
    Button runBtn;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = findViewById(R.id.output);
        input = findViewById(R.id.input);
        runBtn = findViewById(R.id.runBtn);
        print("Welcome to Faceless Tech Termux v1.0\nType command and tap RUN\n$ ");
        runBtn.setOnClickListener(v -> {
            String cmd = input.getText().toString().trim();
            if(cmd.isEmpty()) return;
            print(cmd + "\n");
            input.setText("");
            runCommand(cmd);
        });
    }
    void print(String s){ output.append(s); }
    void runCommand(String cmd){
        new Thread(() -> {
            try {
                if(cmd.equals("clear")){ runOnUiThread(() -> output.setText("$ ")); return; }
                if(cmd.startsWith("echo ")){ String out = cmd.substring(5) + "\n$ "; runOnUiThread(() -> print(out)); return; }
                if(cmd.equals("help")){ runOnUiThread(() -> print("Commands: echo, clear, ls, pwd, help\n$ ")); return; }
                Process p = Runtime.getRuntime().exec(new String[]{"sh","-c",cmd});
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder sb = new StringBuilder(); String line;
                while((line = r.readLine()) != null) sb.append(line).append("\n");
                p.waitFor();
                String result = sb.toString() + "$ ";
                runOnUiThread(() -> print(result));
            } catch(Exception e){ runOnUiThread(() -> print("Error: " + e.getMessage() + "\n$ ")); }
        }).start();
    }
                }
