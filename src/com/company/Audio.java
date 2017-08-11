package com.company;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;

public class Audio {
    enum Sound {
        MENU, SWORD, STEPS, FAST_STEPS, BREATHE, BG,
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

    private static Clip changeSpeed(boolean loop, double ratio, AudioInputStream ais) throws Exception {
        AudioFormat af = ais.getFormat();
        int frameSize = af.getFrameSize();
        byte[] b = new byte[(int) Math.pow(2, 16)];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int read = 1; read > -1;) {
            read = ais.read(b);

            if (read > 0)
                baos.write(b, 0, read);
        }

        byte[] b0 = baos.toByteArray();
        byte[] b1 = new byte[(int) (b0.length / ratio)];

        for (int i = 0; i < b1.length / frameSize; i++)
            for (int j = 0; j < frameSize; j++)
                b1[i * frameSize + j] = b0[(int) (i * frameSize * ratio + j)];

        ByteArrayInputStream bais = new ByteArrayInputStream(b1);
        AudioInputStream ais1 = new AudioInputStream(bais, af, b1.length);
        Clip c = AudioSystem.getClip();
        c.open(ais1);
        if (loop)
            c.loop(Clip.LOOP_CONTINUOUSLY);
        c.stop();
        return c;
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

        clips.put(Sound.FAST_STEPS, changeSpeed(true, 2, AudioSystem.getAudioInputStream(new File("res/audio/steps.wav"))));

        stream = AudioSystem.getAudioInputStream(new File("res/audio/breathing.wav"));
        clip = AudioSystem.getClip();
        clip.open(stream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.stop();
        clips.put(Sound.BREATHE, clip);

        stream = AudioSystem.getAudioInputStream(new File("res/audio/bg.wav"));
        clip = AudioSystem.getClip();
        clip.open(stream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.stop();
        clips.put(Sound.BG, clip);
    }
}
