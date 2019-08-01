/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context;

import static client.ClientListener.CLIENT_LISTENER;
import client.gui.GuiManager;
import client.gui.RenderContext;
import client.gui.RenderedChunk;
import static client.render_contexts.SpriteContext.viewHeight;
import core.Message;
import game.Main;
import graphics.Color;
import graphics.Window;
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
import static server.ServerListener.SERVER_LISTENER;
import static server.world.BlockDefinition.getBlockID;
import server.world.Chunk;
import util.math.IntVectorN;
import static util.math.MathUtils.mod;
import util.math.Vec2d;
import util.math.Vec3d;
import util.math.VectorN;

/**
 *
 * @author TARS
 */
public class ASCIIContext implements RenderContext {

    public static final ASCIIContext ASCII_CONTEXT = new ASCIIContext();
    
    private Set<IntVectorN> requestedChunks = new HashSet();
    private Map<IntVectorN, RenderedChunk> loadedChunks = Collections.synchronizedMap(new HashMap());
    private List<Integer> listenerIDs = new ArrayList();

    private ASCIIContext(){
        
    }
    
    @Override
    public boolean contains(VectorN v) {
        return true;
    }

    @Override
    public void create() {

        Window.window.resizeWindow(100, 180);
        Main.position = new Vec3d(0, 0, 25);

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

    private static final Vec2d tileDim = new Vec2d(10, 18);

    @Override
    public void render() {

        ASCIIRender.startRender("Courier-12");

        if (Main.position != null) {

            IntVectorN pos3d = Main.position.toVectorN().floor();
            IntVectorN lowerLeft = IntVectorN.of(pos3d.x(), pos3d.y()).sub(ASCIIRender.getRenderDim().mult(0.5).floor());

            for (int x = 0; x < ASCIIRender.getRenderDim().x(); x++) {
                for (int y = 0; y < ASCIIRender.getRenderDim().y(); y++) {
                    VectorN pos = VectorN.of(x, y).add(lowerLeft.toVectorN());
                    IntVectorN chunkPos = pos.div(Chunk.SIZE).floor();
                    if (!requestedChunks.contains(chunkPos)) {
                        requestedChunks.add(chunkPos);
                        SERVER_LISTENER.receiveMessage(new RequestRenderChunkMessage(chunkPos));
                    }
                    if (loadedChunks.containsKey(chunkPos)) {
                       renderTile(mod((int) Math.floor(pos.x()), Chunk.SIZE), mod((int) Math.floor(pos.y()), Chunk.SIZE), x, y, pos3d.z(), loadedChunks.get(chunkPos));
                    }
                }
            }
        }

        ASCIIRender.finishRender("Courier-12");
    }
    
    private void renderTile(int x, int y, int trueX, int trueY, int z, RenderedChunk r){
        if (r.getRenBlock(x, y, z, false).id == getBlockID("Air")) {
            int blockMin = r.getMaxBlockHeight(mod(x, Chunk.SIZE), mod(y, Chunk.SIZE), -1000, false);
            double distToBlock = Math.max(0, (viewHeight - blockMin) * .05);
            ASCIIRender.drawTile(trueX, trueY, new Tile('j', Color.GREEN, Color.GRAY.alphaMix(Color.GREEN.setA(Math.exp(-distToBlock)))));
        } else {
            ASCIIRender.drawTile(trueX, trueY, new Tile('A', Color.GREEN, Color.GREEN.multRGB(0.5)));
        }
    }
}
