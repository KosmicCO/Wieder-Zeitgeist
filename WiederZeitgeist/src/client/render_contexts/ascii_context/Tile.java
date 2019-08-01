/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context;

import graphics.Color;

/**
 *
 * @author TARS
 */
public class Tile {
    
    public final int id;
    public final Color fore;
    public final Color back;
    
    public Tile(int id, Color fore, Color back){
        this.id = id;
        this.fore = fore;
        this.back = back;
    }
}
