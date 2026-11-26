package com.facelesstech.termux;

import 【entity-android¦canonical_name=android】.content.Context;
import 【entity-android¦canonical_name=android】.os.Handler;
import 【entity-android¦canonical_name=android】.os.Looper;
import java.io.*;
import java.net.*;
import java.util.zip.ZipInputStream;

public class BootstrapInstaller {
    static String BOOTSTRAP_URL = "https://github.com/termux/termux-packages/releases/download/bootstrap-2024.10.12/bootstrap-aarch64.zip";

    public static void installIfNeeded(Context ctx, Runnable onDone) {
        File prefix = new File(ctx.getFilesDir(), "usr");
        File doneFile = new File(prefix, ".faceless-bootstrap-done");
        if (doneFile.exists()) { onDone.run(); return; }
        new Thread(() -> {
            try {
                prefix.mkdirs();
                URL url = new URL(BOOTSTRAP_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                ZipInputStream zin = new ZipInputStream(new BufferedInputStream(conn.getInputStream()));
                java.util.zip.ZipEntry entry;
                byte[] buf = new byte[8192];
                while ((entry = zin.getNextEntry()) != null) {
                    File out = new File(ctx.getFilesDir(), entry.getName());
                    if (entry.isDirectory()) { out.mkdirs(); continue; }
                    out.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(out);
                    int n; while ((n = zin.read(buf)) != -1) fos.write(buf, 0, n);
                    fos.close(); zin.closeEntry();
                }
                zin.close();
                exec("chmod -R 755 " + prefix.getAbsolutePath() + "/bin");
                exec("chmod -R 755 " + prefix.getAbsolutePath() + "/bin/applets");
                doneFile.createNewFile();
                new Handler(Looper.getMainLooper()).post(onDone);
            } catch (Exception e) { e.printStackTrace(); new Handler(Looper.getMainLooper()).post(onDone); }
        }).start();
    }
    static void exec(String cmd) { try { Runtime.getRuntime().exec(new String[]{"sh","-c",cmd}).waitFor(); } catch(Exception e){} }
}
