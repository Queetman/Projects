package com.company.View;


import com.company.Model.DateMeth;
import com.company.Model.TaskBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class Table extends JFrame {

    // Модель данных таблицы
    private DefaultTableModel tableModel;
    private JTable table1;
    // Данные для таблиц

    //Поля для ввода
    private JTextField taskField;
    private JTextField dayField;
    private JTextField monthField;
    private JTextField yearField;

    // Заголовки столбцов
    private Object[] columnsHeader = new String[]{"Задание", "Срок", "Состояние"};

    public Table() throws IOException, SQLException {

        //Название
        JFrame frame = new JFrame("Чекер заданий");
        //Компановщик
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Создание стандартной модели
        tableModel = new DefaultTableModel();

//Создание нового текстового поля для добавления задания
        taskField = new JTextField(20);
        dayField = new JTextField(2);
        monthField = new JTextField((3));
        yearField = new JTextField(4);

//Введение записей, которые будут в текстовых полях по умолчанию.
        taskField.setText("Задание");

        // Определение стоблцов
        tableModel.setColumnIdentifiers(columnsHeader);
        // Наполнение модели данными

        // Создание таблицы на основании модели данных
        table1 = new JTable(tableModel);

        //Заполнение данными при запуске программы. пример запонения ниже:
        //  tableModel.addRow(new String[]{"as", "2", "5"}); т.е. запоняем построчно.
        for (int i = 0; i < TaskBase.getTasks().length; i++) {
            tableModel.addRow(new String[]{TaskBase.getTasks()[i][0], TaskBase.getTasks()[i][1], TaskBase.getTasks()[i][2]});
        }

        // Создание кнопки добавления строки таблицы
        JButton add = new JButton("Добавить");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //Получение входных данных
                String task = taskField.getText();
                int day = DateMeth.checkDay(dayField.getText(), monthField.getText());
                int month = DateMeth.checkMonth(monthField.getText());
                int year = DateMeth.checkYear(yearField.getText());

                String dateStr = day + "." + month + "." + year;

                try {
                    //Вставка в эл. базу
                    TaskBase.insertTask(task, day, month, year);
                    tableModel.addRow(new String[]{task, dateStr, DateMeth.isDone(dateStr)});

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Создание кнопки удаления строки таблицы
        JButton remove = new JButton("Удалить");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int rowNum = table1.getSelectedRow();

                String task = (String) table1.getValueAt(rowNum, 0);
                String date = (String) table1.getValueAt(rowNum, 1);

                tableModel.removeRow(rowNum);

                int day = Integer.parseInt(date.split("\\.")[0]);
                int month = Integer.parseInt(date.split("\\.")[1]);
                int year = Integer.parseInt(date.split("\\.")[2]);

                try {
                    TaskBase.deleteTask(task, day, month, year);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });

        //что делает кнопка: выносит в поля корректируемое задание. Это задание автоматом удаляется.
        JButton edit = new JButton("Изменить");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Получение входных данных
                int rowNum = table1.getSelectedRow();
                String task = (String) table1.getValueAt(rowNum, 0);
                String date = (String) table1.getValueAt(rowNum, 1);
                System.out.println(date);

                int day = Integer.parseInt(date.split("\\.")[0]);
                int month = Integer.parseInt(date.split("\\.")[1]);
                int year = Integer.parseInt(date.split("\\.")[2]);

                try {
                    TaskBase.deleteTask(task, day, month, year);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                //Вывод задание на экран
                taskField.setText(task);
                dayField.setText(date.split("\\.")[0]);
                monthField.setText(date.split("\\.")[1]);
                yearField.setText(date.split("\\.")[2]);

                // и удаление текущего задания.
                tableModel.removeRow(rowNum);
            }
        });

        // Формирование интерфейса
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table1));
        getContentPane().add(contents);
        JPanel buttons = new JPanel();

//Добавление кнопок
        buttons.add(add);
        buttons.add(remove);
        buttons.add(edit);

        frame.add(new JLabel("Введите задание и срок дд,мм,гггг"));

        JPanel fields = new JPanel();
        fields.add(taskField);
        fields.add(dayField);
        fields.add(monthField);
        fields.add(yearField);

        frame.add(fields);
        frame.add(buttons);
        frame.add(buttons);
        frame.add(contents);

        // Вывод окна на экран
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException, SQLException {
        new Table();
    }
}

