package com.company.model;

import java.awt.*;

public class Box extends CollisionObject implements Movable {

    public Box(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.green);

        int getLeftUpCornerX = getX() - getWidth() / 2;
        int getLeftUpCornerY = getY() - getHeight() / 2;

        graphics.drawRect(getLeftUpCornerX, getLeftUpCornerY, getWidth(), getHeight());
        graphics.fillRect(getLeftUpCornerX, getLeftUpCornerY, getWidth(), getHeight());

    }

    @Override
    public void move(int x, int y) {
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);
    }
}




