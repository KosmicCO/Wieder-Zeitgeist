/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator.base_gen_steps;

import server.world.generator.GenStep;

/**
 * The type representing the render step of a chunk being rendered.
 *
 * @author TARS
 */
public class RenderStep implements GenStep {

    /**
     * An instance of the step.
     */
    public static final RenderStep STEP = new RenderStep();
    
    @Override
    public int id() {
        return 0;
    }
}
