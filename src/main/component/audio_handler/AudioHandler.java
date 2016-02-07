package main.component.audio_handler;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * plays wav files
 */
public class AudioHandler {

    public static boolean isMuted;

    //volume specified
    public void playAudio(String path, float volume){
        if (!isMuted) { //only play audio when it isnt muted
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource(path));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Clip clip;

    //loops with volume specified
    public void loopAudio(String path, float volume){
        if (!isMuted) { //only play audio when it isnt muted
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource(path));

                clip = AudioSystem.getClip();
                clip.open(ais);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.loop(999);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopAudio(){
        clip.stop();
    }

}
