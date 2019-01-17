package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientWindow extends JFrame {

    JPanel authPanel;
    private JTextField clientMsgElement;
    private JTextArea serverMsgElement;

    final String serverHost;
    final int serverPort;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public ClientWindow(String host, int port) {
        serverHost = host;
        serverPort = port;

        initConnection();
        initGUI();
        initServerListner();
    }

    private void initConnection() {
        try {
            socket = new Socket(serverHost, serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGUI() {
        setBounds(600, 300, 500, 500);
        setTitle("Chat Client");

        //authentication
        authPanel = new JPanel(new GridLayout(2, 3));//панелька для аутенификации с логином и паролем

        JTextField loginField = new JTextField();
        loginField.setToolTipText("Enter your Login here");//всплывающие подсказки
        authPanel.add(new JLabel("Login: "));
        authPanel.add(loginField);

        JButton signInButton = new JButton("Sing In");
        authPanel.add(signInButton);

        JTextField passwordField = new JTextField();
        passwordField.setToolTipText("Enter your Password here");//всплывающие подсказки
        authPanel.add(new JLabel("Password: "));
        authPanel.add(passwordField);
        add(authPanel, BorderLayout.NORTH);

        //действие при нажатии кнопк аутентификации
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendAuthCommand(loginField.getText(), passwordField.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //многострочный элемент для всех сообщений
        serverMsgElement = new JTextArea();
        serverMsgElement.setEditable(false);
        serverMsgElement.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(serverMsgElement);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        //кнопка отправки сообщения
        JButton sendButton = new JButton("SEND");
        bottomPanel.add(sendButton, BorderLayout.EAST);
        clientMsgElement = new JTextField();
        bottomPanel.add(clientMsgElement, BorderLayout.CENTER);

        //отправка по кнопке
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = clientMsgElement.getText();
                try {
                    sendMessage(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                clientMsgElement.grabFocus();
            }
        });

        //отправка по Enter
        clientMsgElement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = clientMsgElement.getText();
                try {
                    sendMessage(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    out.writeUTF("end");
                    out.flush();
                    socket.close();
                    out.close();
                    in.close();
                } catch (IOException exc) {
                }
            }
        });

        setVisible(true);
    }

    //метод для проверки логина и пароля
    private void sendAuthCommand(String login, String password) throws IOException {
        String command = "auth___" + login + "___" + password;//___- символ-разделитель
        out.writeUTF(command);//проверка
        out.flush();//отправка на сервер
    }

    private void initServerListner() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = in.readUTF();//приемник сообщений из сервера
                        if (message.equalsIgnoreCase("end session")) {
                            break;
                        } else if (message.equalsIgnoreCase("signIn_success")) {//если получени от сервера "signIn_success"
                            JOptionPane.showMessageDialog(null, "SingIn ok!");// получение диалогового окна
                            authPanel.setVisible(false);//скрытие панели с логином и паролем
                        } else if (message.equalsIgnoreCase("signIn_fail")) {//если получени сообщение "signIn_fail"
                            JOptionPane.showMessageDialog(null, "SingIn fail! Try one more time");// получение диалогового окна
                        } else
                            serverMsgElement.append(message + "\n");//если еще что-то получено - служебное сообщение
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void sendMessage(String message) throws IOException {
        if (!message.trim().isEmpty()) {
            out.writeUTF(message);
            out.flush();
            clientMsgElement.setText("");
        }
    }
}
