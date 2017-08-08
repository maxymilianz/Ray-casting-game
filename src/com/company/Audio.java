package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Hashtable;

public class Audio {
    enum Sound {
        MENU, SWORD, STEPS, BREATHE,
    }

    private static Hashtable<Sound, Clip> clips = new Hashtable<>();

    static void resetAndStart(Sound s) {
        Clip c = clips.get(s);
        c.setMicrosecondPosition(0);
        c.start();
    }

    static void start(Sound s) {
        clips.get(s).start();
    }

    static void stop(Sound s) {
        clips.get(s).stop();
    }

    static void init() throws Exception {
        AudioInputStream stream = AudioSystem.getAudioInputStream(new File("res/audio/intro.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.stop();        // after setting clip loop continuously I have to stop it
        clips.put(Sound.MENU, clip);

        stream = AudioSystem.getAudioInputStream(new File("res/audio/sword.wav"));
        clip = AudioSystem.getClip();
        clip.open(stream);
        clips.put(Sound.SWORD, clip);

        stream = AudioSystem.getAudioInputStream(new File("res/audio/steps.wav"));
        clip = AudioSystem.getClip();
        clip.open(stream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.stop();
        clips.put(Sound.STEPS, clip);

        stream = AudioSystem.getAudioInputStream(new File("res/audio/breathing.wav"));
        clip = AudioSystem.getClip();
        clip.open(stream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.stop();
        clips.put(Sound.BREATHE, clip);
    }
}
