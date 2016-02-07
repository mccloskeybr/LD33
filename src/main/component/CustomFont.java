package main.component;

import java.awt.*;
import java.io.File;
import java.io.InputStream;

/**
 * Registers fonts n stuff
 */
public class CustomFont {

    public void registerFont(String path){

        InputStream is = getClass().getResourceAsStream(path);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try{
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
