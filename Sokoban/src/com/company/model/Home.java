package com.company.model;

import java.awt.*;

//этот класс будет отвечать за места на игровом поле (дома) в которые нужно сдвинуть все ящики.
public class Home extends GameObject
{
    //  Высота и ширинадома должна быть равна 2.

    public Home(int x, int y)
    {
        super(x, y);
        setWidth(2);
        setHeight(2);
    }

    @Override
    public void draw(Graphics graphics)
    {
        graphics.setColor(Color.black);

        int getLeftUpCornerX=getX()-getWidth()/2;
        int getLeftUpCornerY=getY()-getHeight()/2;

        graphics.drawRect(getLeftUpCornerX, getLeftUpCornerY, getWidth(), getHeight());
        graphics.fillRect( getLeftUpCornerX, getLeftUpCornerY, getWidth(), getHeight());
    }
}