package main.component;

import javax.swing.*;
import java.awt.event.KeyListener;

/**
 * Game window, where everything is rendered n shit
 */
public class Window {

    public static final int WINDOW_WIDTH = 960, WINDOW_HEIGHT = 640;

    private JFrame frame;

    public Window(){
        frame = new JFrame();
        frame.setTitle(Main.NAME_GAME);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void addKeyListener(KeyListener keyListener){
        frame.addKeyListener(keyListener);
    }

    public void draw(JPanel jPanel){
        frame.add(jPanel);
        frame.setVisible(true);
    }

}
