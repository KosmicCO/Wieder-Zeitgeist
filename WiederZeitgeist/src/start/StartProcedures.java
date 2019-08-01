/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package start;

import static client.ClientListener.CLIENT_LISTENER;
import static java.lang.Thread.sleep;
import static server.ServerListener.SERVER_LISTENER;
import server.world.World;

/**
 * A class designed for starting up certain systems of the game.
 *
 * @author TARS
 */
public class StartProcedures {

    private static boolean serverUp = false;
    private static boolean clientUp = false;

    /**
     * Starts the server and the client listener threads appropriately and
     * safely.
     *
     * @param name The name of the window.
     */
    public static void startListenerThreads(String name) {

        serverUp = false;
        clientUp = false;

        SERVER_LISTENER.start(() -> {
            World.initialize();
            serverUp = true;
        }, () -> {
            World.closeCurrentWorld();
            CLIENT_LISTENER.stop();
        });

        CLIENT_LISTENER.start(() -> {
            clientUp = true;
        }, () -> {
            SERVER_LISTENER.stop();
            SERVER_LISTENER.join();
        });

        while (!(serverUp && clientUp)) {
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
