package com.company;


import com.company.listeners.FrameListener;
import com.company.listeners.TabbedPaneChangeListener;
import com.company.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);


    public void showAbout() {
        JOptionPane.showMessageDialog(this, "HTML Editor", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    //Добавь в представление метод update(), который должен получать документ у контроллера и устанавливать его в панель редактирования htmlTextPane.
    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }


    public void selectHtmlTab() {
//Выбирать html вкладку (переключаться на нее)
        tabbedPane.setSelectedIndex(0);
        //Сбрасывать все правки с помощью метода
        resetUndo();
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public boolean isHtmlTabSelected() {
        return tabbedPane.getSelectedIndex() == 0;
    }

    public void undo() {

        try {
            undoManager.undo();
        } catch (CannotUndoException e) {
            ExceptionHandler.log(e);
        }
    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (CannotUndoException e) {
            ExceptionHandler.log(e);
        }
    }

    public void resetUndo() {

        undoManager.discardAllEdits();
    }

    private JTabbedPane tabbedPane = new JTabbedPane(); // это будет панель с двумя вкладками.
    private JTextPane htmlTextPane = new JTextPane(); //это будет компонент для визуального редактирования html.  Он будет размещен на первой вкладке.
    private JEditorPane plainTextPane = new JEditorPane(); // это будет компонент для редактирования html в виде текста, он будет отображать код html (теги и их содержимое).
    private Controller controller;

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    /*  Добавь конструктор класса View. Он должен устанавливать внешний вид и поведение
          (look and feel) нашего приложения такими же, как это определено в системе.*/
    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void initMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();//Это и будет наша панель меню.
        // ициализировать меню в следующем порядке:
        JMenuBar menuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, menuBar);// Файл
        MenuHelper.initEditMenu(this, menuBar);//Редактировать
        MenuHelper.initStyleMenu(this, menuBar);//Стиль,
        MenuHelper.initAlignMenu(this, menuBar);//Выравнивание
        MenuHelper.initColorMenu(this, menuBar);//Цвет
        MenuHelper.initFontMenu(this, menuBar);//Шрифт
        MenuHelper.initHelpMenu(this, menuBar);// Помощь.

        this.getContentPane().add(menuBar, BorderLayout.NORTH);//Добавлять в верхнюю часть панели контента текущего фрейма нашу панель меню,
    }

    //  Они будут отвечать за инициализацию меню и панелей редактора.
    public void initEditor() {
        htmlTextPane.setContentType("text/html"); //Устанавливать значение "text/html" в качестве типа контента для компонента htmlTextPane.
        JScrollPane scrollPane1 = new JScrollPane(htmlTextPane);//Создавать новый локальный компонент JScrollPane на базе htmlTextPane.
        tabbedPane.add("HTML", scrollPane1);//  Добавлять вкладку в панель tabbedPane с именем "HTML" и компонентом из предыдущего пункта.
        JScrollPane scrollPane2 = new JScrollPane(plainTextPane);  //Создавать новый локальный компонент JScrollPane на базе plainTextPane.
        tabbedPane.add("Текст", scrollPane2);// Добавлять еще одну вкладку в tabbedPane с именем "Текст" и компонентом из предыдущего пункта.
        tabbedPane.setPreferredSize(new Dimension(800, 600));//Устанавливать предпочтительный размер панели tabbedPane.
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);//Создавать объект класса TabbedPaneChangeListener
        tabbedPane.addChangeListener(tabbedPaneChangeListener);// и устанавливать его в качествеслушателя изменений в tabbedPane.
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);//Добавлять по центру панели контента текущего фрейма нашу панель с вкладками.
    }

    public void initGui() {
        initMenuBar();
        initEditor();
        pack();//реализацию pack мы унаследовали от класса JFrame.
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    //этот метод будет вызваться при выборе пунктов меню
    @Override
    public void actionPerformed(ActionEvent e) {  //Получи из события команду с помощью метода getActionCommand(). Это будет обычная строка.
//По этой строке ты можешь понять какой пункт меню создал данное событие.
        String command = e.getActionCommand();

        switch (command) {

            case "Новый":
                controller.createNewDocument();
                break;
            case "Открыть":
                controller.openDocument();
                break;
            case "Сохранить":
                controller.saveDocument();
                break;
            case "Сохранить как...":
                controller.saveDocumentAs();
                break;
            case "Выход":
                controller.exit();
                break;
            case "О программе":
                showAbout();
                break;
        }
    }

    //инициализация
    public void init() {
        initGui();//Вызывать инициализацию графического интерфейса
//Добавлять слушателя событий нашего окна.
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        //Показывать наше окно
        //   На этом этапе приложение при запуске должно показывать окно, которое можно растягивать,
        //  разворачивать, закрыть и т.д.*/
        setVisible(true);
    }

    //  Они будут отвечать за инициализацию меню и панелей редактора.
    public void exit() {
        controller.exit();
    }

    //Этот метод вызывается, когда произошла смена выбранной вкладки
    public void selectedTabChanged() {//Метод должен проверить, какая вкладка сейчас оказалась выбранной
        if (isHtmlTabSelected()) {
            //значит нам нужно получить текст из plainTextPane и установить его в контроллер с помощью метода setPlainText
            controller.setPlainText(plainTextPane.getText());
        } else {
            //необходимо получить текст у контроллера с помощью метода getPlainText() и установить его в панель plainTextPane
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }
}

