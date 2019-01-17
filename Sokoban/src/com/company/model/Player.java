package com.company.model;

import java.awt.*;

public class Player extends CollisionObject implements Movable
{

    public Player(int x, int y)
    {
        super(x, y);
    }

    @Override
    public void draw(Graphics graphics)
    {
        graphics.setColor(Color.blue);

        int getLeftUpCornerX = getX() - getWidth() / 2;
        int getLeftUpCornerY = getY() - getHeight() / 2;

        graphics.drawRect(getLeftUpCornerX, getLeftUpCornerY, getWidth(), getHeight());
        graphics.fillRect(getLeftUpCornerX, getLeftUpCornerY, getWidth(), getHeight());

    }

    @Override
    public void move(int x, int y)
    {
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);
    }
}




