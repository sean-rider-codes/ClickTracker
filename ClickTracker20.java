/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clicktracker2.pkg0;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 *
 * @author SeanR
 */
public class ClickTracker20 implements NativeMouseInputListener{

    /**
     * @param args the command line arguments
     */
    double x;
    double y;
    String program = "";
    private static int MaxTitleLength = 125;
    static String point = "";
    static String username = "";
    
    public static void main(String[] args) 
    {
        //turn off logger
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        
        //initiate Global mouse listener
        try
        {
            GlobalScreen.registerNativeHook();
        }
        catch(NativeHookException ex)
        {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        
        ClickTracker20 ex = new ClickTracker20();
        
        GlobalScreen.addNativeMouseListener(ex);
        GlobalScreen.addNativeMouseMotionListener(ex);
     
    }
    
    public void nativeMouseClicked(NativeMouseEvent e) {
        //System.out.println("Mouse Clicked: " + e.getClickCount());
        x = e.getX(); //x coordinate
        y = e.getY(); //y coordinate
        program = this.getActiveWindowProcess(); //name of program
        username = System.getProperty("user.name"); //current user of the computer
        
        System.out.println("x: " + x + " " + "y: " + y);
        System.out.println("Program: " + program);
        point = x + ";" + y + ";" + username + ";" + program;
        
        try
        {
            FileOutputStream fos = new FileOutputStream("data.txt", true);
            PrintWriter pw = new PrintWriter(fos);
            pw.println(point);
            
            pw.close();
        }
        catch (FileNotFoundException fnfe)
        {
            System.out.println("Unable to locate file.");
        }
        
        System.out.println(username);
    }

    public void nativeMousePressed(NativeMouseEvent e) {
        //System.out.println("Mouse Pressed: " + e.getButton());
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
        //System.out.println("Mouse Released: " + e.getButton());
    }

    public void nativeMouseMoved(NativeMouseEvent e) {
        //System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
    }

    public void nativeMouseDragged(NativeMouseEvent e) {
        //System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
    }
    
    //Load Process Status API
    static class Psapi
     {
          static
          {
               Native.register("psapi");
          }
 
          public static native int GetModuleBaseNameW(Pointer hProcess, Pointer hmodule, char[] lpBaseName, int size);
   }
    //Load Kernel32 API
     static class Kernel32
     {
          static
          {
               Native.register("kernel32");
          }
 
          public static int ProcessQueryInformation = 0x0400;
          public static int ProcessVmRead = 0x0010;
 
          public static native Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);
     }
     //Load User32 API
     static class User32DLL
     {
          static
          {
               Native.register("user32");
          }
 
          public static native int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);
          public static native HWND GetForegroundWindow();
          public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
     }
     //retrieve name of program being clicked on
     private static String getActiveWindowProcess()
     {
        char[] buffer = new char[MaxTitleLength * 2]; // create buffer
        PointerByReference pointer = new PointerByReference(); // create pointer
        HWND foregroundWindow = User32DLL.GetForegroundWindow(); // get active window
        User32DLL.GetWindowThreadProcessId(foregroundWindow, pointer); // Get a reference to the process ID
        Pointer process = Kernel32.OpenProcess(Kernel32.ProcessQueryInformation | Kernel32.ProcessVmRead, false, pointer.getValue()); // get a reference to the process
        Psapi.GetModuleBaseNameW(process, null, buffer, MaxTitleLength); // read the name of the process into buffer
        String processName = Native.toString(buffer); // convert buffer to String
        return processName;
     }

    
}
