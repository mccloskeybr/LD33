package main.map.map_objects.entity;

import main.component.Window;
import main.component.audio_handler.AudioHandler;
import main.component.image_handler.SpriteSheet;
import main.component.visual_handler.Animator;
import main.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Player class
 * */
public class Player {

    private static int CURRENT_ACTION, IDLE = 0, MOVING = 1, STRIKING = 2, BLOCKING = 3, JUMPING = 4, SLAMMING = 5;

    private long timeSinceLand, shakeTimer;
    private double x, y, moveSpeed, currentHeightJumped, jumpHeight;
    private int currentHealth;
    private boolean isMoving, isStriking, isAbleJump, isJumping, justLanded, isFalling, isBlocking;

    private boolean hasPlayedStep;

    private BufferedImage[] idle, moving, striking, blocking, jumping, slamming;

    private Animator animator;

    public Player(int x, int y){
        this.x = x;
        this.y = y;
        this.moveSpeed = 25;

        currentHealth = 20;
        jumpHeight = 5; //5 64 tile high jumps

        shakeTimer = 250;

        animator = new Animator();
        loadAnimations();
    }

    public void setMoving(boolean isMoving){
        this.isMoving = isMoving;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public int getHealth(){
        return currentHealth;
    }

    public boolean isStriking(){
        return isStriking;
    }

    public boolean isBlocking(){
        return isBlocking;
    }

    public boolean justLanded(){
        return justLanded;
    }

    //returns true if current health is below zero
    public boolean isDead(){
        return currentHealth <= 0;
    }

    public void setBlocking(boolean isBlocking){
        if (!isStriking) {
            this.isBlocking = isBlocking;
        }
    }

    public void takeDamage(int damage){
        currentHealth -= damage;
    }

    public void attack(){
        if (!isJumping && !isFalling && !isBlocking) {
            isStriking = true;
        }
    }

    public void jump(){
        if (!isBlocking && !justLanded && !isStriking) {
            if (isAbleJump && !isJumping) {
                isJumping = true;
            }
        }
    }

    public void stopJumping(){
        isJumping = false;
    }


    public void loadAnimations(){
        SpriteSheet playerSpriteSheet = new SpriteSheet("/image/sprite_sheet/spritesheet_player.png", 64, 64);

        idle = new BufferedImage[3];
        idle[0] = playerSpriteSheet.getTile(0, 192);
        idle[1] = playerSpriteSheet.getTile(64, 192);
        idle[2] = playerSpriteSheet.getTile(128,192);

        moving = new BufferedImage[6];
        moving[0] = playerSpriteSheet.getTile(0, 0);
        moving[1] = playerSpriteSheet.getTile(64, 0);
        moving[2] = playerSpriteSheet.getTile(128, 0);
        moving[3] = playerSpriteSheet.getTile(0, 64);
        moving[4] = playerSpriteSheet.getTile(64, 64);
        moving[5] = playerSpriteSheet.getTile(128, 64);

        striking = new BufferedImage[2];
        striking[0] = playerSpriteSheet.getTile(0, 128);
        striking[1] = playerSpriteSheet.getTile(64, 128);

        blocking = new BufferedImage[1];
        blocking[0] = playerSpriteSheet.getTile(128, 128);

        jumping = new BufferedImage[1];
        jumping[0] = playerSpriteSheet.getTile(0, 128);

        slamming = new BufferedImage[1];
        slamming[0] = playerSpriteSheet.getTile(128, 128);

        animator.setFrames(idle);
        animator.setSpeed(400);
    }

    //updates all the stuff for the entity
    public void update(float dt){
        updateAnimation();
        move(dt);
        gravity(dt);

        if (isMoving){
            if ((animator.getCurrentIndex() == 0 || animator.getCurrentIndex() == 3) && !hasPlayedStep){
                new AudioHandler().playAudio("/sound/playerstep.wav", -15.0f);
                hasPlayedStep = true;
            } else if (animator.getCurrentIndex() != 0 && animator.getCurrentIndex() != 3){
                hasPlayedStep = false;
            }
        }

        if (isStriking){
            if (animator.hasCompleted()){
                isStriking = false;
            }
        }
        if (justLanded){
            if (System.currentTimeMillis() - timeSinceLand >= shakeTimer){
                justLanded = false;
            }
        }
    }

    public void gravity(float dt){
        if (!isJumping){
            if (y < Window.WINDOW_HEIGHT){
                y += 64 * dt;
                isFalling = true;
                isAbleJump = false;
            } else {
                isAbleJump = true;
                currentHeightJumped = 0;

                if (isFalling) {
                    justLanded = true;
                    timeSinceLand = System.currentTimeMillis();
                    isFalling = false;
                }
            }
        } else {
            y -= 64 * dt;
            currentHeightJumped += 1 * dt;
        }
    }

    public void move(float dt){
        if (!isBlocking && !justLanded) {
            if (isMoving) {
                x += moveSpeed * dt;
            }
        }
        if (isJumping){
            if (currentHeightJumped >= jumpHeight){
                isJumping = false;
            }
        }
    }

    //updates the animation
    public void updateAnimation(){
        animator.update();

        if (isMoving && !isStriking) {
            if (CURRENT_ACTION != MOVING) {
                CURRENT_ACTION = MOVING;
                animator.setFrames(moving);
            }
        }
        if (isStriking){
            if (CURRENT_ACTION != STRIKING){
                CURRENT_ACTION = STRIKING;
                animator.setFrames(striking);
                new AudioHandler().playAudio("/sound/punch.wav", -15.0f);
            }
        }
        if (isBlocking){
            if (CURRENT_ACTION != BLOCKING){
                CURRENT_ACTION = BLOCKING;
                animator.setFrames(blocking);
            }
        }
        if (isJumping){
            if (CURRENT_ACTION != JUMPING){
                CURRENT_ACTION = JUMPING;
                animator.setFrames(jumping);
            }
        }
        if (isFalling){
            if (CURRENT_ACTION != SLAMMING){
                CURRENT_ACTION = SLAMMING;
                animator.setFrames(slamming);
            }
        }
        if (!isMoving && !isStriking && !isBlocking && !isJumping && !isFalling){
            if (CURRENT_ACTION != IDLE){
                CURRENT_ACTION = IDLE;
                animator.setFrames(idle);
                animator.setSpeed(400);
            }
        }
    }

    public void render(Graphics g){
        g.drawImage(animator.getCurrentFrame(), (int) x - Map.camx + Window.WINDOW_WIDTH / 2,
                (int) y - Window.WINDOW_HEIGHT / 2 - 90, 384, 384, null);
    }


}
