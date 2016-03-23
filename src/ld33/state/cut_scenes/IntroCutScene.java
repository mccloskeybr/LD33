package ld33.state.cut_scenes;

import ld33.component.audio_handler.AudioHandler;
import ld33.state.StateManager;

/**
 * Cut scene after play has been selected
 */
public class IntroCutScene extends CutSceneState {

    public IntroCutScene(AudioHandler audioHandler){
        super (audioHandler);
        setBackgroundImages(new String[]{
                "/image/intro/intro1.png",
                "/image/intro/intro3.png",
                "/image/intro/intro3.png",
                "/image/intro/intro2.png",
        });
        setDialogue(new String[]{
                "It's been long enough.",
                "After years of deforestation, pollution, and the murder of my people,",
                "It's finally time for vengeance.",
                "Even if it costs me my life, my sacrifice will not be in vain."
        });
    }

    @Override
    public void update(float dt){
        if (isFinished()) {
            stopAudio();
            StateManager.setCurrentStateIndex(StateManager.GAME_STATE_INDEX);
        }
    }
}
