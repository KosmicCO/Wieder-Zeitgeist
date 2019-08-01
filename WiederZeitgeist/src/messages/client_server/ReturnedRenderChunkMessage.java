/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.client_server;

import client.gui.RenderedChunk;
import core.Message;
import util.math.IntVectorN;

/**
 * A message from the server to the client with the RenderChunk that the client
 * had requested.
 *
 * @author TARS
 */
public class ReturnedRenderChunkMessage implements Message {

    /**
     * The position of the chunk.
     */
    public final IntVectorN position;

    /**
     * The RenderChunk data for rendering the chunk.
     */
    public final RenderedChunk renderChunk;

    /**
     * Default constructor.
     *
     * @param cp The position of the chunk.
     * @param rc The RenderChunk data.
     */
    public ReturnedRenderChunkMessage(IntVectorN cp, RenderedChunk rc) {
        position = cp;
        renderChunk = rc;
    }
}
