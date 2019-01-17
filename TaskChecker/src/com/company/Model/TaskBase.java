package com.company.Model;


import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Макс on 30.05.2017.
 */
public class TaskBase {


    //Соединение к БД
    private static Connection connection;
    private static Statement stmt;//состояние подключения годится для простых запросов.
    private static PreparedStatement ps; // подготовленный запрос (более надежный вид запроса чем statement


    //Соединение с БД
    public static void connect() throws SQLException {

        connection = DriverManager.getConnection("jdbc:sqlite:Tasks.db");// на сайтах разработчиков есть данные по драйверам билиботеки
        //если указать имя не существующей базы, тогда будет создана новая база, но она будет пуста. Надо в начале ее заполнить.
        //как подключить итд
        stmt = connection.createStatement();

        connection.setAutoCommit(true);//автоматическое получение к  БД
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Добавить строку
    public static void insertTask(String task, int day, int month, int year) throws SQLException {

        try {
            connect();
            System.out.println(stmt.executeUpdate("INSERT INTO Tasks (task, day, month, year) Values ('" + task + "', " + day + "," + month + "," + year + ");"));// подготовка запроса
        } catch (SQLException e) {
            System.out.println("Проблемы при работе");
        } finally {//надо закрывать
            disconnect();
        }
        //На выходе печатает кол-во измененных строк и изменяет их
    }

    //удалить задание
    public static void deleteTask(String task, int day, int month, int year) throws SQLException {

        System.out.println(stmt.executeUpdate("DELETE FROM Tasks WHERE task = '" + task + "' AND  day = " + day + " AND  month = " + month + " AND  year = " + year + ";"));
    }

    public static void updateTask(String task, int day, int month, int year) throws SQLException {
        deleteTask(task, day, month, year);
        System.out.println(stmt.executeUpdate("INSERT INTO Tasks (task, day, month, year) Values ('" + task + "', " + day + "," + month + "," + year + ");"));// подготовка запроса
        //На выходе печатает кол-во измененных строк и изменяет их
    }

    public static String[][] getTasks() throws SQLException {
        connect();
// получение кол-ва столбцов

        //Получение кол- ва рядов
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM Tasks");
        int rowCount = 0;

        while (rsCount.next()) {
            rowCount = rsCount.getInt(1);
        }

        //  Получение набора заданий.
        ResultSet rs = stmt.executeQuery("SELECT * FROM Tasks;");


        String[][] tasks = new String[rowCount][3];
        int i = 0;

//Получаем список значений (используется в начале)
        while (rs.next()) {

            String task = rs.getString(1);
            String date = rs.getInt(2) + "." + rs.getInt(3) + "." + rs.getInt(4);
            String isDone = DateMeth.isDone(date);

            tasks[i][0] = task;
            tasks[i][1] = date;
            tasks[i][2] = isDone;
            i++;
        }
        return tasks;
    }

    public static void main(String[] args) {

        try {
            connect();
            String[][] arr = getTasks();

            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    System.out.print(arr[i][j] + " ");
                }
                System.out.println();
            }
            deleteTask("one", 1, 1, 1);
        } catch (SQLException e) {
            System.out.println("Проблемы при работе");
            e.printStackTrace();
        } finally {//надо закрывать
            disconnect();
        }
    }
}
