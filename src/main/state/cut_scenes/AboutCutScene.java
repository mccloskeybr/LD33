package main.state.cut_scenes;

import main.component.audio_handler.AudioHandler;
import main.state.StateManager;

import java.awt.*;

/**
 * the about selection on the main menu, just scrolls through info about the game
 */
public class AboutCutScene extends CutSceneState {

    public AboutCutScene(AudioHandler audioHandler){
        super(audioHandler);
        setBackgroundImages(new String[]{
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",
                "/image/intro/intro1.png",

        });
        setFont(new Font("Ariel", Font.PLAIN, 24));
        setDialogue(new String[]{
                "Hey guys, this is just a little game I made for LD 33.",
                "I haven't participated in any of the previous Ludum Dare competitions",
                "but I've wanted to for a long time.",
                "Anyway, \"You are the Monster\" is really interesting.",
                "My take on this was to use the 'big and brutal' aspects of monsters.",
                "First things that came to mind were things like Rampage for the Game Cube",
                "as well as various shows that have huge monsters in them like LOTR and SnK",
                "and I think that's where I found the motivation for a medieval backdrop.",
                "I hope it turned out ok.",
                "Tell me what you think! - VampyVampire"
        });
    }

    @Override
    public void update(float dt) {
        if (isFinished()){
            stopAudio();
            StateManager.setCurrentStateIndex(StateManager.MENU_STATE_INDEX);
        }
    }
}
