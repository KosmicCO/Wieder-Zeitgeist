/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

/**
 * Creates the context for a render system to be added as the single component
 * to the GuiManager to make very different rendering algorithms
 * interchangeable.
 *
 * @author TARS
 */
public interface RenderContext extends Component {
    
    /**
     * Creates the necessary context for the renderer.
     */
    public void create();

    /**
     * Destructs the context for the renderer to make way for the next context.
     */
    public void destroy();
}
