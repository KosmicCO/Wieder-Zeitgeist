/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.client.view;

import core.Message;
import util.math.VectorN;

/**
 * Transmits the state of the mouse wheel.
 *
 * @author TARS
 */
public class MouseWheel implements Message {

    /**
     * The current wheel offset.
     */
    public final VectorN wheelOffset;

    /**
     * The delta vector of the offset.
     */
    public final VectorN wheelDelta;

    /**
     * Default constructor.
     *
     * @param wheel The current wheel offset.
     * @param delta The delta of the wheel offset.
     */
    public MouseWheel(VectorN wheel, VectorN delta) {
        wheelOffset = wheel;
        wheelDelta = delta;
    }
}
