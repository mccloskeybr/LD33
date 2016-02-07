package main.map.map_objects.entity;

import main.component.image_handler.SpriteSheet;
import main.component.visual_handler.Animator;
import main.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * little people that run around and die when stepped or smacked
 */
public class Civilian {

    private double x, y, moveSpeed;

    private BufferedImage[] run;
    private Animator animator;

    public Civilian(int x, int y){
        this.x = x;
        this.y = y;

        moveSpeed = new Random().nextInt(10) + 5;

        animator = new Animator();
        loadAnimations();
    }

    public double getX(){
        return x;
    }

    public void loadAnimations(){
        SpriteSheet civSpriteSheet = new SpriteSheet("/image/sprite_sheet/spritesheet_civ.png", 32, 32);

        run = new BufferedImage[6];
        run[0] = civSpriteSheet.getTile(0, 0);
        run[1] = civSpriteSheet.getTile(32, 0);
        run[2] = civSpriteSheet.getTile(64, 0);
        run[3] = civSpriteSheet.getTile(0, 32);
        run[4] = civSpriteSheet.getTile(32, 32);
        run[5] = civSpriteSheet.getTile(64, 32);

        animator.setFrames(run);
    }

    //runs away
    public void update(float dt){

        x += dt * moveSpeed;

        animator.update();

    }

    public void render(Graphics g){
        g.drawImage(animator.getCurrentFrame(), (int) x - Map.camx + main.component.Window.WINDOW_WIDTH / 2,
                (int) y, 32, 32, null);
    }
}
