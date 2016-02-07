package main.map.map_objects;

import main.component.image_handler.ImageLoader;
import main.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * arrow class
 */
public class Arrow {

    public static final int DAMAGE = 1;

    private double x, y, movespeed;

    private static BufferedImage image;

    public Arrow(double x, double y){
        this.x = x;
        this.y = y;

        movespeed = 64;

        if (image == null){
            image = new ImageLoader().loadImage("/image/arrow.png");
        }
    }

    public double getX(){
        return x;
    }

    public void update(float dt){
        x -= dt * movespeed;
    }

    public void render(Graphics g){
        g.drawImage(image, (int) x - Map.camx + main.component.Window.WINDOW_WIDTH / 2,
                (int) y, 32, 32, null);
    }
}
