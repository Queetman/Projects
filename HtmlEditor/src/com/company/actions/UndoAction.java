package com.company.actions;

import com.company.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

//Класс отмены действия
public class UndoAction extends AbstractAction {
    private View view;

    public UndoAction(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.undo();

    }
}