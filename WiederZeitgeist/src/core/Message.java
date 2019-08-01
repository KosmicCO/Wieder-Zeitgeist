/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.function.Consumer;

/**
 * A type to spread information between Listeners.
 *
 * @author Kosmic
 */
public interface Message {

    /**
     * Checks whether the given message is of the type of MessageType. If so,
     * the handler is run on the given message.
     *
     * @param <M> The type to run the handler on.
     * @param message The given message to check.
     * @param messageType The type to run the handler on.
     * @param handler The handler to be run if the message is of the correct
     * type.
     */
    public static <M extends Message> void onMessageType(Message message, Class<M> messageType, Consumer<M> handler) {
        if (message.getClass().equals(messageType)) {
            handler.accept((M) message);
        }
    }
}
