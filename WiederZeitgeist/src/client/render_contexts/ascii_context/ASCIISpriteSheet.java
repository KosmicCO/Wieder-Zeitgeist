/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import graphics.Color;
import graphics.opengl.BufferObject;
import static graphics.opengl.GLObject.bindAll;
import graphics.opengl.Shader;
import graphics.opengl.Texture;
import graphics.opengl.VertexArrayObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import util.math.Transformation;
import util.math.Vec2d;

/**
 * A sprite sheet which takes in a grayscale image to replace white with a
 * background color and black with a foreground color.
 *
 * @author TARS
 */
public class ASCIISpriteSheet {

    public static final int SHEET_DEPTH = 32;

    private static final Map<String, ASCIISpriteSheet> ASCII_SPRITE_SHEET_CACHE = new HashMap();
    private static final Map<String, Vec2d> ASCII_DIMENSIONS = new HashMap();
    
    public static ASCIISpriteSheet load(String fileStem) {
        if (!ASCII_SPRITE_SHEET_CACHE.containsKey(fileStem)) {
            ASCIISpriteSheet s = new ASCIISpriteSheet(fileStem + ".png");
            ASCII_SPRITE_SHEET_CACHE.put(fileStem, s);
            ASCII_DIMENSIONS.put(fileStem, loadDimensions(fileStem + ".yml"));
        }
        return ASCII_SPRITE_SHEET_CACHE.get(fileStem);
    }

    public static Vec2d dim(String fileStem) {
        if(!ASCII_DIMENSIONS.containsKey(fileStem)){
            load(fileStem);
        }
        return ASCII_DIMENSIONS.get(fileStem);
    }

    public static final Shader ASCII_SPRITE_SHEET_SHADER = Shader.load("ascii_sheet");

    public static final VertexArrayObject ASCII_SPRITE_SHEET_VAO = VertexArrayObject.createVAO(() -> {
        BufferObject vbo = new BufferObject(GL_ARRAY_BUFFER, new float[]{
            0.5f, 0.5f, 0, 1, 1,
            0.5f, -0.5f, 0, 1, 0,
            -0.5f, -0.5f, 0, 0, 0,
            -0.5f, 0.5f, 0, 0, 1
        });
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 20, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 12);
        glEnableVertexAttribArray(1);
    });

    private final Texture texture;

    private ASCIISpriteSheet(String fileName) {
        this.texture = Texture.load(fileName);

    }

    public void draw(Transformation t, int id, Color fore, Color back) {
        drawTexture(texture, t, id, fore, back);
    }

    public static void drawTexture(Texture texture, Transformation t, int id, Color fore, Color back) {
        ASCII_SPRITE_SHEET_SHADER.setMVP(t);
        ASCII_SPRITE_SHEET_SHADER.setUniform("foreColor", fore);
        ASCII_SPRITE_SHEET_SHADER.setUniform("backColor", back);
        ASCII_SPRITE_SHEET_SHADER.setUniform("subCoords", new Vec2d(id % SHEET_DEPTH, 15 - id / SHEET_DEPTH));
        bindAll(texture, ASCII_SPRITE_SHEET_SHADER, ASCII_SPRITE_SHEET_VAO);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    public int getHeight() {
        return texture.getHeight();
    }

    public int getWidth() {
        return texture.getWidth();
    }

    private static Vec2d loadDimensions(String configPath) {
        try {
            YamlReader configRead = new YamlReader(new FileReader("sprites/" + configPath));
            ASCIISheetConfig conf = configRead.read(ASCIISheetConfig.class);
            return new Vec2d(conf.width, conf.height);
        } catch (YamlException | FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static class ASCIISheetConfig {

        public int width;
        public int height;
    }
}
