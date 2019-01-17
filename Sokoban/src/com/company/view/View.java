package com.company.view;

import com.company.controller.Controller;
import com.company.controller.EventListener;
import com.company.model.GameObjects;

import javax.swing.*;

public class View extends JFrame {
    private Controller controller;
    private Field field;

    public View(Controller controller) {
        this.controller = controller;
    }

    // будет обновлять представление(перерисовывать поле).
    public void update(){
        field.repaint();
    }

    public void setEventListener(EventListener eventListener){
        this.field.setEventListener(eventListener);
    }

    public void init() {
        field = new Field(this);
        add(field);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("Сокобан");
        setVisible(true);
    }

    public GameObjects getGameObjects()
    {
        return controller.getGameObjects();
    }

    public void completed(int level)
    {
        update();
        JOptionPane.showMessageDialog(null, level + "Completed", "Level", JOptionPane.INFORMATION_MESSAGE);
        controller.startNextLevel();
    }
}
