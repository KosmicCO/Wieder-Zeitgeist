/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator;

import java.util.Arrays;

/**
 * Enum keeping ids for chunk loading steps. The steps must have a unique id
 * such that their ids are sequential.
 *
 * @author TARS
 */
public enum GenStep {

    /**
     * Generates the structures that store block data.
     */
    BLOCKS(0),
    /**
     * Generates everything needed to render the chunk.
     */
    RENDER(1, BLOCKS);

    /**
     * The id for the step. Also works as the index for an array keeping track
     * of whether it has been generated.
     */
    public final int id;
    private final GenStep[] dependencies;

    private GenStep(int id, GenStep... dependents) {
        this.id = id;
        dependencies = dependents;
    }

    /**
     * Gets the dependencies of the step.
     *
     * @return An array of the dependencies.
     */
    public GenStep[] getDependencies() {
        return Arrays.copyOf(dependencies, dependencies.length);
    }
}
