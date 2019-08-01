/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.client_server;

import core.Message;
import util.math.IntVectorN;

/**
 * A request from the client to the server for a rendered chunk.
 *
 * @author TARS
 */
public class RequestRenderChunkMessage implements Message {

    /**
     * The position of the chunk to get.
     */
    public final IntVectorN chunk;

    /**
     * Default constructor.
     *
     * @param c The position of the chunk to get.
     */
    public RequestRenderChunkMessage(IntVectorN c) {
        chunk = c;
    }
}
