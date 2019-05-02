/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.client.view;

import core.Message;
import util.vec.Vector;

/**
 * Transmits the new mouse position and the distance traveled.
 * @author TARS
 */
public class MousePosition implements Message {
    
    /**
     * New position of the mouse.
     */
    public final Vector position;

    /**
     * Vector representing change from the old mouse position.
     */
    public final Vector delta;
    
    /**
     * Default constructor.
     * @param pos The new position of the mouse.
     * @param d The delta vector from the old mouse position.
     */
    public MousePosition(Vector pos, Vector d){
        position = pos;
        delta = d;
    }
}