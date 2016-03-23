package ld33.state;

/**
 * essentially a carbon copy of menu class.
 * player is dead screen
 */

import ld33.component.audio_handler.AudioHandler;
import ld33.component.image_handler.ImageLoader;

import ld33.component.Window;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
;


/**
 * Main menu
 */
public class DeathMenu implements State {

    private final String[] choices = {
            "Play Again",
            "Main Menu",
            "Exit"
    };

    private int currentSelectionIndex, currentAlpha, fadeTimer;
    private boolean hasSelected;

    private Color optionColor, currentSelectionColor, scoreColor;
    private Font optionFont, scoreFont;

    private BufferedImage background;

    public DeathMenu(){
        init();
    }

    @Override
    public void init() {
        optionColor = new Color(44, 44, 44);
        currentSelectionColor = new Color (138, 138, 138);
        scoreColor = new Color(186, 186, 186);

        scoreFont = new Font("VCR OSD Mono", Font.PLAIN, 60);
        optionFont = new Font("VCR OSD Mono", Font.PLAIN, 36);

        background = new ImageLoader().loadImage("/image/bg/death_screen.png");

        currentSelectionIndex = 0;
        hasSelected = false;
    }

    @Override
    public void update(float dt) {
        if (hasSelected){
            if (fadeTimer > 15) {
                GameState.resetScore();
                if (currentSelectionIndex == 0) {
                    StateManager.setCurrentStateIndex(StateManager.GAME_STATE_INDEX);
                } else if (currentSelectionIndex == 1) {
                    StateManager.setCurrentStateIndex(StateManager.MENU_STATE_INDEX);
                } else if (currentSelectionIndex == 2) {
                    System.exit(1);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, ld33.component.Window.WINDOW_WIDTH, ld33.component.Window.WINDOW_HEIGHT, null);

        g.setFont(optionFont);
        for (int i = 0; i < choices.length; i++){
            if (i == currentSelectionIndex){
                g.setColor(currentSelectionColor);
            } else {
                g.setColor(optionColor);
            }
            int stringWidth = g.getFontMetrics(optionFont).stringWidth(choices[i]);
            g.drawString(choices[i], (ld33.component.Window.WINDOW_WIDTH + stringWidth) / 2 - stringWidth, ld33.component.Window.WINDOW_HEIGHT / 2 + 100 + 50 * i);
        }

        g.setFont(scoreFont);
        g.setColor(scoreColor);
        g.drawString("" + GameState.getScore(), Window.WINDOW_WIDTH / 2 - 100, Window.WINDOW_HEIGHT / 2 - 15);

        //fades to black when something has been selected
        if (hasSelected){
            g.setColor(new Color(0, 0, 0, currentAlpha));
            g.fillRect(0, 0, ld33.component.Window.WINDOW_WIDTH, ld33.component.Window.WINDOW_HEIGHT);
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
        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
