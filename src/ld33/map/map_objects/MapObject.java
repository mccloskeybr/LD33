package ld33.map.map_objects;

import ld33.map.map_objects.entity.Player;

/**
 * Created by mccloskeybr on 3/21/16.
 */
public class MapObject {

    private int id;
    private double x;

    public MapObject(int id, double x) {
        this.id = id;
        this.x = x;
    }

    public double getX(){
        return x;
    }

    public int getId(){
        return id;
    }

    public double getDistance(Player player){

        double distance = x - player.getX();

        if (distance > 0)
            return distance;

        return -1;

    }

    public void setX(double x) {
        this.x = x;
    }

}
