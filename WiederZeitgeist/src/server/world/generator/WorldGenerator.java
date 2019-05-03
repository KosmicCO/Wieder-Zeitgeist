/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator;

import server.world.Chunk;
import util.vec.IntVector;

/**
 * Interface for a world generator.
 *
 * @author TARS
 */
public interface WorldGenerator {

    /**
     * Generates a blank chunk for generating steps.
     *
     * @param chunkPos The chunk position.
     * @return A new chunk.
     */
    public Chunk createChunk(IntVector chunkPos);

    /**
     * Generates the chunk step for a given chunk. Throws a RuntimeException if
     * the chunk does not have the dependent chunk steps.
     *
     * @param chunk The chunk.
     * @param gs The step to generate.
     */
    public void generateStep(Chunk chunk, GenStep gs);

    /**
     * Returns the dependencies defined by the GenStep method and also extra
     * dependencies if the generator needs.
     *
     * @param gs The step to get the dependencies for.
     * @return The dependencies array.
     */
    public GenStep[] getDependencies(GenStep gs);
}
