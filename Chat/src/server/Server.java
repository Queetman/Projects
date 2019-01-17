package server;

import exceptions.AuthFailException;
import filters.ChatFilter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private static volatile Server instance;

    private List<ClientHandler> clients;

    private List<ChatFilter> filters;//списрок фильтров

    private ServerSocket serverSocket = null;

    //метод для добавления фильтров
    public void addFilter(ChatFilter filter) {
        filters.add(filter);
        System.out.println("Filter is added!");
    }

    public synchronized void addClient(ClientHandler clientHandler, String nick) throws AuthFailException {
        for (ClientHandler client : clients) {
            //проверка на одинаковый ник
            if (client.getClientName().equals(nick)) {
                System.out.println("Client with nick " + nick + " is already exists!");
                throw new AuthFailException();//ошибка аутентификации пробрасывается исключение до класса clientHandler и метода isAuhOk
            }
        }
        clientHandler.setNick(nick);//если не одинаковый ник, добавляем в клиенты

        clients.add(clientHandler);
        System.out.println(clientHandler.getClientName() + " is added to subscribers list!");
    }

    //конструктор принимает серврерный порт и имя базы данных
    public Server(int serverPort, String dbName) {
        System.out.println("Server init start.");
        clients = new LinkedList<>();//список клиентов инициализируется в конструкторе
        //linkedList используется когда в большинстве случаев работаем в конец списков. плюс перебор быстрее элементов при отправке сообщений клиентам.
        // Он в данном случае быстрее ArrayList.
        filters = new ArrayList<>();//инициализация фильтра

        try {
            serverSocket = new ServerSocket(serverPort);//инициализация серверного сокета
            System.out.println("Server socket init OK.");

            SQLHandler.connect(dbName);//подключение БД
            System.out.println("Server DB init OK.");

            System.out.println("Server ready and waiting for clients...");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void waitForClient() {
        Socket socket = null;
        try {
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client connected.");
                ClientHandler client = new ClientHandler(socket, this);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
                System.out.println("Server closed.");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void newMessageFromClient(String message, String clientName) {
        for (ChatFilter filter : filters) {
            message = filter.filter(message);//фильтрация сообщений
        }
        for (ClientHandler client : clients) {
            try {
                client.getOut().writeUTF(clientName + ": " + message);
                client.getOut().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}