package main.map.map_objects;

import main.component.audio_handler.AudioHandler;
import main.component.image_handler.ImageLoader;
import main.component.image_handler.SpriteSheet;
import main.component.visual_handler.Animator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * houses
 */
public class House {

    private BufferedImage[] sinking, crumbling;
    private Animator animator;

    private boolean isSinking, hasTakenDamage;
    private double x, y, origy;
    private int health = 3;
    private BufferedImage image;

    public House(int x, int y){

        this.x = x;
        this.y = origy = y;

        Random rand = new Random();
        int i = rand.nextInt(3);
        if (i == 0){
            image = new ImageLoader().loadImage("/image/house/house1.png");
        } else if (i == 1){
            image = new ImageLoader().loadImage("/image/house/house2.png");
        } else {
            image = new ImageLoader().loadImage("/image/house/house3.png");
        }

        animator = new Animator();
        loadAnimations();

    }

    public void loadAnimations(){
        SpriteSheet buildingSpriteSheet = new SpriteSheet("/image/sprite_sheet/spritesheet_buildingfall.png", 48, 36);

        sinking = new BufferedImage[7];
        sinking[0] = buildingSpriteSheet.getTile(0, 0);
        sinking[1] = buildingSpriteSheet.getTile(48, 0);
        sinking[2] = buildingSpriteSheet.getTile(96, 0);
        sinking[3] = buildingSpriteSheet.getTile(0, 36);
        sinking[4] = buildingSpriteSheet.getTile(48, 36);
        sinking[5] = buildingSpriteSheet.getTile(96, 36);
        sinking[6] = buildingSpriteSheet.getTile(0, 72);

        buildingSpriteSheet = new SpriteSheet("/image/sprite_sheet/spritesheet_buildingcrumble.png", 32, 36);

        crumbling = new BufferedImage[3];
        crumbling[0] = buildingSpriteSheet.getTile(0, 0);
        crumbling[1] = buildingSpriteSheet.getTile(32, 0);
        crumbling[2] = buildingSpriteSheet.getTile(64, 0);
    }

    public int getX(){
        return (int) x;
    }

    public int getY(){
        return (int) y;
    }

    public boolean isSinking(){
        return isSinking;
    }

    public boolean hasTakenDamage(){
        return hasTakenDamage;
    }

    public void setTakenDamage(boolean hasTakenDamage){
        this.hasTakenDamage = hasTakenDamage;
    }

    public void takeDamage(){
        health -= 1;
        if (health <= 0 && !isSinking){
            new AudioHandler().playAudio("/sound/buildingcollapse.wav", -10.0f);
            isSinking = true;
            animator.setFrames(sinking);
            animator.setSpeed(300);
        }

        hasTakenDamage = true;
    }

    public void sink(float dt){
        y += 5 * dt;
    }

    //y param not necessary as all houses are same height
    public void render(Graphics g, int x){
        g.drawImage(image, x, (int) y, 192, 215, null);
        if (isSinking) {
            g.drawImage(crumbling[2], x, (int) y, 192, 215, null);
            g.drawImage(animator.getCurrentFrame(), x, (int) origy - 25, 192, 215, null);

            if (!animator.hasCompleted()) {
                animator.update();
            }
            //crumbling overlay when health get below threshold
        } else if (health != 4){
            if (health < 2){
                g.drawImage(crumbling[2], x, (int) y, 192, 215, null);
            } else if (health < 3){
                g.drawImage(crumbling[1], x, (int) y, 192, 215, null);
            } else if (health < 4){
                g.drawImage(crumbling[0], x, (int) y, 192, 215, null);
            }
        }
    }
}
