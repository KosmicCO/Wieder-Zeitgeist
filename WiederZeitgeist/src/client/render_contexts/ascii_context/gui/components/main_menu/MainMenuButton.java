/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context.gui.components.main_menu;

import client.gui.components.Button;
import static client.render_contexts.ascii_context.ASCIIRender.drawRect;
import client.render_contexts.ascii_context.Tile;
import graphics.Color;
import util.math.VectorN;

/**
 *
 * @author TARS
 */
public class MainMenuButton extends Button{
    
    
    private VectorN position;
    private VectorN size;
    private Runnable action;
    private MainMenuLabel label;
    
    public MainMenuButton(VectorN position, double length, int initState, String label, Runnable pressAction){
        super(initState);
        this.position = position;
        size = VectorN.of(length, 1);
        action = pressAction;
        this.label = new MainMenuLabel(position, (int) Math.ceil(length), label);
    }

    @Override
    public boolean contains(VectorN v) {
        return size.containsExclusive(v.sub(position));
    }

    @Override
    public void pressed() {
        action.run();
    }

    @Override
    public void render() {
        drawRect(position, size, new Tile(0, Color.WHITE, Color.BLUE.multRGB(getState() * 0.2 + 0.4)));
        label.render();
    }

    @Override
    public void unpressed() {

    }
}
