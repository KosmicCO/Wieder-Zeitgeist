/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context;

import engine.Settings;
import graphics.Camera;
import util.math.IntVectorN;
import util.math.Transformation;
import util.math.Vec2d;

/**
 *
 * @author TARS
 */
public class ASCIIRender {

    private static IntVectorN renderDim;
    private static Tile[][] tileBuffer;

    public static IntVectorN getRenderDim() {
        return renderDim;
    }

    public static void startRender(String asciiSheet) {
        Vec2d aDim = ASCIISpriteSheet.dim(asciiSheet);
        renderDim = IntVectorN.of((int) Math.floor(Settings.WINDOW_WIDTH / aDim.x), (int) Math.floor(Settings.WINDOW_HEIGHT / aDim.y));
        tileBuffer = new Tile[renderDim.x()][renderDim.y()];
    }

    public static void finishRender(String asciiSheet) {
        Vec2d aDim = ASCIISpriteSheet.dim(asciiSheet);
        Vec2d viewSize = (new Vec2d(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT)).div(aDim);
        Camera.camera2d.setCenterSize(new Vec2d(0, 0), viewSize);
        for (int x = 0; x < tileBuffer.length; x++) {
            for (int y = 0; y < tileBuffer[0].length; y++) {
                if (tileBuffer[x][y] != null) {
                    ASCIISpriteSheet.load(asciiSheet).draw(Transformation.create(new Vec2d(x - renderDim.x() / 2.0 + 0.5, y - renderDim.y() / 2.0 + 0.5), 0, 1),
                            tileBuffer[x][y].id, tileBuffer[x][y].fore, tileBuffer[x][y].back);
                }
            }
        }
    }

    public static void drawTile(int x, int y, Tile t) {
        if (renderDim.x() <= x || x < 0 || renderDim.y() <= y || y < 0) {
            throw new IllegalArgumentException("Given coordinates are not in bounds: (" + x + ", " + y + ").");
        }
        tileBuffer[x][y] = t;
    }
}
