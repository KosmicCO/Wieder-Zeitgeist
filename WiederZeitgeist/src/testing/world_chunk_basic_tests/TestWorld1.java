/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing.world_chunk_basic_tests;

import static client.ClientListener.CLIENT_LISTENER;
import java.util.Random;
import messages.client.view.KeyPress;
import messages.client.view.WindowShouldClose;
import messages.client_server.MakeNewWorldLocalMessage;
import messages.client_server.RequestRenderChunkMessage;
import messages.client_server.ReturnedRenderChunkMessage;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static server.ServerListener.SERVER_LISTENER;
import server.world.BlockDefinition;
import static server.world.BlockDefinition.getBlockID;
import server.world.generator.generator_implementations.PerlinHeight;
import start.StartProcedures;
import util.math.IntVectorN;
import util.math.VectorN;

/**
 * Tests the basic functionality of the world and chunk interactions.
 *
 * @author TARS
 */
public class TestWorld1 {

    private static int left;

    /**
     * Test the implementation of the chunk and world dynamic loading system.
     * Also used to test the block implementation.
     *
     * @param args No arguments are taken.
     */
    public static void main(String[] args) {

        left = 1;

        int id1 = CLIENT_LISTENER.addListener(ReturnedRenderChunkMessage.class, m -> {
            System.out.println(m.position);
        });

        CLIENT_LISTENER.addListener(WindowShouldClose.class, m -> {
            CLIENT_LISTENER.stop();
        });

        int id2 = CLIENT_LISTENER.addListener(KeyPress.class, m -> {

            if (m.changed && m.state) {
                switch (m.key) {

                    case GLFW_KEY_C:

                        for (int i = 0; i < 100; i++) {
                            SERVER_LISTENER.receiveMessage(new RequestRenderChunkMessage(IntVectorN.of(left + i, 0))); // creates more chunks to test memory usage
                        }

                        left += 100;
                        break;
                    case GLFW_KEY_B:
                        System.out.println(BlockDefinition.getBlock((short) 3).name);
                        System.out.println(BlockDefinition.getBlock("Sand").id);
                }
            }
        }
        );
        
        CLIENT_LISTENER.removeListener(id2);

        StartProcedures.startListenerThreads("Test World 1");
        SERVER_LISTENER.receiveMessage(new MakeNewWorldLocalMessage(new PerlinHeight(getBlockID("Sand", (short) 0), getBlockID("Air", (short) 0), new Random())));
        SERVER_LISTENER.receiveMessage(new RequestRenderChunkMessage(IntVectorN.of(0, 0)));
    }
}
