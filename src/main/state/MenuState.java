package main.state;

import main.component.audio_handler.AudioHandler;
import main.component.image_handler.ImageLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.component.Window;

/**
 * Main menu
 */
public class MenuState implements State {

    private final String[] choices = {
            "Play",
            "About",
            "Exit"
    };

    private static AudioHandler audioHandler;

    private int currentSelectionIndex, currentAlpha, fadeTimer;
    private boolean hasSelected;

    private Color optionColor, currentSelectionColor;
    private Font optionFont, creditFont;

    private BufferedImage background;

    public MenuState(){
        init();
    }

    public static AudioHandler getAudioHandler(){
        return audioHandler;
    }

    @Override
    public void init() {
        optionColor = new Color(44, 44, 44);
        currentSelectionColor = new Color (138, 138, 138);

        optionFont = new Font("VCR OSD Mono", Font.PLAIN, 36);
        creditFont = new Font("VCR OSD Mono", Font.PLAIN, 12);

        background = new ImageLoader().loadImage("/image/bg/main_menu.png");

        currentSelectionIndex = 0;
        hasSelected = false;

        audioHandler = new AudioHandler();
        audioHandler.loopAudio("/sound/menu_bg.wav", -10.0f);
    }

    @Override
    public void update(float dt) {
        if (hasSelected){
            if (fadeTimer > 15) {
                if (currentSelectionIndex == 0) {
                    StateManager.setCurrentStateIndex(StateManager.CUTSCENE_INDEX);
                } else if (currentSelectionIndex == 1) {
                    StateManager.setCurrentStateIndex(StateManager.ABOUT_INDEX);
                } else if (currentSelectionIndex == 2) {
                    System.exit(1);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT, null);

        g.setFont(optionFont);
        for (int i = 0; i < choices.length; i++){
            if (i == currentSelectionIndex){
                g.setColor(currentSelectionColor);
            } else {
                g.setColor(optionColor);
            }
            int stringWidth = g.getFontMetrics(optionFont).stringWidth(choices[i]);
            g.drawString(choices[i], (Window.WINDOW_WIDTH + stringWidth) / 2 - stringWidth, Window.WINDOW_HEIGHT / 2 + 50 * i);
        }

        g.setColor(optionColor);
        g.setFont(creditFont);
        g.drawString("Made by VampyVampire", Window.WINDOW_WIDTH - 200, Window.WINDOW_HEIGHT - 70);
        g.drawString("IntelliJ 14.1.14, Java 1.8", Window.WINDOW_WIDTH - 200, Window.WINDOW_HEIGHT - 60);
        g.drawString("Graphics Gale, Paint.net", Window.WINDOW_WIDTH - 200, Window.WINDOW_HEIGHT - 50);
        g.drawString("Abundant Music, SFXR", Window.WINDOW_WIDTH - 200, Window.WINDOW_HEIGHT - 40);

        //fades to black when something has been selected
        if (hasSelected){
            g.setColor(new Color(0, 0, 0, currentAlpha));
            g.fillRect(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
            currentAlpha += 255 / 15;
            if (currentAlpha > 255){
                currentAlpha = 255;
            }
            fadeTimer++;
        }

    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S){
            new AudioHandler().playAudio("/sound/menuscroll.wav", -10.0f);
            currentSelectionIndex ++;
            if (currentSelectionIndex == choices.length){
                currentSelectionIndex = 0;
            }
        } else if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W){
            new AudioHandler().playAudio("/sound/menuscroll.wav", -10.0f);
            currentSelectionIndex --;
            if (currentSelectionIndex == -1){
                currentSelectionIndex = choices.length - 1;
            }
        } else if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE){
            if (!hasSelected) {
                new AudioHandler().playAudio("/sound/menuselect.wav", -10.0f);
                hasSelected = true;
            }
        } else if (k == KeyEvent.VK_M){
            AudioHandler.isMuted = !AudioHandler.isMuted;
            if (AudioHandler.isMuted){
                audioHandler.stopAudio();
            } else {
                audioHandler.loopAudio("/sound/menu_bg.wav", -10.0f);
            }
        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
