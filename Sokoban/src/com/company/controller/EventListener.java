package com.company.controller;

import com.company.model.Direction;

//Давай создадим интерфейс слушателя событий EventListener
public interface EventListener
{
    // передвинуть объект в определенном направлении.
    public void move(Direction direction);

    //  начать заново текущий уровень.
    public void   restart();

    // начать следующий уровень.
    public void startNextLevel();

    // уровень с номером level завершён.
    public void levelCompleted(int level);

}