package com.facelesstech.termux;

public class PkgHelper {
    public static final String REPO_MAIN = "https://packages.termux.dev/apt/termux-main stable main";
    public static final String[] DEFAULT_PACKAGES = {"bash", "coreutils", "apt", "pkg"};
    // First commands user runs after install:
    // pkg update -y && pkg upgrade -y
    // pkg install nodejs git python -y
}
