package sample.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//класс, который показывает файлы и путь к файлвм, содержащиеся в директории
public class ShowFilesInDir {

    //метод показывает путь к файлам в директории
    public static ArrayList<String> getFileTree(String root) throws IOException {

        ArrayList<String> dirs = new ArrayList<>();
        ArrayList<File> filesList = new ArrayList<>();

        File file = new File(root);
        filesList.add(file);//добавляем все файлы в диреектории в лист

        while (!filesList.isEmpty()) {//пока лист не полон
            File underRoot = filesList.get(0);//добавляется директория и проверяется на наличие файлов
            filesList.remove(0);
            if (underRoot.isDirectory())//если путь является директорией
                for (File f : underRoot.listFiles()) filesList.add(f);//получаем список
                // файлов (listfiles)  и если они являются жиректорией выкидываем в стек

            else if (underRoot.isFile()) dirs.add(underRoot.getAbsolutePath());// если файл
            // прописываем путь к файлу(getAbsoluteRootPath) и добавляем в в коллекцию
        }
        return dirs;
    }
    //метод показывает названия файлов в директории
    public static ArrayList<String> getFilesName(String root) throws IOException {
        List<String> list = getFileTree(root);
        ArrayList<String> filesName = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
//pattern.quote- метод необходимый, чтобы экранировать слэш.
            String[] arrDir = list.get(i).split(Pattern.quote("\\"));
            filesName.add(arrDir[arrDir.length - 1]);
        }
        return filesName;
    }
}

