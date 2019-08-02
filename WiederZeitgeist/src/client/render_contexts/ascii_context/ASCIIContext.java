/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context;

import static client.ClientListener.CLIENT_LISTENER;
import client.gui.Component;
import client.gui.RenderContext;
import static client.render_contexts.ascii_context.ASCIIRender.fill;
import client.render_contexts.ascii_context.gui.fullscreens.MainMenu;
import core.Message;
import engine.Settings;
import game.Main;
import graphics.Color;
import graphics.Window;
import java.util.ArrayList;
import java.util.List;
import messages.client.view.MouseButton;
import messages.client.view.MousePosition;
import messages.client.view.WindowShouldClose;
import util.math.Vec2d;
import util.math.Vec3d;
import util.math.VectorN;

/**
 *
 * @author TARS
 */
public class ASCIIContext implements RenderContext {

    public static final ASCIIContext ASCII_CONTEXT = new ASCIIContext();

    private final List<Integer> listenerIDs = new ArrayList();

    private Component fullScreen;
    private String currentSpriteSheet = "Courier-12";
    private VectorN tileDim = ASCIISpriteSheet.dim(currentSpriteSheet).toVectorN();

    private ASCIIContext() {
        fullScreen = new MainMenu(this);//new GameRenderPanel();
    }

    @Override
    public boolean contains(VectorN v) {
        return true;
    }

    @Override
    public void create() {

        Window.window.resizeWindow(600, 360);
        Settings.MIN_WINDOW_WIDTH = 600;
        Settings.MIN_WINDOW_HEIGHT = 360;
        Settings.WINDOW_WIDTH_DIVISOR = (int) tileDim.x();
        Settings.WINDOW_HEIGHT_DIVISOR = (int) tileDim.y();
        Settings.CLOSE_ON_X = false;

        Main.position = new Vec3d(0, 0, 25);

        // makes the X button stop the program
        listenerIDs.add(CLIENT_LISTENER.addListener(WindowShouldClose.class, m -> {
            CLIENT_LISTENER.stop();
        }));
    }

    @Override
    public void destroy() {
        listenerIDs.forEach(id -> CLIENT_LISTENER.removeListener(id));
        listenerIDs.clear();
    }

    @Override
    public boolean handleMessage(Message message) {

        boolean handled = Message.onMessageType(message, MousePosition.class, mp -> {
            fullScreen.handleMessage(new MousePosition(scaleMouseInputs(mp.position), scaleMouseInputs(mp.delta)));
        });

        handled |= Message.onMessageType(message, MouseButton.class, mb -> {
            fullScreen.handleMessage(new MouseButton(mb.button, mb.state, mb.changed, scaleMouseInputs(mb.mousePos)));
        });

        if (!handled) {
            fullScreen.handleMessage(message);
        }

//        Message.onMessageType(message, KeyPress.class, kp -> {
//            switch(kp.key){
//                case GLFW.GLFW_KEY_W:
//                    Main.position = Main.position.add(new Vec3d(0, 1, 0));
//                    break;
//                case GLFW.GLFW_KEY_S:
//                    Main.position = Main.position.add(new Vec3d(0, -1, 0));
//                    break;
//                case GLFW.GLFW_KEY_A:
//                    Main.position = Main.position.add(new Vec3d(-1, 0, 0));
//                    break;
//                case GLFW.GLFW_KEY_D:
//                    Main.position = Main.position.add(new Vec3d(1, 0, 0));
//                    break;
//            }
//        });
        return true;
    }

    @Override
    public void render() {
        ASCIIRender.startRender(currentSpriteSheet);
        fill(new Tile(0, Color.WHITE, Color.BLACK));
        fullScreen.render();
        ASCIIRender.finishRender(currentSpriteSheet);
    }

    private VectorN scaleMouseInputs(VectorN in) {
        return in.bimap((i, s) -> i * s, VectorN.of(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT).bimap((wd, td) -> wd / td, tileDim));
    }
}
