package main.component.image_handler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Loads images
 */
public class ImageLoader {

    public BufferedImage loadImage(String path){
        try{
            return ImageIO.read(getClass().getResource(path));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
