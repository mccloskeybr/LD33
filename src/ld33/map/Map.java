package ld33.map;

import ld33.component.Window;
import ld33.map.map_objects.House;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * handles all the tiles, from creating to rendering
 */

public class Map {

    private ArrayList<House> buildings;
    private int[][] idMap;

    public static int camx, camy;

    public Map(){
        buildings = new ArrayList<>();
    }

    public ArrayList<House> getBuildings(){
        return buildings;
    }

    public void spawnHouse(int x){
        buildings.add(new House(x, Window.WINDOW_HEIGHT / 2 + 100));
    }

    //sets camera position around the player (tween gives it some floatiness; it isn't rigid)
    public void setCameraPos(int x, int y, boolean shake){

        float tween;
        int realx = x;

        if (shake){
            realx = x + new Random().nextInt(300) - 150;
            tween = 0.7f;
        } else {
            tween = 0.3f;
        }

        camx += (realx + 300 - camx) * tween;
        camy += (y - camy) * tween;
    }

    //renders map
    public void render(Graphics g){
        for (int i = 0; i < buildings.size(); i++){
            buildings.get(i).render(g, (int) buildings.get(i).getX() - camx + Window.WINDOW_WIDTH / 2);
        }
    }


}
