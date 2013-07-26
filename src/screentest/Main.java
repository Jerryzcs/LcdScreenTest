package screentest;

/* Public domain. Use at your own risk. */

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import java.nio.ByteBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

/**
 * Main class.
 * 
 * Don't be impressed.
 */
public class Main extends SimpleApplication implements ActionListener {
    /** Quad for the on-screen object. */
    Quad q;
    /** Geometry for the on-screen object. */
    Geometry geom;
    /** Solid color material (red/green/blue) for the on-screen object. */
    Material mat;
    /** Image for the checkerboard pattern. */
    Image ichecker;
    /** Reversed image for the checkerboard pattern. */
    Image irchecker;
    /** Texture for the checkerboard pattern. */
    Texture2D tchecker;
    /** Reversed texture for the checkerboard pattern. */
    Texture2D trchecker;
    /** Material for the checkerboard pattern. */
    Material mchecker;
    /** Reversed material for the checkerboard pattern. */
    Material mrchecker;
    /** What test mode we are in. */
    int test_mode = 0;
    /** Current "seizure mode" color. */
    int seize_color = 0;
    /** Which checkerboard texture is in use. */
    boolean rcheck = false;
    /** Screen X size */
    int screen_x;
    /** Screen Y size */
    int screen_y;
    /** Standard log4j logger object */
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Main method
     * @param args Arguments passed to this program. Currently ignored. 
     */
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
	screen_x = settings.getWidth();
	screen_y = settings.getHeight();

        inputManager.addMapping("Red",
                new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Green",
                new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Blue",
                new KeyTrigger(KeyInput.KEY_3));
	inputManager.addMapping("Seizure",
		new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("White",
                new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("Black",
                new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("Checker",
                new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("Checker Freeze",
                new KeyTrigger(KeyInput.KEY_8));

	inputManager.addListener(this, "Red", "Green", "Blue", "Seizure");
	inputManager.addListener(this, "White", "Black", "Checker", "Checker Freeze");

	q = new Quad(screen_x,screen_y);
        geom = new Geometry("Quad", q);

        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        geom.setMaterial(mat);

        guiNode.attachChild(geom);

	ichecker = makecheckerpattern(false);
	mchecker = new Material(assetManager, "Common/MatDefs/Misc/ColoredTextured.j3md");
	mchecker.setColor("Color", ColorRGBA.White);
	tchecker = new Texture2D(ichecker);
	mchecker.setTexture("ColorMap", tchecker);

	irchecker = makecheckerpattern(true);
	mrchecker = new Material(assetManager, "Common/MatDefs/Misc/ColoredTextured.j3md");
	mrchecker.setColor("Color", ColorRGBA.White);
	trchecker = new Texture2D(irchecker);
	mrchecker.setTexture("ColorMap", trchecker);

	setDisplayFps(false);
	setDisplayStatView(false);
    }

    /**
     * Makes the checkered pattern image.
     * 
     * A sequence of bytes is written to a buffer and interpreted as an 8-bit
     * grayscale image.
     * 
     * @param reverse When true, the image will be inverse of normal (black
     * texels will be white and vice versa).
     * @return Image object for this texture.
     * 
     * @author ccfreak2k
     */
    private Image makecheckerpattern(boolean reverse) {
	// I forgot how to flatten 2D loops into 1D arrays :(
	byte check_pattern[] = new byte[screen_x*screen_y];
	boolean white = false;
	int k = 0;

	if (reverse) {
	    white = !white;
	}

	for (int i = 0; i < screen_y; i++) {
	    for (int j = 0; j < screen_x; j++) {
		white = !white;
		if (white) {
		    check_pattern[k] = (byte)0xFF;
		} else {
		    check_pattern[k] = (byte)0x00;
		}
		k++;
	    }
	    white = !white;
	}
	ByteBuffer tempData = BufferUtils.createByteBuffer(screen_x*screen_y);
        tempData.put(check_pattern).flip();
        return new Image(Format.Luminance8, screen_x, screen_y, tempData);
    }
    

    @Override
    public void simpleUpdate(float tpf) {
	// Non-checker mode
	switch (test_mode) {
	    case 0: geom.setMaterial(mat); mat.setColor("Color", ColorRGBA.Red); break;
	    case 1: geom.setMaterial(mat); mat.setColor("Color", ColorRGBA.Green); break;
	    case 2: geom.setMaterial(mat); mat.setColor("Color", ColorRGBA.Blue); break;
	    case 3: geom.setMaterial(mat); seize(); break;
	    case 4: geom.setMaterial(mat); mat.setColor("Color", ColorRGBA.White); break;
	    case 5: geom.setMaterial(mat); mat.setColor("Color", ColorRGBA.Black); break;
	    case 6:
		if (rcheck) {
		    geom.setMaterial(mrchecker);
		} else {
		    geom.setMaterial(mchecker);
		} rcheck = !rcheck; break;
	    case 7: geom.setMaterial(mchecker); break;
	}
    }

    /**
     * Swaps the colors during the "seizure mode." Called once each frame.
     * 
     * @author ccfreak2k
     */
    private void seize() {
	switch (seize_color) {
	    case 0: mat.setColor("Color", ColorRGBA.Red); break;
	    case 1: mat.setColor("Color", ColorRGBA.Green); break;
	    case 2: mat.setColor("Color", ColorRGBA.Blue); break;
	}
	seize_color++;
	if (seize_color > 2) {
	    seize_color = 0;
	}
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /**
     * Called on keypresses.
     * @param name Name of the action
     * @param value True when pressed, false when released
     * @param tpf Time since last frame
     */
    public void onAction(String name, boolean value, float tpf) {
	if (name.equals("Red")) {
            if (value) {
		test_mode = 0;
	    }
	}

	if (name.equals("Green")) {
            if (value) {
		test_mode = 1;
	    }
	}

	if (name.equals("Blue")) {
            if (value) {
		test_mode = 2;
	    }
	}

	if (name.equals("Seizure")) {
	    if (value) {
		test_mode = 3;
	    }
	}

	if (name.equals("White")) {
            if (value) {
		test_mode = 4;
	    }
	}

	if (name.equals("Black")) {
            if (value) {
		test_mode = 5;
	    }
	}

	if (name.equals("Checker")) {
            if (value) {
		test_mode = 6;
	    }
	}

	if (name.equals("Checker Freeze")) {
            if (value) {
		test_mode = 7;
	    }
	}
    }
}
