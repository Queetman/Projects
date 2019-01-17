package com.company.controller;

import com.company.model.Model;
import com.company.view.EditUserView;
import com.company.view.UsersView;

// "Этот класс будет получать запрос от клиента, оповещать Модель об этом, а Модель будет обновлять ModelData-у.
public class Controller {

    private Model model;
    private UsersView usersView;
    private EditUserView editUserView;

    //methods;
    public void onShowAllUsers() {
        //обратиться к модели и инициировать загрузку юзеров
        model.loadUsers();
        //Вью обновляется контроллером.
        usersView.refresh(model.getModelData());
    }

    public void onShowAllDeletedUsers() {
        model.loadDeletedUsers();
        usersView.refresh(model.getModelData());
    }

    public void onOpenUserEditForm(long userId) {
        model.loadUserById(userId);
        editUserView.refresh(model.getModelData());
    }

    public void onUserDelete(long id) {
        model.deleteUserById(id);
        usersView.refresh(model.getModelData());
    }

    public void onUserChange(String name, long id, int level) {
        model.changeUserData(name, id, level);
        usersView.refresh(model.getModelData());
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setUsersView(UsersView usersView) {
        this.usersView = usersView;
    }

    public void setEditUserView(EditUserView editUserView) {
        this.editUserView = editUserView;
    }
}
