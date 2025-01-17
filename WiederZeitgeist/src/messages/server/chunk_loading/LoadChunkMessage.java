/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.server.chunk_loading;

import core.Message;
import server.world.generator.GenStep;
import util.math.IntVectorN;

/**
 * Signals within world to load in a chunk to a certain step;
 *
 * @author TARS
 */
public class LoadChunkMessage implements Message {

    /**
     * The vector representing the chunk.
     */
    public final IntVectorN chunk;

    /**
     * The step to generate the chunk to.
     */
    public final GenStep step;

    /**
     * Default constructor.
     *
     * @param c The chunk position to load in.
     * @param gs The step to load the chunk to.
     */
    public LoadChunkMessage(IntVectorN c, GenStep gs) {
        chunk = c;
        step = gs;
    }
}
