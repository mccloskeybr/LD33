package ld33.map.map_objects;

import ld33.component.image_handler.ImageLoader;
import ld33.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * arrow class
 */
public class Arrow extends MapObject{

    public static final int DAMAGE = 1;

    private double y, movespeed;

    private static BufferedImage image;

    public Arrow(double x, double y){

        super(-1, x);

        this.y = y;

        movespeed = 64;

        if (image == null){
            image = new ImageLoader().loadImage("/image/arrow.png");
        }
    }

    public void update(float dt){
        setX(getX() - dt * movespeed);
    }

    public void render(Graphics g){
        g.drawImage(image, (int) getX() - Map.camx + ld33.component.Window.WINDOW_WIDTH / 2,
                (int) y, 32, 32, null);
    }
}
