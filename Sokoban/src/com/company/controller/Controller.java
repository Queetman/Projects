package com.company.controller;

import com.company.model.Direction;
import com.company.model.GameObjects;
import com.company.model.Model;
import com.company.view.View;

import javax.swing.*;

public class Controller implements EventListener
{

    private View view;
    private Model model;

    public static void main(String[] args)
    {
        Controller controller=new Controller();
    }


    public Controller() {
        model = new Model();
        model.restart();
        model.setEventListener(this);
        view = new View(this);
        view.init();
        view.setEventListener(this);

    }


    //  он должензапросить игровые объекты у модели и вернуть их
    public GameObjects getGameObjects(){

        return model.getGameObjects();

    }

    // должен вызывать move(Direction direction) у моделии update() у представления.
    @Override
    public void move(Direction direction)
    {
        model.move(direction);
        view.update();

    }
    // должен перезапускать модель и обновлять представление.
    @Override
    public void restart()
    {
        model.restart();
        view.update();

    }

    //должен запускать у модели новый уровень и обновлять представление.
    @Override
    public void startNextLevel()
    {
        model.startNextLevel();
        view.update();


    }

    @Override
    public void levelCompleted(int level)
    {
        view.completed(level);

    }
}

