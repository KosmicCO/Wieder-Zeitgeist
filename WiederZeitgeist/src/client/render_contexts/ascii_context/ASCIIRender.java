/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context;

import engine.Settings;
import graphics.Camera;
import graphics.Color;
import util.math.IntVectorN;
import util.math.Transformation;
import util.math.Vec2d;
import util.math.VectorN;
import static util.math.VectorN.EPSILON;

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

    public static void drawRect(VectorN position, VectorN rectSize, Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null");
        }
        VectorN correction = rectSize.map(v -> Math.min(v, 0));
        VectorN pos1 = position.add(correction);
        VectorN pos = pos1.map(v -> Math.max(v, 0));
        VectorN endpos = rectSize.sub(correction.mult(2)).sub(pos.sub(pos1)).bimap((v, d) -> Math.min(v, d), renderDim.toVectorN()).add(pos);

        if (renderDim.x() > 0 && renderDim.y() > 0) {
            for (int i = (int) pos.x(); i < endpos.x() - EPSILON; i++) {
                for (int j = (int) pos.y(); j < endpos.y() - EPSILON; j++) {
                    checklessDrawTile(i, j, tile);
                }
            }
        }
    }

    public static void fill(Tile tile){
        for (int i = 0; i < renderDim.x(); i++) {
            for (int j = 0; j < renderDim.y(); j++) {
                tileBuffer[i][j] = tile;
            }
        }
    }
    
    public static void drawForeground(int x, int y, int tileID, Color color) {
        drawTile(x, y, new Tile(tileID, color, Color.CLEAR));

    }

    public static void checklessDrawTile(int x, int y, Tile t) {
        
        if(t.back.a == 1){
            tileBuffer[x][y] = t;
        }else{
            Tile under = tileBuffer[x][y];
            if(under == null){
                tileBuffer[x][y] = t;
            }
            tileBuffer[x][y] = new Tile(t.id, t.fore, under.back.alphaMix(t.back));
        }
        
    }

    public static void drawTile(int x, int y, Tile t) {
        if (renderDim.x() <= x || x < 0 || renderDim.y() <= y || y < 0) {
            throw new IllegalArgumentException("Given coordinates are not in bounds: (" + x + ", " + y + ").");
        }
        checklessDrawTile(x, y, t);
    }
}
