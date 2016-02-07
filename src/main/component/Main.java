package main.component;

import main.state.StateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Contains main method and game loop
 */
public class Main extends JPanel{

    public static final String NAME_GAME = "Colossus Guard";

    public static void main(String[] args){

        Window window = new Window();
        new Main(window);

    }

    private Window window;

    private boolean isRunning;
    private long timeSinceLastFrame;

    public Main(Window window){
        this.window = window;

        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
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
        StateManager.resetGame();
        isRunning = true;
    }

    private void update(){

        float dt = System.currentTimeMillis() - timeSinceLastFrame;
        timeSinceLastFrame = System.currentTimeMillis();

        if (dt > 0.02f){
            dt = 0.02f;
        }

        StateManager.getCurrentState().update(dt);
        window.draw(this);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        StateManager.getCurrentState().render(g);
    }

}
