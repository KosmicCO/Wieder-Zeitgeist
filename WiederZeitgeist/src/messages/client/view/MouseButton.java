/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.client.view;

import core.Message;
import util.vec.Vector;

/**
 * Transmits the state of the mouse buttons.
 * @author TARS
 */
public class MouseButton implements Message {
    
    /**
     * The button to which this message applies.
     */
    public final int button;

    /**
     * The state of the mouse button.
     */
    public final boolean state;

    /**
     * Whether the mouse button changed just now.
     */
    public final boolean changed;

    /**
     * The mouse position at the time of the click;
     */
    public final Vector mousePos;
    
    /**
     * Default constructor.
     * @param b The button.
     * @param s The state.
     * @param c Whether the state just changed.
     * @param mouse The mouse position at the time of the button press.
     */
    public MouseButton(int b, boolean s, boolean c, Vector mouse){
        button = b;
        state = s;
        changed = c;
        mousePos = mouse;
    }
}
