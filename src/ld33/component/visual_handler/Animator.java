package ld33.component.visual_handler;

import java.awt.image.BufferedImage;

/**
 * keeps track of current sprite in animation
 */
public class Animator {

    private int currentIndex;
    private BufferedImage[] frames;
    private boolean hasCompleted;

    private long timeSinceLastUpdate, updateSpeed = 180;

    public void update(){
        if (System.currentTimeMillis() - timeSinceLastUpdate >= updateSpeed){
            currentIndex++;
            if (currentIndex == frames.length){
                currentIndex = 0;
                hasCompleted = true;
            }
            timeSinceLastUpdate = System.currentTimeMillis();
        }
    }

    public int getCurrentIndex(){
        return currentIndex;
    }

    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        reset();
    }

    public void setSpeed(long updateSpeed){
        this.updateSpeed = updateSpeed;
    }

    //resets the animation
    public void reset(){
        currentIndex = 0;
        updateSpeed = 180;
        hasCompleted = false;
    }

    //true after 1 complete run through of the frames
    public boolean hasCompleted(){
        return hasCompleted;
    }

    public BufferedImage getCurrentFrame(){
        return frames[currentIndex];
    }


}
