/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import static client.ClientListener.CLIENT_LISTENER;
import graphics.Color;
import java.io.File;
import java.nio.IntBuffer;
import java.util.function.Consumer;
import messages.client.view.RenderMessage;
import messages.client.view.WindowShouldClose;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryUtil.NULL;
import util.Utils;
import util.math.VectorN;

/**
 * Class for managing the window for rendering and other basic rendering
 * features.
 *
 * @author TARS
 */
public class Window {

    private static double previousTime;
    private static Consumer<Double> render;
    private static Window window;

    private static void checkInit() {
        if (window == null) {
            throw new RuntimeException("Window has not been initialized yet.");
        }
    }

    /**
     * Cleans up the GLFW library and the window to terminate the program.
     */
    public static void cleanupGLFW() {
        window.cleanup();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Gets the color of the background of the screen.
     *
     * @return The color of the background.
     */
    public static Color getBackground() {
        checkInit();
        return window.background;
    }

    /**
     * Gets the current dimensions of the window.
     *
     * @return The current dimensions of the window.
     */
    public static VectorN getDimensions() {
        checkInit();
        return window.dimensions;
    }

    /**
     * Gets the current title of the window.
     *
     * @return The name of the window.
     */
    public static String getTitle() {
        checkInit();
        return window.name;
    }

    /**
     * Initializes the window singleton.
     *
     * @param name The title of the window.
     * @param dim The dimensions of the window.
     */
    public static void initialize(String name, VectorN dim) {
        System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = new Window(name, dim, Color.BLACK);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLUtil.setupDebugMessageCallback();

        Input.initialize();

        CLIENT_LISTENER.addListener(RenderMessage.class, m -> {
            double newTime = Utils.getTime();
            glfwPollEvents();
            if (render != null) {
                render.accept(newTime - previousTime);
            }
            previousTime = newTime;
            if (glfwWindowShouldClose(window.windowID)) {
                CLIENT_LISTENER.receiveMessage(new WindowShouldClose());
            }
            CLIENT_LISTENER.receiveMessage(m);
        });

        CLIENT_LISTENER.receiveMessage(new RenderMessage());
    }

    /**
     * Sets the background color of the screen.
     *
     * @param back The color to set as the background.
     */
    public static void setBackground(Color back) {
        checkInit();
        window.background = back;
    }

    static void setCursorPosCallback(GLFWCursorPosCallbackI cursorPosCallback) {
        checkInit();
        glfwSetCursorPosCallback(window.windowID, cursorPosCallback);
    }

    /**
     * Sets the dimensions of the screen.
     *
     * @param dim The dimensions to set the window to.
     */
    public static void setDimensions(VectorN dim) {
        checkInit();
        // resize?
        window.dimensions = dim;
    }

    static void setKeyCallback(GLFWKeyCallbackI keyCallback) {
        checkInit();
        glfwSetKeyCallback(window.windowID, keyCallback);
    }

    static void setMouseButtonCallback(GLFWMouseButtonCallbackI mouseButtonCallback) {
        checkInit();
        glfwSetMouseButtonCallback(window.windowID, mouseButtonCallback);
    }

    /**
     * Sets the consumer to be run every frame.
     *
     * @param r The consumer which takes the delta time in as an input.
     */
    public static void setRender(Consumer r) {
        checkInit();
        render = r;
    }

    static void setScrollCallback(GLFWScrollCallbackI scrollCallback) {
        checkInit();
        glfwSetScrollCallback(window.windowID, scrollCallback);
    }

    /**
     * Sets the title of the window.
     *
     * @param name The new title of the window.
     */
    public static void setTitle(String name) {
        checkInit();
        window.name = name;
        GLFW.glfwSetWindowTitle(window.windowID, name);
    }

    private Color background;
    private VectorN dimensions;
    private String name;
    private final long windowID;

    private Window(String name, VectorN dim, Color back) {

        this.name = name;
        dimensions = dim;
        background = back;

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // Create the window
        windowID = glfwCreateWindow((int) dimensions.x(), (int) dimensions.y(), name, NULL, NULL);
        if (windowID == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Get the thread stack and push a new frame
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowID, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    windowID,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);
        // Enable v-sync

        // Make the window visible
        glfwShowWindow(windowID);
    }

    private void cleanup() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);
    }
}
