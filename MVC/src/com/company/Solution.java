package com.company;

import com.company.controller.Controller;
import com.company.model.MainModel;
import com.company.model.Model;
import com.company.view.EditUserView;
import com.company.view.UsersView;

public class Solution {
    //Класс Solution будет эмулятором пользователя
    public static void main(String[] args) {
        Model model = new MainModel();
        UsersView usersView = new UsersView();
        EditUserView editUserView = new EditUserView();

        Controller controller = new Controller();

        usersView.setController(controller);
        editUserView.setController(controller);

        controller.setModel(model);
        controller.setUsersView(usersView);
        controller.setEditUserView(editUserView);

        usersView.fireEventShowAllUsers();
        usersView.fireEventShowDeletedUsers();
        usersView.fireEventOpenUserEditForm(126);

        editUserView.fireEventUserDeleted(124);

        editUserView.fireEventUserChanged("IPetrov", 125, 15);

    }
}
