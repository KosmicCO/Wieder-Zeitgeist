package game;

import behaviors.FPSBehavior;
import behaviors.QuitOnEscapeBehavior;
import client.gui.GuiManager;
import client.gui.RenderContext;
import static client.render_contexts.SpriteContext.SPRITE_CONTEXT;
import static client.render_contexts.ascii_context.ASCIIContext.ASCII_CONTEXT;
import engine.Core;
import static engine.Layer.RENDER2D;
import engine.Settings;
import graphics.Color;
import java.util.Random;
import messages.client_server.MakeNewWorldLocalMessage;
import static server.ServerListener.SERVER_LISTENER;
import static server.world.BlockDefinition.getBlockIDNoSub;
import server.world.generator.generator_implementations.PerlinHeight;
import start.StartProcedures;
import util.math.Vec3d;

public class Main {

    public static Vec3d position = null;
    private static boolean useSpriteContext = false;
    
    public static void main(String[] args) {
        
        Settings.BACKGROUND_COLOR = new Color(.6, .6, .6, 1);
        Settings.ENABLE_VSYNC = false;
        
        Core.init();
        RenderContext context = useSpriteContext ? SPRITE_CONTEXT :  ASCII_CONTEXT;
        context.create();
        GuiManager.GUI_MANAGER.setCurrentComponent(context);
        new FPSBehavior().create();
        new QuitOnEscapeBehavior().create();
        
        RENDER2D.onStep(() -> {
            GuiManager.GUI_MANAGER.render();
        });
        
        // Starts the window, threads, etc.
        StartProcedures.startListenerThreads("Test World 1");
        
        // Makes a new world with the generator AllBlock
        SERVER_LISTENER.receiveMessage(new MakeNewWorldLocalMessage(new PerlinHeight(
                getBlockIDNoSub("Dirt"), getBlockIDNoSub("Air"), new Random())));
        
        Core.run();
    }

    public static void switchContext(){
        useSpriteContext = !useSpriteContext;
        GuiManager.GUI_MANAGER.getCurrentComponent().destroy();
        RenderContext context = useSpriteContext ? SPRITE_CONTEXT :  ASCII_CONTEXT;
        context.create();
        GuiManager.GUI_MANAGER.setCurrentComponent(context);
    }
}
