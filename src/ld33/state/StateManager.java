package ld33.state;

import ld33.state.cut_scenes.AboutCutScene;
import ld33.state.cut_scenes.IntroCutScene;

/**
 * Manages / manipulates the current state, basically how main game loop interacts with the current state
 */
public class StateManager {

    private static State currentState;
    private static int currentStateIndex;
    public static final int MENU_STATE_INDEX = 0, ABOUT_INDEX = -1, CUTSCENE_INDEX = 1, GAME_STATE_INDEX = 2, DEATH_INDEX = 3;

    private static boolean isPaused;

    public static State getCurrentState(){
        return currentState;
    }

    public static boolean isPaused(){
        return isPaused;
    }

    public static void resetGame(){
        currentStateIndex = MENU_STATE_INDEX;
        loadState();
    }

    public static void setCurrentStateIndex(int currentStateIndex1){
        currentStateIndex = currentStateIndex1;
        loadState();
    }

    public static void setPaused(boolean b){
        isPaused = b;
    }

    private static void loadState(){
        if (currentStateIndex == MENU_STATE_INDEX){
            currentState = new MenuState();
        } else if (currentStateIndex == CUTSCENE_INDEX) {
            currentState = new IntroCutScene(MenuState.getAudioHandler());
        } else if (currentStateIndex == GAME_STATE_INDEX){
            currentState = new GameState();
        } else if (currentStateIndex == ABOUT_INDEX){
            currentState = new AboutCutScene(MenuState.getAudioHandler());
        } else if (currentStateIndex == DEATH_INDEX){
            currentState = new DeathMenu();
        }
    }

}
