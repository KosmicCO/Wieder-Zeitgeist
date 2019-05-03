/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import core.Message;
import java.util.ArrayList;
import java.util.List;
import util.vec.Vector;

/**
 * A component which holds other components in an order.
 *
 * @author TARS
 */
public class Container implements Component {

    private final List<Component> components;

    /**
     * Constructs a component container with no elements.
     */
    public Container() {
        components = new ArrayList();
    }

    /**
     * Adds the component to render and receive messages first.
     *
     * @param comp The component to add to top.
     */
    public void addTop(Component comp) {
        components.add(comp);
    }

    @Override
    public boolean contains(Vector v) {
        return components.stream().anyMatch(c -> c.contains(v));
    }

    @Override
    public boolean handleMessage(Message message) {
        boolean stop = false;
        for (int i = components.size() - 1; i >= 0; i--) {
            stop = components.get(i).handleMessage(message);
            if (stop) {
                break;
            }
        }
        return stop;
    }

    /**
     * Removes the given component from the container.
     *
     * @param comp The component to remove from the container.
     */
    public void remove(Component comp) {
        components.remove(comp);
    }

    @Override
    public void render() {
        components.forEach(Component::render);
    }
}
