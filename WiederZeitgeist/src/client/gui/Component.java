/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import core.Message;
import util.vec.Vector;

/**
 * An interface for a renderable component which reacts to user input.
 * @author TARS
 */
public interface Component {
    
    /**
     * Determines whether v is contained by the component.
     * @param v The vector to check.
     * @return Whether the vector v is contained by this.
     */
    public boolean contains(Vector v);
    
    /**
     * Gets a message to handle.
     * @param message The message to handle.
     * @return A mask on the handled message. If it returns true, an ordered container will not pass the message onto later components.
     */
    public boolean handleMessage(Message message);
    
    /**
     * Renders the component.
     */
    public void render();
}
