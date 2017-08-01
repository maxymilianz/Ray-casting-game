package com.company;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    int resX, resY;

    public Menu(int resX, int resY) {
        this.resX = resX;
        this.resY = resY;
    }

    public void paint(Graphics g) {
        g.drawImage(Textures.getSprites().get(Sprite.Sprites.MENU_BG).getImage(), 0, 0, resX, resY, null);
    }
}
