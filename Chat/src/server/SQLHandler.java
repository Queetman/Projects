package server;

import java.sql.*;

//класс для работы с SQL
public class SQLHandler {
    private static Connection connection;
    private static PreparedStatement statement;

    //приходит имя базы данных
    public static void connect(String dbName) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");//название базы org.sqlite. JDBC- это драйвер ( решение для работы со всеми БД).
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);//иницализация драйвера с которой работает база
    }
    //метод для проверки аутентификации
    public static String getNick(String login, String password) throws SQLException {
        Statement statement = connection.createStatement();

        //делается запрос
        ResultSet rs = statement.executeQuery(
                "select nick from \"users_table\" where login = \"" + login
                        + "\" and password = \"" + password + "\"");//select nick from "users_table" - выбрать ник из usersTable
        // where login = "" + login  где логин= логину,пришедшему на вход
        //"\" and password = \"" + password + "\""); и пароль =паролю, пришедшему на вход
        String nick = null;
        while (rs.next()) {//если логин и пароль соответствует
            nick = rs.getString(1);//получаем строку, если нет - null
        }
        return nick;
    }
}
