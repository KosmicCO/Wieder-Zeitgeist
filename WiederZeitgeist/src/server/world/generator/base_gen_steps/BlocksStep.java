/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator.base_gen_steps;

import server.world.generator.GenStep;

/**
 * The generation step representing the block storage system if a chunk having
 * been generated.
 *
 * @author TARS
 */
public class BlocksStep implements GenStep {

    /**
     * An instance of the step.
     */
    public static final BlocksStep STEP = new BlocksStep();
    
    @Override
    public int id() {
        return 1;
    }

}
