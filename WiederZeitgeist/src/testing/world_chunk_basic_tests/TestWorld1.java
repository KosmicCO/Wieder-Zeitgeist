/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing.world_chunk_basic_tests;

import static client.ClientListener.CLIENT_LISTENER;
import messages.client.view.KeyPress;
import messages.client.view.WindowShouldClose;
import messages.client_server.MakeNewWorldLocal;
import messages.client_server.RequestRenderChunk;
import messages.client_server.ReturnedRenderChunk;
import static server.ServerListener.SERVER_LISTENER;
import server.world.generator.generator_implementations.AllBlock;
import start.StartProcedures;
import util.vec.IntVector;
import util.vec.Vector;

/**
 * Tests the basic functionality of the world and chunk interactions.
 *
 * @author TARS
 */
public class TestWorld1 {

    private static int left;

    /**
     * Test the implementation of the chunk and world dynamic loading system.
     *
     * @param args No arguments are taken.
     */
    public static void main(String[] args) {

        left = 1;

        CLIENT_LISTENER.addListener(ReturnedRenderChunk.class, m -> {
            System.out.println(m.position);
        });

        CLIENT_LISTENER.addListener(WindowShouldClose.class, m -> {
            CLIENT_LISTENER.stop();
        });

        CLIENT_LISTENER.addListener(KeyPress.class, m -> {

            for (int i = 0; i < 100; i++) {
                SERVER_LISTENER.receiveMessage(new RequestRenderChunk(new IntVector(left + i, 0))); // creates more chunks to test memory usage
            }
            
            left += 100;
        });

        StartProcedures.startListenerThreads("Test World 1", new Vector(400, 400));
        SERVER_LISTENER.receiveMessage(new MakeNewWorldLocal(new AllBlock(0)));
        SERVER_LISTENER.receiveMessage(new RequestRenderChunk(new IntVector(0, 0)));

    }
}
