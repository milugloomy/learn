package com.baqi.jni;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

public class Windows {

    private static final Kernel32 KERNEL = Kernel32.INSTANCE;
    private static final User32 user32 = User32.INSTANCE;

    public static void main(String[] args) {
        WinDef.HWND hwnd = user32.GetActiveWindow();
        System.out.println(hwnd);
    }
}
