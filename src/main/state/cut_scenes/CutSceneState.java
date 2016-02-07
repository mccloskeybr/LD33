package main.state.cut_scenes;

import main.component.audio_handler.AudioHandler;
import main.component.image_handler.ImageLoader;
import main.state.State;
import main.component.Window;
import main.state.StateManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * for cutscenes, scroll through dialogue
 */
public abstract class CutSceneState implements State {

    private int currentStringIndex;
    private AudioHandler audioHandler;

    private BufferedImage[] images;
    private Font dialogueFont;
    private Color dialogueColor;

    private String[] dialogue;

    public CutSceneState(AudioHandler audioHandler) {
        init();
        this.audioHandler = audioHandler;
    }

    //sets the dialogue options to be scrolled through
    public void setDialogue(String[] dialogue){
        this.dialogue = dialogue;
    }

    public void setBackgroundImages(String[] path){
        images = new BufferedImage[path.length];
        for (int i = 0; i < path.length; i++){
            images[i] = new ImageLoader().loadImage(path[i]);
        }
    }

    public void stopAudio(){
        audioHandler.stopAudio();
    }

    public void setFont(Font font){
        dialogueFont = font;
    }

    public boolean isFinished(){
        return currentStringIndex >= dialogue.length;
    }

    @Override
    public void init() {
        currentStringIndex = 0;

        dialogueFont = new Font("VCR OSD Mono", Font.PLAIN, 18);
        dialogueColor = new Color(255, 255, 255);
    }

    @Override
    public void render(Graphics g) {
        try {
            g.drawImage(images[currentStringIndex], 0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT, null);

            g.setColor(dialogueColor);
            g.setFont(dialogueFont);
            int stringWidth = g.getFontMetrics(dialogueFont).stringWidth(dialogue[currentStringIndex]);
            g.drawString(dialogue[currentStringIndex], (Window.WINDOW_WIDTH + stringWidth) / 2 - stringWidth, Window.WINDOW_HEIGHT / 2 + 200);
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Cutscene did the thing again");
        }
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_SPACE || k == KeyEvent.VK_ENTER){
            currentStringIndex++;
        }
    }

    @Override
    public void keyReleased(int k) {

    }

}
