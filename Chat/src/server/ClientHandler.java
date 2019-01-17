package server;

import exceptions.AuthFailException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

//отвечает за общение с каждым конкретным клиентом
public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    //потоки ввода/вывода
    private DataOutputStream out;
    private DataInputStream in;

    private static int clientsCount = 0;
    private String clientName;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            clientsCount++;
            clientName = "client" + clientsCount;

            System.out.println("Client \"" + clientName + "\" ready!");
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        waitForAuth();
        waitForMessage();
    }

    //метод для принятия сообщений
    private void waitForMessage() {
        while (true) {
            String message = null;
            try {
                message = in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(clientName + ": " + message);

            new Thread(new MessagesSender(message, clientName, server)).start();
        }
    }

    private void waitForAuth() {
        while (true) {
            String message = null;
            try {
                message = in.readUTF();//получение входящего сообщения
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (isAuthOk(message)) {//если аутентификация проходит нормально
                    out.writeUTF("signIn_success");//отправление сообщения клиенту
                    break;
                } else {
                    out.writeUTF("signIn_fail");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(clientName + " auth ok and is ready for chat!");
    }

    //проверка на приход видасообщения. в данном случае идентификация
    private boolean isAuthOk(String message) {
        System.out.println(clientName + "[NO AUTH]: " + message);
//если не ноль, тогда строку делим на логин и пароль, удаляя разделитель"___"
        if (message != null) {// приходит сообщение: "Auth message from " + clientName). clientName - логин___пароль
            String[] parsedMessage = message.split("___");

            //если длина сообщения равна 3 "Auth message from ", "логин", "пароль"
            if (parsedMessage.length == 3) {
                try {
                    processAuthMessage(parsedMessage);
                    return true;
                } catch (AuthFailException e) {//если ловится исключение из класса Server метода addClient
                    return false;
                }
            }
        }
        return false;
    }

    //метод для получения данных
    private void processAuthMessage(String[] parsedMessage) throws AuthFailException {
        if (parsedMessage[0].equals("auth")) {
            System.out.println("Auth message from " + clientName);
            String login = parsedMessage[1];//получение логина
            String password = parsedMessage[2];//получение пароля

            String nick = null;
            try {
                //получение ника
                nick = SQLHandler.getNick(login, password);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new AuthFailException();
            }

            if (nick != null) {//добавление клиента к серверу (элемента этого класса и ника)
                server.addClient(this, nick);
                return;
            }
            throw new AuthFailException();//если ник мы не получили тоже пробрасываем
        }
    }

    public DataOutputStream getOut() {
        return out;
    }

    public String getClientName() {
        return clientName;
    }

    public void setNick(String nick) {
        this.clientName = nick;
    }
}
