package ld33.state;

import ai.AI_Main;
import ld33.component.Window;
import ld33.component.audio_handler.AudioHandler;
import ld33.component.image_handler.ImageLoader;
import ld33.component.visual_handler.Background;
import ld33.map.Map;
import ld33.map.map_objects.Arrow;
import ld33.map.map_objects.entity.Archer;
import ld33.map.map_objects.entity.Civilian;
import ld33.map.map_objects.entity.Player;

import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Game state, everything that happens in game is managed here
 */
public class GameState implements State {

    private static AI_Main ai;

    private AudioHandler audioHandler;

    private ArrayList<Archer> enemies;
    private ArrayList<Civilian> civilians;
    private Player player;
    private Map map;

    private BufferedImage score_image, help_image;
    private Font score_font;
    private Color score_color;
    private static int score;

    private BufferedImage hp_image;
    private Font hp_font;
    private Color hp_color;

    private Background bg;

    private boolean playShake;

    public GameState(){
        init();
    }

    public Player getPlayer(){
        return player;
    }

    public Map getMap(){
        return map;
    }

    public ArrayList<Archer> getEnemies(){
        return enemies;
    }

    public static void resetScore(){
        score = 0;
    }

    public static int getScore(){
        return score;
    }

    @Override
    public void init() {

        if (ai == null)
            ai = new AI_Main();

        ImageLoader imageLoader = new ImageLoader();

        help_image = imageLoader.loadImage("/image/hud/help.png");

        score_image = imageLoader.loadImage("/image/hud/score.png");
        score_color = new Color(93,0, 14);
        score_font = new Font("VCR OSD Mono", Font.PLAIN, 32);

        hp_image = imageLoader.loadImage("/image/hud/hp.png");
        hp_color = score_color;
        hp_font = score_font;

        bg = new Background();

        map = new Map();
        player = new Player(0,Window.WINDOW_HEIGHT);
        enemies = new ArrayList<>();
        civilians = new ArrayList<>();

        audioHandler = new AudioHandler();
        audioHandler.loopAudio("/sound/game_bg.wav", -20.0f);

        score = 0;

    }

    @Override
    public void update(float dt) {
        if (!StateManager.isPaused()) {

            ai.update(this, dt);

            if (player.isDead()){
                audioHandler.stopAudio();
                //StateManager.setCurrentStateIndex(StateManager.DEATH_INDEX);
            }

            player.update(dt);
            map.setCameraPos((int) player.getX(), (int) player.getY(), player.justLanded());
            if (player.justLanded() && playShake){ //ensures the slam sound effect isnt spammed
                audioHandler.playAudio("/sound/slam.wav", -10.0f);
                playShake = false;
            } else if (!player.justLanded()){
                playShake = true;
            }

            //updates and checks for civilians
            for (int i = 0; i < civilians.size(); i++){
                civilians.get(i).update(dt);
                //stepped on by player
                if (player.getX() + 150 > civilians.get(i).getX() &&
                        player.getX() < civilians.get(i).getX()){
                    civilians.remove(i);
                    audioHandler.playAudio("/sound/splat.wav", -20f);
                    i--;

                    score += 1;
                }
            }

            //updates and checks for archers
            for (int i = 0; i < enemies.size(); i++){
                //hit by player
                if (player.getX() + 384 > enemies.get(i).getX() && player.isStriking() &&
                        player.getX() < enemies.get(i).getX()){

                    System.out.println("PUNCH");
                    score += 50;

                    enemies.remove(i);
                    audioHandler.playAudio("/sound/splat.wav", -20f);
                    i--;
                    if (i < 0){
                        i = 0;
                        if (enemies.size() == 0){
                            break;
                        }
                    }
                    score += 10;
                }
                //if out of bounds
                if (enemies.get(i).getX() < player.getX() - 250){
                    enemies.remove(i);
                    i--;
                    if (i < 0){
                        i = 0;
                        if (enemies.size() == 0){
                            break;
                        }
                    }
                }
                enemies.get(i).update(dt);
                //arrow hits player
                for (int j = 0; j < enemies.get(i).getArrows().size(); j++){
                    if (player.getX() + 150 > enemies.get(i).getArrows().get(j).getX() &&
                            player.getX() < enemies.get(i).getArrows().get(j).getX()){
                        if (!player.isBlocking()) {
                            player.takeDamage(Arrow.DAMAGE);
                            new AudioHandler().playAudio("/sound/playerhit.wav", 0.0f);
                        } else {
                            System.out.println("BLOCK");
                            score += 10;
                        }
                        enemies.get(i).getArrows().remove(j);
                        j--;
                    }
                }
            }

            //buildings sink when you pound them
            for (int i = 0; i < map.getBuildings().size(); i++){
                if (!player.isStriking() && !player.justLanded()){
                    map.getBuildings().get(i).setTakenDamage(false);
                }

                //if out of bounds
                if (map.getBuildings().get(i).getX() < player.getX() - 2000){ //large number ensures that buildings wont disappear while sinking
                    map.getBuildings().remove(i);
                    i--;
                    if (i < 0){
                        i = 0;
                    }
                }
                //if pounding, deal damage
                if (player.getX() + 384 > map.getBuildings().get(i).getX() &&
                        player.getX() < map.getBuildings().get(i).getX()){
                    if (((player.justLanded() && !playShake) || player.isStriking()) && !map.getBuildings().get(i).isSinking()){

                        if (!map.getBuildings().get(i).hasTakenDamage()) {
                            map.getBuildings().get(i).takeDamage();
                            if (player.justLanded()){
                                map.getBuildings().get(i).takeDamage();
                            }
                            if (map.getBuildings().get(i).isSinking()) {
                                for (int j = 0; j < new Random().nextInt(3) + 2; j++) { //people run out of the buildings when it starts sinking
                                    civilians.add(new Civilian((int) map.getBuildings().get(i).getX() + new Random().nextInt(75) + 100, Window.WINDOW_HEIGHT - 70));
                                }
                            }
                        }

                    }
                }

                if (map.getBuildings().get(i).isSinking()){
                    //in case an archer was standing on the roof
                    for (int j = 0; j < enemies.size(); j++){
                        if (enemies.get(j).getX() < map.getBuildings().get(i).getX() + 192 &&
                                enemies.get(j).getX() > map.getBuildings().get(i).getX()){
                            enemies.remove(j);
                            score += 50;
                            break;
                        }
                    }
                    //update y position
                    map.getBuildings().get(i).sink(dt);
                    //remove when it gets too low
                    if (map.getBuildings().get(i).getY() > Window.WINDOW_HEIGHT){
                        map.getBuildings().remove(i);
                        i--;
                        score += 100;
                    }
                }
            }

            //loops background images and spawns houses/mobs when needed
            if (((int) player.getX() + 1) / (bg.getX() + 1) > 0){
                bg.updateX();

                //generates houses
                Random rand = new Random();
                int n = 2880;
                for (int i = 0; i < 6; i++) {
                    map.spawnHouse(n + (int) player.getX());

                    //spawns archers on top of houses
                    //more enemies spawn the higher your score is
                    int r;
                    if (score < 250){
                        r = 5;
                    } else if (score < 500){
                        r = 4;
                    } else if (score < 1000){
                        r = 3;
                    } else if (score < 2000){
                        r = 2;
                    } else {
                        r = 1;
                    }

                    if (rand.nextInt(r) == 0){
                        enemies.add(new Archer(n + (int) player.getX() + 100,  Window.WINDOW_HEIGHT / 2 + 100 - 32));
                    }
                    n += rand.nextInt(288) + 192;
                }

                //generates civilians
                n = rand.nextInt(2500 / 10) + 2880;
                for (int i = 0; i < 10; i++){
                    n += rand.nextInt(250);
                    civilians.add(new Civilian(n + (int) player.getX(), Window.WINDOW_HEIGHT - 70));
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);

        bg.render(g);
        player.render(g);
        map.render(g);
        for (int i = 0; i < enemies.size(); i++){
            enemies.get(i).render(g);
        }
        for (int i = 0; i < civilians.size(); i++){
            civilians.get(i).render(g);
        }

        g.setColor(new Color(58, 35, 5));
        g.fillRect(0, Window.WINDOW_HEIGHT - 40, Window.WINDOW_WIDTH, 20);

        g.drawImage(score_image, Window.WINDOW_WIDTH - 300, 50, score_image.getWidth() * 2, score_image.getHeight() * 2, null);
        g.setFont(score_font);
        g.setColor(score_color);
        g.drawString("" + score, Window.WINDOW_WIDTH - 135, 75);

        g.drawImage(hp_image, 120, 50, hp_image.getWidth() * 2, hp_image.getHeight() * 2, null);
        g.setFont(hp_font);
        g.setColor(hp_color);
        g.drawString("" + player.getHealth(), 200, 75);

        g.drawImage(help_image, -Map.camx + Window.WINDOW_WIDTH / 2 - 175, Window.WINDOW_HEIGHT / 2, help_image.getWidth() * 4, help_image.getHeight() * 4, null);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_RIGHT) {
            player.setMoving(true);
        } else if (k == KeyEvent.VK_SPACE) {
            player.jump();
        } else if (k == KeyEvent.VK_Q) {
            player.attack();
        } else if (k == KeyEvent.VK_E){
            player.setBlocking(true);
        } else if (k == KeyEvent.VK_P){
            StateManager.setPaused(!StateManager.isPaused());
        } else if (k == KeyEvent.VK_M){
            AudioHandler.isMuted = !AudioHandler.isMuted;
            if (AudioHandler.isMuted) {
                audioHandler.stopAudio();
            } else {
                audioHandler.loopAudio("/sound/game_bg.wav", -20.0f);
            }
        }
    }

    @Override
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_RIGHT) {
            player.setMoving(false);
        } else if (k == KeyEvent.VK_E){
            player.setBlocking(false);
        } else if (k == KeyEvent.VK_SPACE){
            player.stopJumping();
        }
    }
}
