/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world;

import static client.ClientListener.CLIENT_LISTENER;
import core.Listener;
import core.Message;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import messages.client_server.*;
import messages.server.chunk_loading.*;
import static server.ServerListener.SERVER_LISTENER;
import server.world.generator.GenStep;
import static server.world.generator.GenStep.RENDER;
import server.world.generator.WorldGenerator;
import util.vec.IntVector;

/**
 * The world. It stores the chunks and whatnot associated with the world.
 *
 * @author TARS
 */
public class World {

    private static World currentWorld = null;


    /**
     * Makes sure that the current world saves everything and closes nicely.
     */
    public static void closeCurrentWorld() {
        if (currentWorld == null) {
            return;
        }
        currentWorld.chunkLoader.receiveMessage(new CleanupChunks());
        currentWorld.chunkLoader.join();
        // save the world generator?
        currentWorld = null;
    }

    /**
     * Creates a new world, closing the current world if there is one, and makes
     * it the current world.
     * @param wg The world generator to use.
     */
    public static void createNewWorld(WorldGenerator wg) {
        closeCurrentWorld();
        currentWorld = new World(wg); // make sure the block of type 0 is an actual block
    }
    
    /**
     * Initializes the world.
     */
    public static void initialize(){
        
        // Answers render requests and renders chunks.
        SERVER_LISTENER.addListener(RequestRenderChunk.class, m -> {
            if (currentWorld != null) {
                Chunk c = currentWorld.chunks.get(m.chunk);
                if (c == null || !c.isLoaded() || !c.finishedStep(RENDER)) {
                    currentWorld.chunkLoader.receiveMessage(new LoadChunk(m.chunk, RENDER));
                    SERVER_LISTENER.receiveMessage(m);
                } else {
                    CLIENT_LISTENER.receiveMessage(new ReturnedRenderChunk(m.chunk, null)); // construct it somehow ig
                }
            }
        });
        
        // Answers request to make a new world.
        SERVER_LISTENER.addListener(MakeNewWorldLocal.class, m -> {
            createNewWorld(m.generator);
        });
    }

    private final ChunkIO chunkLoader;
    private final Map<IntVector, Chunk> chunks;
    private final WorldGenerator generator;

    private World(WorldGenerator wg) {
        chunks = new ConcurrentHashMap();
        generator = wg;
        chunkLoader = new ChunkIO();
        chunkLoader.start();
    }

    private Chunk forceGetChunk(IntVector chunkPos) {
        Chunk c = chunks.get(chunkPos);
        if (c == null) {
            // load in the chunk or generate one
            c = generator.createChunk(chunkPos);
            chunks.put(chunkPos, c);
        }
        return c;
    }

    private Collection<Chunk> unloadAllChunks() {
        Collection<Chunk> chunksToUnload = chunks.values();
        chunks.clear();
        return chunksToUnload;
    }

    private Chunk unloadChunk(IntVector chunkPos) {
        Chunk c = chunks.get(chunkPos);
        chunks.remove(chunkPos);
        if (c != null && c.isLoaded()) {
            throw new RuntimeException("Chunk which was to be unloaded was not marked to be unloaded.");
        }
        return c;
    }

    private static class ChunkIO implements Listener {

        private final Queue<Message> messageQueue;
        private volatile Thread ioThread;
        private boolean running;

        public ChunkIO() {
            messageQueue = new ConcurrentLinkedQueue();
            ioThread = null;
            running = false;
        }

        @Override
        public void receiveMessage(Message message) {
            messageQueue.add(message);
        }

        private void handleMessage(Message m) {
            if (LoadChunk.class.isInstance(m)) {
                loadChunk((LoadChunk) m);
            } else if (UnloadChunk.class.isInstance(m)) {
                unloadChunk((UnloadChunk) m);
            } else if (CleanupChunks.class.isInstance(m)) {
                cleanupChunks();
            }
        }

        private void saveChunk(Chunk c) {
            // save it or something
        }

        private void cleanupChunks() {
            Collection<Chunk> toUnload = currentWorld.unloadAllChunks();
            toUnload.forEach(c -> saveChunk(c));
            running = false;
        }

        private void unloadChunk(UnloadChunk ulcm) {
            Chunk c = currentWorld.unloadChunk(ulcm.chunk);
            if (c != null) {
                saveChunk(c);
            }
        }

        private void loadChunk(LoadChunk lcm) {
            Chunk c = currentWorld.forceGetChunk(lcm.chunk);
            if (!c.finishedStep(lcm.step)) {
                for (GenStep gs : currentWorld.generator.getDependencies(lcm.step)) {
                    loadChunk(new LoadChunk(lcm.chunk, gs));
                }
                currentWorld.generator.generateStep(c, lcm.step);
            }
        }

        public void join() {
            Thread iot = ioThread;
            if (iot != null) {
                try {
                    iot.join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Joining thread caused InterruptException.");
                }
            }
        }

        public void start() {
            if (ioThread != null) {
                return;
            }

            running = true;
            ioThread = new Thread(() -> {
                while (running) {
                    if (!messageQueue.isEmpty()) {
                        Message m = messageQueue.remove();
                        if (m != null) {
                            handleMessage(m);
                        }
                    }
                }
                ioThread = null;
            });
            ioThread.start();
        }

        public void stop() {
            running = false;
        }
    }
}