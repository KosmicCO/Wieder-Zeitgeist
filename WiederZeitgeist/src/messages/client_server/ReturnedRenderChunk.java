/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.client_server;

import client.view.RenderChunk;
import core.Message;
import util.vec.IntVector;

/**
 * A message from the server to the client with the RenderChunk that the client had requested.
 * @author TARS
 */
public class ReturnedRenderChunk implements Message{
    
    /**
     * The position of the chunk.
     */
    public final IntVector position;

    /**
     * The RenderChunk data for rendering the chunk.
     */
    public final RenderChunk renderChunk;
    
    /**
     * Default constructor.
     * @param cp The position of the chunk.
     * @param rc The RenderChunk data.
     */
    public ReturnedRenderChunk(IntVector cp, RenderChunk rc){
        position = cp;
        renderChunk = rc;
    }
}
