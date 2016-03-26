package ai;

import ld33.component.*;
import ld33.map.map_objects.MapObject;
import ld33.map.map_objects.entity.Archer;
import ld33.state.GameState;
import ld33.state.StateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mccloskeybr on 3/18/16.
 */
public class AI_Main extends JPanel{

    private int i_currentBrain;
    private int i_parent1;
    private int i_parent2;
    private NeuralNetwork[] brains;
    private int[] reward;
    private int trialNum;
    private ld33.component.Window window;

    private boolean right, q, e, space;

    private double timer;

    public AI_Main(){

        window = new ld33.component.Window(200, ld33.component.Window.WINDOW_HEIGHT);
        window.draw(this);

        int numBrains = 10;

        brains = new NeuralNetwork[numBrains];
        reward = new int[numBrains];

        for (int i = 0; i < brains.length; i++)
            brains[i] = new NeuralNetwork();

        i_currentBrain = 0;

        i_parent1 = -1;
        i_parent2 = -1;

    }

    // returns index of new brain
    private int replaceWorst() {

        // find worst

        int best = 0, secondBest = 1, worst = 2;

        for (int i = 0; i < brains.length; i++) {

            if (reward[i] > reward[best] && i != secondBest)
                best = i;
            else if (reward[i] > reward[secondBest] && i != best)
                secondBest = i;
            else if (reward[i] <= reward[worst])
                worst = i;

        }

        Random random = new Random();
        if (Math.random() < 0.25)
            best = random.nextInt(reward.length);
        if (Math.random() < 0.25)
            secondBest = random.nextInt(reward.length);

        i_parent1 = best;
        i_parent2 = secondBest;

        // reproduce best brains

        NeuralNetwork
                parent1 = brains[best],
                parent2 = brains[secondBest];

        double[] childWeights = new double[parent1.getWeights().length];

        for (int i = 0; i < childWeights.length; i++) {

            if (Math.random() < 0.5)
                childWeights[i] = parent1.getWeights()[i];
            else
                childWeights[i] = parent2.getWeights()[i];

            // mutation
            if (Math.random() < 0.2)
                childWeights[i] = Math.random() - Math.random();

        }

        //replace worst brain

        brains[worst] = new NeuralNetwork(childWeights);
        reward[worst] = 0;

        return worst;
    }

    public void update(GameState state, double delta){

        timer += delta;

        reward[i_currentBrain] = GameState.getScore();

        if (timer > 200 || state.getPlayer().isDead()) {

            timer = 0;
            trialNum++;

            i_currentBrain = replaceWorst();
            StateManager.setCurrentStateIndex(StateManager.GAME_STATE_INDEX);

        } else {

            ArrayList<MapObject> allArrows = new ArrayList<>();

            for (Archer archer: state.getEnemies())
                allArrows.addAll(archer.getArrows());

            MapObject closestObject;
            double input[] = new double[2];

            if (allArrows.size() != 0) {
                closestObject = allArrows.get(0);

                for (MapObject object : allArrows) {
                    if (object.getDistance(state.getPlayer()) != -1 && object.getDistance(state.getPlayer()) < closestObject.getDistance(state.getPlayer()))
                        closestObject = object;
                }

                if (closestObject.getDistance(state.getPlayer()) >= 0 && closestObject.getDistance(state.getPlayer()) < 1000) {
                    input[0] = 1.0;
                }
            }

            if (state.getMap().getBuildings().size() != 0) {
                closestObject = state.getMap().getBuildings().get(0);

                for (MapObject object : state.getMap().getBuildings()) {
                    if (object.getDistance(state.getPlayer()) != -1 && object.getDistance(state.getPlayer()) < closestObject.getDistance(state.getPlayer()))
                        closestObject = object;
                }

                if (closestObject.getDistance(state.getPlayer()) >= 0 && closestObject.getDistance(state.getPlayer()) < 500) {
                    input[1] = 1.0;
                }
            }

            double[] output = brains[i_currentBrain].forward(input);

            if (!q && output[0] > 0.5) {
                state.keyPressed(KeyEvent.VK_Q);
                q = true;
            }
            else if (q && output[0] <= 0.5) {
                state.keyReleased(KeyEvent.VK_Q);
                q = false;
            }

            if (!e && output[1] > 0.5) {
                state.keyPressed(KeyEvent.VK_E);
                e = true;
            }
            else if (e && output[1] <= 0.5) {
                state.keyReleased(KeyEvent.VK_E);
                e = false;
            }

            if (!space && output[2] > 0.5) {
                state.keyPressed(KeyEvent.VK_SPACE);
                space = true;
            }
            else if (space && output[2] <= 0.5) {
                state.keyReleased(KeyEvent.VK_SPACE);
                space = false;
            }

            if (!right && output[3] > 0.5) {
                state.keyPressed(KeyEvent.VK_RIGHT);
                right = true;
            }
            else if (right && output[3] <= 0.5) {
                state.keyReleased(KeyEvent.VK_RIGHT);
                right = false;
            }

            if (state.getPlayer().isMoving() && !state.getPlayer().isBlocking() && !state.getPlayer().isStriking())
                timer = 0;

        }

        repaint();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.BLACK);

        for (int i = 0; i < reward.length; i++)
            g.drawString(i + ": " + reward[i], 5, 20 + 20 * i);

        if (i_parent1 != -1) {
            g.drawString(" - p1", 80, 20 + 20 * i_parent1);
            g.drawString(" - p2", 80, 20 + 20 * i_parent2);
            g.drawString(" - c", 80, 20 + 20 * i_currentBrain);
        }

        g.drawString("NUM TRIALS : " + trialNum, 5, 20 + 20 * reward.length);

        if (q)
            g.setColor(Color.RED);
        g.drawString("PUNCH", 5, 20 + 20 * (reward.length + 1));

        g.setColor(Color.BLACK);
        if (e)
            g.setColor(Color.RED);
        g.drawString("BLOCK", 5, 20 + 20 * (reward.length + 2));

        g.setColor(Color.BLACK);
        if (space)
            g.setColor(Color.RED);
        g.drawString("SPACE", 5, 20 + 20 * (reward.length + 3));

        g.setColor(Color.BLACK);
        if (right)
            g.setColor(Color.RED);
        g.drawString("RIGHT", 5, 20 + 20 * (reward.length + 4));

    }

}
