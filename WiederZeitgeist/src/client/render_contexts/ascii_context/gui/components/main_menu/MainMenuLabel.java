/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context.gui.components.main_menu;

import client.gui.components.Label;
import client.render_contexts.ascii_context.DrawIDString;
import graphics.Color;
import util.math.VectorN;

/**
 *
 * @author TARS
 */
public class MainMenuLabel extends Label{

    public final VectorN position;
    public final int maxLength;
    
    public MainMenuLabel(VectorN position, int len, String label) {
        super(label);
        this.position = position;
        maxLength = len;
    }

    @Override
    public void render() {
        DrawIDString.DEFAULT_DRAW.drawTileString(getLabel(), position, Color.WHITE, false, 0, maxLength);
    }
}
