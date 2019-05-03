/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package start;

import static client.ClientListener.CLIENT_LISTENER;
import client.view.Window;
import static server.ServerListener.SERVER_LISTENER;
import server.world.World;
import util.vec.Vector;

/**
 * A class designed for starting up certain systems of the game.
 * @author TARS
 */
public class StartProcedures {
    
    /**
     * Starts the server and the client listener threads appropriately and safely.
     * @param name The name of the window.
     * @param dim The dimensions of the window.
     */
    public static void startListenerThreads(String name, Vector dim){
        
        SERVER_LISTENER.start(() -> {
            World.initialize();
        }, () -> {
            World.closeCurrentWorld();
            CLIENT_LISTENER.stop();
        });
        
        CLIENT_LISTENER.start(() -> {
            Window.initialize(name, dim);
        }, () -> {
            SERVER_LISTENER.stop();
            SERVER_LISTENER.join();
            Window.cleanupGLFW();
        });
    }
}
