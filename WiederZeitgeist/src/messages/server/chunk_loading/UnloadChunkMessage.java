/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.server.chunk_loading;

import core.Message;
import util.math.IntVectorN;

/**
 * Signals within the world to unload a specific chunk.
 *
 * @author TARS
 */
public class UnloadChunkMessage implements Message {

    /**
     * The position of the chunk to unload.
     */
    public final IntVectorN chunk;

    /**
     * Default constructor.
     *
     * @param c The position of the chunk to unload.
     */
    public UnloadChunkMessage(IntVectorN c) {
        chunk = c;
    }
}
