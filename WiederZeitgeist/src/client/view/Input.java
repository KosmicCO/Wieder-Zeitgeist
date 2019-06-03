/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import static client.ClientListener.CLIENT_LISTENER;
import java.util.BitSet;
import messages.client.view.KeyPress;
import messages.client.view.MouseButton;
import messages.client.view.MousePosition;
import messages.client.view.MouseWheel;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import util.math.VectorN;

/**
 *
 * @author TARS
 */
public class Input {

    private static final BitSet buttons = new BitSet();
    private static final BitSet keys = new BitSet();

    private static VectorN mouse = VectorN.zeros(2);
    private static VectorN wheel = VectorN.zeros(2);

    /**
     * Gets the current state of the mouse button.
     *
     * @param button The mouse button to query.
     * @return The state of the mouse button.
     */
    public static boolean buttonPressed(int button) {
        return buttons.get(button);
    }

    static void initialize() {

        Window.setCursorPosCallback((w, x, y) -> {
            VectorN newMouse = VectorN.of(x, Window.getDimensions().y() - y);
            CLIENT_LISTENER.receiveMessage(new MousePosition(newMouse, newMouse.sub(mouse)));
            mouse = newMouse;
        });

        Window.setKeyCallback((w, k, sc, a, m) -> {
            boolean changed = (keys.get(k) != (a != GLFW_RELEASE));
            keys.set(k, a != GLFW_RELEASE);
            CLIENT_LISTENER.receiveMessage(new KeyPress(k, a != GLFW_RELEASE, changed));
        });

        Window.setMouseButtonCallback((w, b, a, m) -> {
            boolean changed = (buttons.get(b) != (a != GLFW_RELEASE));
            buttons.set(b, a != GLFW_RELEASE);
            CLIENT_LISTENER.receiveMessage(new MouseButton(b, a != GLFW_RELEASE, changed, mouse));
        });

        Window.setScrollCallback((w, xo, yo) -> {
            VectorN newOffset = VectorN.of(xo, yo);
            CLIENT_LISTENER.receiveMessage(new MouseWheel(newOffset, newOffset.sub(wheel)));
            wheel = newOffset;
        });
    }

    /**
     * Gets the current state of the key.
     *
     * @param key The key to query.
     * @return The state of the key.
     */
    public static boolean keyPressed(int key) {
        return keys.get(key);
    }

    /**
     * Gets the current position of the mouse.
     *
     * @return The position of the mouse.
     */
    public static VectorN mousePosition() {
        return mouse;
    }

    /**
     * Gets the current wheel offset.
     *
     * @return The offset of the mouse wheel.
     */
    public static VectorN mouseWheel() {
        return wheel;
    }
}
