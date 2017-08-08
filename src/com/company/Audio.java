package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Hashtable;

public class Audio {
    enum Sound {
        MENU,
    }

    private static Hashtable<Sound, Clip> clips = new Hashtable<>();

    static void start(Sound s) {
        Clip c = clips.get(s);
        c.setMicrosecondPosition(0);
        c.start();
    }

    static void stop(Sound s) {
        clips.get(s).stop();
    }

    static void init() throws Exception {
        AudioInputStream stream = AudioSystem.getAudioInputStream(new File("res/audio/intro.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        clips.put(Sound.MENU, clip);
    }
}
