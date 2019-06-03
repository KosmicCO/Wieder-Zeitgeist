package game;

import behaviors.FPSBehavior;
import behaviors.QuitOnEscapeBehavior;
import static client.ClientListener.CLIENT_LISTENER;
import client.gui.RenderedChunk;
import engine.Core;
import engine.Input;
import static engine.Layer.RENDER2D;
import engine.Settings;
import graphics.Camera;
import graphics.Color;
import graphics.sprites.Sprite;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import messages.client.view.WindowShouldClose;
import messages.client_server.MakeNewWorldLocalMessage;
import messages.client_server.RequestRenderChunkMessage;
import messages.client_server.ReturnedRenderChunkMessage;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static server.ServerListener.SERVER_LISTENER;
import static server.world.BlockDefinition.getBlockIDNoSub;
import server.world.Chunk;
import server.world.generator.generator_implementations.PerlinHeight;
import start.StartProcedures;
import static util.math.MathUtils.clamp;
import static util.math.MathUtils.floor;
import static util.math.MathUtils.mod;
import util.math.Transformation;
import util.math.Vec2d;
import util.vec.IntVector;
import util.vec.Vector;

public class Main {

    private static Set<IntVector> requestedChunks = new HashSet();
    private static Map<IntVector, RenderedChunk> loadedChunks = Collections.synchronizedMap(new HashMap());

    public static void main(String[] args) {
        Settings.BACKGROUND_COLOR = new Color(.6, .6, .6, 1);
        Core.init();

        new FPSBehavior().create();
        new QuitOnEscapeBehavior().create();

        // receives message
        CLIENT_LISTENER.addListener(ReturnedRenderChunkMessage.class, m -> {
            System.out.println(m.position);
            loadedChunks.put(m.position, m.renderChunk);
        });
        // makes the X button stop the program
        CLIENT_LISTENER.addListener(WindowShouldClose.class, m -> {
            CLIENT_LISTENER.stop();
        });
        // Starts the window, threads, etc.
        StartProcedures.startListenerThreads("Test World 1", new Vector(400, 400));
        // Makes a new world with the generator AllBlock
        SERVER_LISTENER.receiveMessage(new MakeNewWorldLocalMessage(new PerlinHeight(
                getBlockIDNoSub("Dirt"), getBlockIDNoSub("Air"), new Random())));

        RENDER2D.onStep(() -> {
            cameraControls();
            Camera.camera2d.setCenterSize(viewPos, viewSize.mul(Math.pow(2, -.5 * viewZoom)));
            for (int x = floor(Camera.camera2d.lowerLeft.x); x < Camera.camera2d.upperRight.x; x++) {
                for (int y = floor(Camera.camera2d.lowerLeft.y); y < Camera.camera2d.upperRight.y; y++) {
                    // draw block here
                    IntVector chunkPos = new IntVector(floor((double) x / Chunk.SIZE), floor((double) y / Chunk.SIZE));
                    if (!requestedChunks.contains(chunkPos)) {
                        requestedChunks.add(chunkPos);
                        SERVER_LISTENER.receiveMessage(new RequestRenderChunkMessage(chunkPos));
                    }
                    if (loadedChunks.containsKey(chunkPos)) {
                        RenderedChunk r = loadedChunks.get(chunkPos);
                        if (r.getRenBlock(mod(x, Chunk.SIZE), mod(y, Chunk.SIZE), viewHeight, false).id == getBlockIDNoSub("Air")) {
                            int blockMin = r.getMaxBlockHeight(mod(x, Chunk.SIZE), mod(y, Chunk.SIZE), -1000, false);
                            double distToBlock = Math.max(0, (viewHeight - blockMin) * .05);
                            Sprite.load("grass.png").draw(Transformation.create(new Vec2d(x, y), 0, 1), new Color(1, 1, 1, Math.exp(-distToBlock)));
                        } else {
                            Sprite.load("grass.png").draw(Transformation.create(new Vec2d(x, y), 0, 1), new Color(.5, .5, .5, 1));
                        }
                    }
                }
            }
        });

        Core.run();
    }

    public static Vec2d viewPos = new Vec2d(0, 0);
    public static double viewZoom = 0;
    public static Vec2d viewSize = new Vec2d(16, 9);
    public static int viewHeight = 30;

    public static void cameraControls() {
        double dx = 0, dy = 0;
        if (Input.keyDown(GLFW_KEY_W)) {
            dy += 1;
        }
        if (Input.keyDown(GLFW_KEY_A)) {
            dx -= 1;
        }
        if (Input.keyDown(GLFW_KEY_S)) {
            dy -= 1;
        }
        if (Input.keyDown(GLFW_KEY_D)) {
            dx += 1;
        }
        viewPos = viewPos.add(new Vec2d(dx, dy).mul(5 * Math.pow(2, -.5 * viewZoom) * Core.dt()));

        viewHeight += Input.mouseWheel();
        viewHeight = clamp(viewHeight, 0, 50);
        viewZoom = clamp(viewZoom, -6, -6);
    }
}
