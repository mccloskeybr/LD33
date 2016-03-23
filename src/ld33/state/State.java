package ld33.state;

import java.awt.*;

/**
 * State interface
 */
public interface State {

    //initialize the state
    void init();

    //update the state (step updater)
    void update(float dt);

    //render whatever the state needs rendered
    void render(Graphics g);

    //key controls
    void keyPressed(int k);
    void keyReleased(int k);

}
