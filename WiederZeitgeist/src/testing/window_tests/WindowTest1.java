/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing.window_tests;

import static client.ClientListener.CLIENT_LISTENER;
import client.view.Window;
import messages.client.view.KeyPress;
import messages.client.view.WindowShouldClose;
import util.vec.Vector;

/**
 * Testing whether the basic functions of the window and input works properly.
 * @author TARS
 */
public class WindowTest1 {

    /**
     * Tests the basic functionality of the window and input.
     * @param args Does not accept arguments.
     */
    public static void main(String[] args) {

        CLIENT_LISTENER.addListener(KeyPress.class, m -> {
            System.out.println(m.key + " " + m.state + " " + m.changed);
        });
        
        CLIENT_LISTENER.addListener(WindowShouldClose.class, m -> {
            CLIENT_LISTENER.stop();
        });

        CLIENT_LISTENER.start(() -> {
            Window.initialize("Test Window 1", new Vector(400, 400));
        }, () -> {
            Window.cleanupGLFW();
        });
    }
}
