/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import core.Listener;
import core.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The main threaded singleton listener for the server section of the game.
 *
 * @author TARS
 */
public class ServerListener implements Listener {

    /**
     * The threaded listener which acts as a head for all the messages going to
     * the client section from elsewhere.
     */
    public static final ServerListener SERVER_LISTENER = new ServerListener();
    private static int nextID = 0;
    private final Queue<Message> messageQueue;
    private final Map<Integer, MessageListenerPair> receiverID;
    private final Map<Class<? extends Message>, List<Listener>> receivers;
    private boolean running;
    private volatile Thread serverThread;

    private ServerListener() {
        receivers = new ConcurrentHashMap();
        receiverID = new ConcurrentHashMap();
        messageQueue = new ConcurrentLinkedQueue();
        running = false;
    }

    /**
     * Adds a listener to be alerted when a message that it subscribed to was
     * received.
     *
     * @param <M> The message type to subscribe to.
     * @param messageType The message type to subscribe to.
     * @param listener A listener which specifically takes the message type M.
     * @return The id of the listener, which can be used to remove it.
     */
    public synchronized <M extends Message> int addListener(Class<M> messageType, Listener<M> listener) {
        if (receivers.containsKey(messageType)) {
            if (!receivers.get(messageType).contains(listener)) {
                receivers.get(messageType).add(listener);
            }
        } else {
            List listenerList = new ArrayList();
            listenerList.add(listener);
            receivers.put(messageType, listenerList);
        }
        int id = nextID;
        nextID++;
        receiverID.put(id, new MessageListenerPair(messageType, listener));
        return id;
    }

    /**
     * Joins with the server thread.
     */
    public void join() {
        Thread st = serverThread;
        if (st != null) {
            try {
                st.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException("Joining thread caused InterruptException.");
            }
        }
    }

    @Override
    public synchronized void receiveMessage(Message message) {
        if (!receivers.containsKey(message.getClass())) {
            receivers.put(message.getClass(), new ArrayList());
        }
        messageQueue.add(message);
    }

    /**
     * Removes the listener referred to by the id.
     *
     * @param id The id of the listener.
     */
    public synchronized void removeListener(int id) {
        MessageListenerPair entry = receiverID.get(id);
        if (entry == null) {
            throw new IllegalArgumentException("Given id does not match a listener: " + id);
        }

        List<Listener> list = receivers.get(entry.mType);
        if (list == null) {
            throw new IllegalArgumentException("Given id does not match a listener: " + id);
        }
        list.remove(entry.lstn);
        if (list.isEmpty()) {
            receivers.remove(entry.mType);
        }
        receiverID.remove(id);
    }

    /**
     * Starts the SERVER_LISTENER thread.
     *
     * @param init The initial code to be run.
     * @param close The final code to be run when stop() is called.
     */
    public void start(Runnable init, Runnable close) {
        if (serverThread != null) {
            return;
        }
        running = true;
        serverThread = new Thread(() -> {
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
            serverThread = null;
        });
        serverThread.start();
    }

    /**
     * Signals the SERVER_LISTENER thread to close.
     */
    public void stop() {
        running = false;
    }

    private static class MessageListenerPair<M extends Message> {

        public Class<M> mType;
        public Listener<M> lstn;

        public MessageListenerPair(Class<M> message, Listener<M> listener) {
            mType = message;
            lstn = listener;
        }
    }
}
