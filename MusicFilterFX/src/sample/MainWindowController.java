package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import sample.Filters.FinalFilter;
import sample.Filters.PreFilter;
import sample.actions.ShowFilesInDir;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

public class MainWindowController {

    public static final Logger logger = Logger.getLogger("");
    public static Handler hand;

    @FXML
    public CheckBox isCopy;

    @FXML
    public CheckBox isMinus;

    @FXML
    public CheckBox isMix;

    @FXML
    public CheckBox hasThisWord;

    @FXML
    public CheckBox hasItOrNot;

    //2 таблицы для хранение данных о файлах: начальных и отфильтрованных [id, номер композиции, путь к ней]
    private ArrayList<Object[]> initDataList;
    private ArrayList<Object[]> filtDataList;

    // константа для подсчета количества рядов (задает id)
    int rowCount;

    ArrayList<String> filesDir;
    ArrayList<String> fileNames;

    //Таблицы которые забиваются в видимую таблицу для отображения.
    private final ObservableList<Composition> printInitData = FXCollections.observableArrayList();
    private final ObservableList<Composition> printFiltData = FXCollections.observableArrayList();

    //Поле с директрорией
    @FXML
    private TextField dirField;

    //поле со словом, которое фильтруется
    @FXML
    private TextField word;

    //Таблица с начальными файлами
    @FXML
    private TableView<Composition> initTable;

    @FXML
    private TableColumn<Composition, String> initTableId;

    @FXML
    private TableColumn<Composition, String> initTableComp;

    //Таблица с отфильтрованными файлами
    @FXML
    private TableView<Composition> filteredTable;

    @FXML
    private TableColumn<Composition, String> filteredTableId;

    @FXML
    private TableColumn<Composition, String> filteredTableComp;

    //инициализация
    @FXML
    public void initialize() {

        try {
            hand = new FileHandler("logFile.log");
            hand.setFormatter(new SimpleFormatter());

        } catch (IOException e) {

            logger.log(Level.WARNING, "logger IOException");
        }
        logger.addHandler(hand);

        //Привязка ячейкам значения, которое необходимо добавить.
        // т.е. есть ячейки непонятные. В них надо что-то забить. Эти 2 строки описвают ЧТО надо забить.
        //создается фабрика значений, которые отправляются и добавляется фабрика свойств.
        //что важно: в классе Composition есть 2 переменные: compId и compName. Необходимто, чтобы
        // в PropertyValueFactory были те же значения.
        initTableId.setCellValueFactory(new PropertyValueFactory<Composition, String>("compId"));
        initTableComp.setCellValueFactory(new PropertyValueFactory<Composition, String>("compName"));

        filteredTableId.setCellValueFactory(new PropertyValueFactory<Composition, String>("compId"));
        filteredTableComp.setCellValueFactory(new PropertyValueFactory<Composition, String>("compName"));

        initDataList = new ArrayList<>();
        filtDataList = new ArrayList<>();

        //отправка значений в отображаемые таблицы.
        //Одна особенность - printInitData и printFiltData - это ObservableList. Они наблюдаюбт об
        // своем состоянии и в случае их изменения значения автоматом попадает в  initTable и filteredTable и автоматом
        //отображается.
        initTable.setItems(printInitData);
        filteredTable.setItems(printFiltData);
    }
    //Кнопочки
    @FXML
    public void chooseDirButton() {

        chooseDir();
    }

    @FXML
    public void viewFilesButton() {

        try {
            viewFiles();
        } catch (IOException e) {
            alert("view IOException");
            logger.log(Level.WARNING, "viewButton IOException");
        }
    }

    @FXML
    public void filterButton() {

        filter();
    }

    @FXML
    public void moveCompositionRightButton() {

        moveCompositionRight();
    }

    @FXML
    public void moveCompositionLeftButton() {

        moveCompositionLeft();
    }

    @FXML
    public void clearListsButton() {

        clearData();
    }

    @FXML
    public void deleteButton() {

        try {
            deleteFiles();
        } catch (IOException e) {
            alert("deleteButton IOException");

        }
    }

    private void chooseDir() {
        final Stage stage = new Stage();
        // ядро метода - создание выборщика директории

        final DirectoryChooser directoryChooser =
                new DirectoryChooser();

        //выбор директории
        final File selectedDirectory =
                directoryChooser.showDialog(stage);

        try {
            dirField.setText(selectedDirectory.getAbsolutePath());
        } catch (NullPointerException npe) {
            alert("Неверно выбрана директория");
        }
    }

    //Методы к кнопочкам плюс вспомогательные методы
    public void viewFiles() throws IOException {

        clearData();

        //Получаем пачку файлов и путей к ним.
        filesDir = ShowFilesInDir.getFileTree(dirField.getText());
        fileNames = ShowFilesInDir.getFilesName(dirField.getText());

        logger.log(Level.INFO, "fileNamesSize= " + fileNames.size());

        //далее необходимо перенести их в таблицу 2 колонки
        for (int i = 0; i < fileNames.size(); i++) {

            Object[] o = new Object[]{rowCount, fileNames.get(i), filesDir.get(i)};

            //Здесь заполняется лист initDataList с данными.
            if (PreFilter.isMp3(o)) {

                initDataList.add(o);
                printInitData.add(new Composition(String.valueOf(rowCount), (String) o[1]));
                rowCount++;
            }
        }

        logger.log(Level.INFO, "printInitDataSize=" + printInitData.size());

        if (printInitData.size() == 0) {
            alert("Выбрана невеверная директория или необходимых файлов нет");
        }
    }

    private void filter() {

        logger.log(Level.INFO,

                "initDataListSize=" + initDataList.size() + "\n" +
                        "isMinusState=" + isMinus.isSelected() + "\n" +
                        "isMixState=" + isMix.isSelected() + "\n" +
                        "isCopyState=" + isCopy.isSelected() + "\n" +
                        "hasThisWordState=" + hasThisWord.isSelected() + "\n");

        //Сначала необходимо понять, какие фильтры будут использованы
        FinalFilter filteredData =
                new FinalFilter(initDataList, isMinus.isSelected(), isMix.isSelected(), isCopy.isSelected(), hasThisWord.isSelected());

        //затем окончательная фильтровка со словом.
        filtDataList = filteredData.getFiltData(word.getText(), hasItOrNot.isSelected());

        logger.log(Level.INFO, "filtDataList.size()=" + filtDataList.size());

        if (filtDataList.size() == 0) alert("Лист для удаления файлов пуст!");

        //перегонка из одного листа в другой, дабы вывест все на экран
        //Данный в filtDataList представлены в виде массива Object с id, названием композиции и путем к ней.
        // id и название композиции необхордимо перевести в String и засунуть в Composition чтобы потом
        //засунуть  в printFiltData, которая оповестит filteredTable и выведет изменения

        for (int i = 0; i < filtDataList.size(); i++) {

            Composition comp = new Composition(filtDataList.get(i)[0].toString(), filtDataList.get(i)[1].toString());
            printFiltData.add(comp);
        }

        logger.log(Level.INFO, "printFiltData.size()=" + printFiltData.size());

    }

    public void moveCompositionRight() {

        int index = initTable.getSelectionModel().getSelectedIndex();

        if (index != -1) {

            Composition comp = initTable.getItems().get(index);
            printFiltData.add(comp);

        } else alert("Нет данных для перемещения");
    }

    public void moveCompositionLeft() {

        //ищем индекс в видимой таблице
        int index = filteredTable.getSelectionModel().getSelectedIndex();

        logger.log(Level.INFO, "indexNum" + index);

        if (index != -1) {

            //находим композицию которая была выделена
            Composition comp = filteredTable.getItems().get(index);

            //находим id композиции
            String compId = comp.getCompId();

            int j = 0;

            //ищем совпадение id выбранной композиции и id в листе
            for (int i = 0; i < printFiltData.size(); i++) {

                if (compId.equals(printFiltData.get(i).getCompId())) {
                    j = i;
                    break;
                }
            }
            //нащли -  удаляем
            printFiltData.remove(j);
        } else alert("Нет данных для перемещения");
    }

    private void deleteFiles() throws IOException {

        Dialogs.DialogResponse dialogs = Dialogs.showConfirmDialog(new Stage(),
                "Вы действительно хотите удалить файлы?", "Удаление файлов");

        if (filtDataList.size() != 0) {

            //dialogs.ordinal()==0 - это ответ да. 1- это нет, 2 это отмена
            if (dialogs.ordinal() == 0) {

                //удаление файлов
                for (int i = 0; i < filtDataList.size(); i++) {
                    String dir = String.valueOf(filtDataList.get(i)[2]);

                    File file = new File(dir);
                    if (file.delete()) {
                        logger.log(Level.INFO, "файл удален");

                    } else logger.log(Level.INFO, "Файла  не обнаружено");
                }
                alert("Файлы удалены!");
                clearData();
                viewFiles();
            }

        } else alert("Лист для удаления файлов пуст!");
    }

    private void clearData() {

        rowCount = 1;

        //очистка листов, отвечающих за отображение данных
        printInitData.clear();
        printFiltData.clear();

        //очитска листов листы отвечающих за хранение данных
        initDataList.clear();
        filtDataList.clear();
    }

    //Всплывающие сообщения
    private void alert(String message) {
        Dialogs.showInformationDialog(new Stage(), message, "Информация", "Инфо.");

    }
}






