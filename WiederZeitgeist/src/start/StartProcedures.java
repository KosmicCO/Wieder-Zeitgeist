/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package start;

import static client.ClientListener.CLIENT_LISTENER;
import client.view.Window;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.ServerListener.SERVER_LISTENER;
import server.world.World;
import util.vec.Vector;

/**
 * A class designed for starting up certain systems of the game.
 * @author TARS
 */
public class StartProcedures {
    
    private static boolean serverUp = false;
    private static boolean clientUp = false;
    
    /**
     * Starts the server and the client listener threads appropriately and safely.
     * @param name The name of the window.
     * @param dim The dimensions of the window.
     */
    public static void startListenerThreads(String name, Vector dim){
        
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
            Window.initialize(name, dim);
            clientUp = true;
        }, () -> {
            SERVER_LISTENER.stop();
            SERVER_LISTENER.join();
            Window.cleanupGLFW();
        });
        
        while(!(serverUp && clientUp)){
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
