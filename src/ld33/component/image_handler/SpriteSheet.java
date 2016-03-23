package ld33.component.image_handler;

import java.awt.image.BufferedImage;

/**
 * For each entity/whatever needs a sprite sheet, grabs tiles for buffered image array to be thrown into animator
 */
public class SpriteSheet {

    private BufferedImage sheet;

    private int width, height;

    public SpriteSheet(String path, int width, int height){
        sheet = new ImageLoader().loadImage(path);

        this.width = width;
        this.height = height;
    }

    public BufferedImage getTile(int x, int y){
        return sheet.getSubimage(x, y, width, height);
    }
}
