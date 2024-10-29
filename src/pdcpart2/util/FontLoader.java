
package pdcpart2.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * FontLoader is a utility class that provides methods to load custom fonts.
 * 
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
public class FontLoader {

    /**
     * Loads a custom font from the specified path and size.
     *
     * @param path The path to the font file.
     * @param size The desired font size.
     * @return The loaded Font object.
     */
    public static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font); // Register the font with the graphics environment
            return font;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Serif", Font.PLAIN, (int) size); // Fallback to default font
        }
    }
}

