/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import core.Listener;
import core.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The main threaded singleton listener for the client section of the game.
 * @author Kosmic
 */
public class ClientListener implements Listener {

    /**
     * The threaded listener which acts as a head for all the messages going to the client section from elsewhere.
     */
    public static final ClientListener CLIENT_LISTENER = new ClientListener();
    
    private Thread clientThread;
    private final Queue<Message> messageQueue;
    private final Map<Class<? extends Message>, List<Listener>> receivers;
    private boolean running;

    private ClientListener() {
        receivers = new HashMap();
        messageQueue = new ConcurrentLinkedQueue();
        running = false;
    }
    
    /**
     * Adds a listener to be alerted when a message that it subscribed to was received.
     * @param <M> The message type to subscribe to.
     * @param messageType The message type to subscribe to.
     * @param listener A listener which specifically takes the message type M.
     */
    public synchronized <M extends Message> void addListener(Class<M> messageType, Listener<M> listener) {
        if (receivers.containsKey(messageType)) {
            receivers.get(messageType).add(listener);
        } else {
            List listenerList = new ArrayList();
            listenerList.add(listener);
            receivers.put(messageType, listenerList);
        }
    }
    
    @Override
    public synchronized void receiveMessage(Message message) {
        if(!receivers.containsKey(message.getClass())){
            receivers.put(message.getClass(), new ArrayList());
        }
        messageQueue.add(message);
    }

    /**
     * Starts the CLIENT_LISTENER thread.
     * @param init The initial code to be run.
     * @param close The final code to be run when stop() is called.
     */
    public void start(Runnable init, Runnable close) {
        if (clientThread != null) {
            return;
        }
        running = true;
        clientThread = new Thread(() -> {
            init.run();
            while (running) {
                if (!messageQueue.isEmpty()) {
                    Message m = messageQueue.remove();
                    if (m != null) {
                        receivers.get(m.getClass()).forEach(l -> l.receiveMessage(m));
                    }
                }
            }
            close.run();
            clientThread = null;
        });
        clientThread.start();
    }

    /**
     * Signals the CLIENT_LISTENER thread to close.
     */
    public void stop() {
        running = false;
    }
}
