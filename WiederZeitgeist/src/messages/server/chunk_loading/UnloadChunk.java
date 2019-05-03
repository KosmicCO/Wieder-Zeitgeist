/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.server.chunk_loading;

import core.Message;
import util.vec.IntVector;

/**
 * Signals within the world to unload a specific chunk.
 * @author TARS
 */
public class UnloadChunk implements Message {
    
    /**
     * The position of the chunk to unload.
     */
    public final IntVector chunk;
    
    /**
     * Default constructor.
     * @param c The position of the chunk to unload.
     */
    public UnloadChunk(IntVector c){
        chunk = c;
    }
}
