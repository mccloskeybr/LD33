package ld33.map.map_objects.entity;

import ld33.component.audio_handler.AudioHandler;
import ld33.component.image_handler.SpriteSheet;
import ld33.component.visual_handler.Animator;
import ld33.map.Map;
import ld33.map.map_objects.Arrow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Enemy
 */
public class Archer {

    private double x, y;
    private long shotTimer, timeSinceLastShot;

    private boolean isShooting;

    private ArrayList<Arrow> arrows;

    private BufferedImage[] shoot, idle;
    private Animator animator;
    private SpriteSheet archerSpriteSheet;

    public Archer(int x, int y){
        this.x = x;
        this.y = y;

        shotTimer = new Random().nextInt(5000) + 5000;

        arrows = new ArrayList<>();

        animator = new Animator();
        loadAnimations();
    }

    public ArrayList<Arrow> getArrows(){
        return arrows;
    }

    public double getX(){
        return x;
    }

    public void loadAnimations(){
        archerSpriteSheet = new SpriteSheet("/image/sprite_sheet/spritesheet_archer.png", 32, 32);

        idle = new BufferedImage[1];
        idle[0] = archerSpriteSheet.getTile(0, 0);

        shoot = new BufferedImage[3];
        shoot[0] = archerSpriteSheet.getTile(32, 0);
        shoot[1] = archerSpriteSheet.getTile(64, 0);
        shoot[2] = archerSpriteSheet.getTile(32, 0);

        animator.setFrames(idle);
    }

    public void update(float dt){
        if (isShooting) {
            if (animator.hasCompleted()) {
                isShooting = false;
                animator.setFrames(idle);
            }
        }

        if (System.currentTimeMillis() - timeSinceLastShot >= shotTimer) {
            timeSinceLastShot = System.currentTimeMillis();
            shotTimer = new Random().nextInt(5000) + 5000;

            arrows.add(new Arrow(x, y));
            animator.setFrames(shoot);
            isShooting = true;

            new AudioHandler().playAudio("/sound/bowtwang.wav", -20.0f);
        }

        for (Arrow arrow : arrows) {
            arrow.update(dt);
        }
        animator.update();
    }

    public void render(Graphics g){
        g.drawImage(animator.getCurrentFrame(), (int) x - Map.camx + ld33.component.Window.WINDOW_WIDTH / 2,
                (int) y, 32, 32, null);

        for (Arrow arrow: arrows){
            arrow.render(g);
        }
    }

}
