/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import static client.ClientListener.CLIENT_LISTENER;
import core.Message;
import engine.Input;
import java.util.Set;
import java.util.TreeSet;
import messages.client.view.KeyPress;
import messages.client.view.MouseButton;
import messages.client.view.MousePosition;
import messages.client.view.MouseWheel;

/**
 * The singleton head for passing and managing the gui structure.
 *
 * @author TARS
 */
public class GuiManager {

    /**
     * The main instance of GuiManager
     */
    public static final GuiManager GUI_MANAGER = new GuiManager();

    private RenderContext currentComponent = null;
    private final Set<Class<? extends Message>> typesToPass;

    private GuiManager() {
        typesToPass = new TreeSet();
        Input.addListener((iType, mouse, deltaMouse, key, pressed, changed) -> {
            if (currentComponent != null) {
                switch (iType) {
                    case Input.MOUSE_IN:
                        currentComponent.handleMessage(new MousePosition(mouse.toVectorN(), deltaMouse.toVectorN()));
                        break;
                    case Input.KEY_IN:
                        currentComponent.handleMessage(new KeyPress(key, pressed, changed));
                        break;
                    case Input.MOUSE_BUTTON_IN:
                        currentComponent.handleMessage(new MouseButton(key, pressed, changed, mouse.toVectorN()));
                        break;
                    case Input.MOUSE_WHEEL_IN:
                        currentComponent.handleMessage(new MouseWheel(mouse.toVectorN(), deltaMouse.toVectorN()));
                        break;
                }
            }
        });
    }

    /**
     * Returns the current component of the manager.
     *
     * @return The current component.
     */
    public RenderContext getCurrentComponent() {
        return currentComponent;
    }

    /**
     * Adds the message type to the list of types to be passed on to components.
     *
     * @param <M> The message type;
     * @param messageType The message type;
     */
    public <M extends Message> void passMessageType(Class<M> messageType) {
        if (!typesToPass.contains(messageType)) {
            CLIENT_LISTENER.addListener(messageType, m -> {
                if (currentComponent != null) {
                    currentComponent.handleMessage(m);
                }
            });
            typesToPass.add(messageType);
        }
    }

    /**
     * Renders the manager's component.
     */
    public void render() {
        if (currentComponent != null) {
            currentComponent.render();
        }
    }

    /**
     * Sets the current component of the manager.
     *
     * @param comp the component to set as the current.
     */
    public void setCurrentComponent(RenderContext comp) {
        currentComponent = comp;
    }
}
