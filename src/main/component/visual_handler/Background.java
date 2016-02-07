package main.component.visual_handler;

import main.component.image_handler.ImageLoader;
import main.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * handles the background
 */
public class Background {

    //x is coord for first instance, x2 is coord for second
    private int x,x2;
    private boolean first; //first refers to the first x and y coords

    private BufferedImage backgroundImage;

    public Background(){
        x = 0;
        x2 = 2880;

        backgroundImage = new ImageLoader().loadImage("/image/bg/level_background.png");
    }

    //returns first instance of the x thingy
    public int getX(){
        if (first) {
            return x2;
        } else {
            return x;
        }
    }

    public void updateX(){
        if (first) {
            x = x2 + 2880;
            first = false;
        } else {
            x2 = x + 2880;
            first = true;
        }
    }

    public void render(Graphics g){
        g.drawImage(backgroundImage, x - Map.camx, 0, 2880, 640, null);
        g.drawImage(backgroundImage, x2 - Map.camx, 0, 2880, 640, null);
    }

}
