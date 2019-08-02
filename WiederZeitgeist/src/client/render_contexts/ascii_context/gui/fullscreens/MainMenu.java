/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context.gui.fullscreens;

import static client.ClientListener.CLIENT_LISTENER;
import client.gui.Component;
import client.gui.Container;
import client.gui.components.Button;
import client.render_contexts.ascii_context.ASCIIContext;
import client.render_contexts.ascii_context.gui.components.main_menu.MainMenuButton;
import core.Message;
import messages.client.view.WindowShouldClose;
import util.math.VectorN;

/**
 * The main menu for the Ascii context.
 * @author TARS
 */
public class MainMenu implements Component{

    private final ASCIIContext context;
    private Component pushedComp;
    
    private final Container mainScreen = new Container();
    
    /**
     * Creates a new main menu for the ascii context.
     * @param parent The caller ASCIIContext instance.
     */
    public MainMenu(ASCIIContext parent){
        context = parent;
        
        mainScreen.addTop(new MainMenuButton(VectorN.of(0, 1), 15, Button.UNPRESSED, "Start", () -> {System.out.println("hi");}));
        mainScreen.addTop(new MainMenuButton(VectorN.of(0, 0), 15, Button.UNPRESSED, "Quit", () -> {
            CLIENT_LISTENER.receiveMessage(new WindowShouldClose());
        }));
    }
    
    @Override
    public boolean contains(VectorN v) {
        return true;
    }

    @Override
    public boolean handleMessage(Message message) {
        if(pushedComp != null){
            pushedComp.handleMessage(message);
        }else{
            mainScreen.handleMessage(message);
        }
        return true;
    }

    @Override
    public void render() {
        mainScreen.render();
        if(pushedComp != null){
            pushedComp.render();
        }
    }
}
