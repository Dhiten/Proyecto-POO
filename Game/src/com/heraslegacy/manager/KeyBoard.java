package com.heraslegacy.manager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import javafx.scene.input.KeyCode;

public class KeyBoard implements KeyListener {

    private static boolean keys[] = new boolean[500];
    private static boolean keysStatic[] = new boolean[500];
    public static boolean up, down, left, right,restart,delete,enter, space, one, doix, trois, quatre, cinq, six, sept, huit,neuf, zero,soltado,escape;
    public static int rate = 2;
    public void uptade() {
        up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
        restart = keys[KeyEvent.VK_R];
        space = keys[KeyEvent.VK_SPACE];
        delete = keys[KeyEvent.VK_BACK_SPACE];
        enter = keys[KeyEvent.VK_ENTER];
        one = keys[KeyEvent.VK_1];

        
        doix = keys[KeyEvent.VK_2];
        trois = keys[KeyEvent.VK_3];
        quatre = keys[KeyEvent.VK_4];
        cinq = keys[KeyEvent.VK_5];
        six = keys[KeyEvent.VK_6];
        sept = keys[KeyEvent.VK_7];
        huit = keys[KeyEvent.VK_8];
        neuf = keys[KeyEvent.VK_9];
        zero = keys[KeyEvent.VK_0];
        escape = keysStatic[KeyEvent.VK_ESCAPE];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()<500){
            keys[e.getKeyCode()] = true;
            
            keysStatic[e.getKeyCode()]=true;//!keysStatic[e.getKeyCode()]?true:false; 
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()<500){
            keys[e.getKeyCode()] = false;
            rate=2;
        }
    }

    public static void setKeysStatic(boolean bool, int ind) {
        KeyBoard.keysStatic[ind] = bool;
    }
}
