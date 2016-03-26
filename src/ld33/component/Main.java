package ld33.component;

import ld33.state.StateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Contains main method and game loop
 */
public class Main extends JPanel{

    public static final String NAME_GAME = "Colossus Guard";

    private boolean cap;
    private boolean render;

    public static void main(String[] args){

        Window window = new Window();
        new Main(window);

    }

    private Window window;

    private boolean isRunning;
    private long timeSinceLastFrame;

    public Main(Window window){
        this.window = window;
        render = true;

        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z)
                    cap = !cap;
                else if (e.getKeyCode() == KeyEvent.VK_R)
                    render = !render;
                else
                    StateManager.getCurrentState().keyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                StateManager.getCurrentState().keyReleased(e.getKeyCode());
            }
        });

        new CustomFont().registerFont("/fonts/VCR_OSD_MONO_1.001.ttf");

        Thread gameThread = new Thread(() -> {
            init();
            while (isRunning) {
                while (!StateManager.isPaused()) {
                    update();
                }

                try{
                    Thread.sleep(100);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        gameThread.start();
    }

    public void init(){
        window.draw(this);

        StateManager.resetGame();
        isRunning = true;
    }

    private void update(){

        float dt = System.currentTimeMillis() - timeSinceLastFrame;
        timeSinceLastFrame = System.currentTimeMillis();

        if (!cap && dt > 0.02f){
            dt = 0.02f;
        }

        StateManager.getCurrentState().update(dt);

        if (render)
            repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        StateManager.getCurrentState().render(g);
    }

}
