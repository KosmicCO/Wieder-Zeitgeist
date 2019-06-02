/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.client_server;

import core.Message;
import server.world.generator.WorldGenerator;

/**
 * Locally signals World to make a new world. Only works if the message is sent
 * locally.
 *
 * @author TARS
 */
public class MakeNewWorldLocalMessage implements Message{

    /**
     * The world generator for the world to use.
     */
    public final WorldGenerator generator;
    
    /**
     * Default constructor.
     * @param wg The world generator to use.
     */
    public MakeNewWorldLocalMessage(WorldGenerator wg){
        generator = wg;
    }
}
