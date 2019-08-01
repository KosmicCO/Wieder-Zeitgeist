/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts;

import static client.ClientListener.CLIENT_LISTENER;
import client.gui.RenderContext;
import client.gui.RenderedChunk;
import core.Message;
import engine.Core;
import engine.Input;
import engine.Settings;
import graphics.Camera;
import graphics.Color;
import graphics.sprites.Sprite;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import messages.client.view.WindowShouldClose;
import messages.client_server.RequestRenderChunkMessage;
import messages.client_server.ReturnedRenderChunkMessage;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static server.ServerListener.SERVER_LISTENER;
import static server.world.BlockDefinition.getBlockID;
import server.world.Chunk;
import util.math.IntVectorN;
import static util.math.MathUtils.clamp;
import static util.math.MathUtils.mod;
import util.math.Transformation;
import util.math.Vec2d;
import util.math.VectorN;



/**
 *
 * @author TARS
 */
public class SpriteContext implements RenderContext{
    
    public static final SpriteContext SPRITE_CONTEXT = new SpriteContext();
    
    private Set<IntVectorN> requestedChunks = new HashSet();
    private Map<IntVectorN, RenderedChunk> loadedChunks = Collections.synchronizedMap(new HashMap());
    private List<Integer> listenerIDs = new ArrayList();

    private SpriteContext(){
        
    }
    
    @Override
    public boolean contains(VectorN v) {
        return true;
    }

    @Override
    public void create() {
        
        // receives message
        listenerIDs.add(CLIENT_LISTENER.addListener(ReturnedRenderChunkMessage.class, m -> {
            System.out.println(m.position);
            loadedChunks.put(m.position, m.renderChunk);
        }));
        
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
        return true;
    }

    @Override
    public void render() {
        cameraControls();
        viewSize = new Vec2d(1, ((double) Settings.WINDOW_HEIGHT) / Settings.WINDOW_WIDTH);
            Camera.camera2d.setCenterSize(viewPos, viewSize.mul(Math.pow(2, -.5 * viewZoom)));
            for (int x = (int) floor(Camera.camera2d.lowerLeft.x); x < Camera.camera2d.upperRight.x; x++) {
                for (int y = (int) floor(Camera.camera2d.lowerLeft.y); y < Camera.camera2d.upperRight.y; y++) {
                    // draw block here
                    IntVectorN chunkPos = VectorN.of(x, y).div(Chunk.SIZE).floor();
                    if (!requestedChunks.contains(chunkPos)) {
                        requestedChunks.add(chunkPos);
                        SERVER_LISTENER.receiveMessage(new RequestRenderChunkMessage(chunkPos));
                    }
                    if (loadedChunks.containsKey(chunkPos)) {
                        RenderedChunk r = loadedChunks.get(chunkPos);
                        if (r.getRenBlock(mod(x, Chunk.SIZE), mod(y, Chunk.SIZE), viewHeight, false).id == getBlockID("Air")) {
                            int blockMin = r.getMaxBlockHeight(mod(x, Chunk.SIZE), mod(y, Chunk.SIZE), -1000, false);
                            double distToBlock = Math.max(0, (viewHeight - blockMin) * .05);
                            Sprite.load("grass.png").draw(Transformation.create(new Vec2d(x, y), 0, 1), new Color(1, 1, 1, Math.exp(-distToBlock)));
                        } else {
                            Sprite.load("grass.png").draw(Transformation.create(new Vec2d(x, y), 0, 1), new Color(.5, .5, .5, 1));
                        }
                    }
                }
            }
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
