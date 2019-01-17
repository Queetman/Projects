package com.company.model;

import java.awt.*;

public class Wall extends CollisionObject
{
    public Wall(int x, int y)
    {
        super(x, y);
    }

    @Override
    public void draw(Graphics graphics)
    {
        graphics.setColor(Color.DARK_GRAY);

        int getLeftUpCornerX=getX()-getWidth()/2;
        int getLeftUpCornerY=getY()-getHeight()/2;

        graphics.drawRect(getLeftUpCornerX, getLeftUpCornerY, getWidth(), getHeight());
        graphics.fillRect( getLeftUpCornerX, getLeftUpCornerY, getWidth(), getHeight());

    }
}
