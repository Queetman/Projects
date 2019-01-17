package com.company;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile; //файл, который сейчас открыт в нашем редакторе (текущий файл)

    public Controller(View view) {
        this.view = view;
    }

    public void resetDocument() {
        if (document != null) {
            //Удалять у текущего документа document слушателя правок которые можно отменить/вернуть
            document.removeUndoableEditListener(view.getUndoListener());
        }
        //Создавать новый документ по умолчанию и присваивать его полю document
        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        //Добавлять новому документу слушателя правок
        document.addUndoableEditListener(view.getUndoListener());
        //Вызывать у представления метод update()
        view.update();
    }

    // Он должен получать текст из документа со всеми html тегами.
    public String getPlainText() {
        StringWriter stringWriter = new StringWriter();
        try {
            //Вызови метод read() из класса HTMLEditorKit, который вычитает данные из реадера в документ document
            new HTMLEditorKit().write(stringWriter, document, 0, document.getLength());

        } catch (Exception e) {
            //Проследи, чтобы метод не кидал исключения. Их необходимо просто логировать
            ExceptionHandler.log(e);
        }
        return stringWriter.toString();
    }

    //Он будет записывать переданный текст с html тегами в документ document
    public void setPlainText(String text) {
        //Сбрось документ
        resetDocument();
        //Создай новый реадер StringReader на базе переданного текста
        StringReader stringReader = new StringReader(text);

        try {
            //Вызови метод read() из класса HTMLEditorKit, который вычитает данные из реадера в документ document
            new HTMLEditorKit().read(stringReader, document, 0);

        } catch (Exception e) {
            //Проследи, чтобы метод не кидал исключения. Их необходимо просто логировать
            ExceptionHandler.log(e);
        }
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    //инициализация
    public void init() {
        createNewDocument();
    }

    public void exit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        //Создавать объект представления.
        View view = new View();

        //  Создавать контроллер, используя представление.
        Controller controller = new Controller(view);

        // Устанавливать у представления контроллер.
        view.setController(controller);
        // Инициализировать представление.
        view.init();
        //Инициализировать контроллер. Контроллер должен инициализироваться после представления
        controller.init();
    }

    public void createNewDocument() {
        view.selectHtmlTab();//Выбирать html вкладку у представления.
        resetDocument();//Сбрасывать текущий документ.
        view.setTitle("HTML редактор");//устанавливать новый заголовок окна, например: "HTML редактор".
        view.resetUndo();//Сбрасывать правки в Undo менеджере
        currentFile = null;//Обнулить переменную currentFile.
    }

    public void openDocument() {

        // Метод должен работать аналогично методу saveDocumentAs(), в той части, которая отвечает за выбор файла

        //Переключать представление на html вкладку
        view.selectHtmlTab();
        //Создавать новый объект для выбора файла JFileChooser
        JFileChooser jFileChooser = new JFileChooser();
        //Устанавливать ему в качестве фильтра объект HTMLFileFilter
        jFileChooser.setFileFilter(new HTMLFileFilter());
        //Показывать диалоговое окно "Save File" для выбора файла
        int n = jFileChooser.showOpenDialog(view);

        //Когда файл выбран, необходимо
        if (n == JFileChooser.APPROVE_OPTION) {
            //Установить новое значение currentFile
            currentFile = jFileChooser.getSelectedFile();
            //Сбросить документ
            resetDocument();
            //Установить имя файла в заголовок у представления
            view.setTitle(currentFile.getName());

            //Создать FileReader, используя currentFile
            try (FileReader fileReader = new FileReader(currentFile)) {
                //Вычитать данные из FileReader-а в документ document с помощью объекта класса
                new HTMLEditorKit().read(fileReader, document, 0);
                //Сбросить правки
                view.resetUndo();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
    }

    public void saveDocument() {

        // Метод должен работать также, как saveDocumentAs(), но не запрашивать файл у пользователя,
        // а использовать currentFile. Если currentFile равен null, то вызывать метод saveDocumentAs().

        if (currentFile == null) {
            saveDocumentAs();
        } else {
            //Переключать представление на html вкладку
            view.selectHtmlTab();

            //Создавать FileWriter на базе currentFile
            try (FileWriter fileWriter = new FileWriter(currentFile)) {
                //Переписывать данные из документа document в объекта FileWriter-а аналогично тому, как мы это делали в методе getPlainText()
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());

            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
    }

    //  Реализуем в контроллере метод для сохранения файла под новым именем
    public void saveDocumentAs() {//Переключать представление на html вкладку.
        view.selectHtmlTab();
//Создавать новый объект для выбора файла JFileChooser.
        JFileChooser jFileChooser = new JFileChooser();
//Устанавливать ему в качестве фильтра объект HTMLFileFilter.
        jFileChooser.setFileFilter(new HTMLFileFilter());

        //Показывать диалоговое окно "Save File" для выбора файла
        int n = jFileChooser.showSaveDialog(view);

        //Если пользователь подтвердит выбор файла:
        if (n == JFileChooser.APPROVE_OPTION) {
            //Сохранять выбранный файл в поле currentFile
            currentFile = jFileChooser.getSelectedFile();
            //Устанавливать имя файла в качестве заголовка окна представления
            view.setTitle(currentFile.getName());

            //Создавать FileWriter на базе currentFile
            try (FileWriter fileWriter = new FileWriter(currentFile)) {
                //Переписывать данные из документа document в объекта FileWriter-а аналогично тому, как мы это делали в методе getPlainText()
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
    }
}

