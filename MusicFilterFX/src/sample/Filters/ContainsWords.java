package sample.Filters;

import java.util.ArrayList;


//ОБщий класс для других классов. СОдержит метод, с помощью которого добавляются в список данные, которые содержат
//слова в пути. Для ускорения процесса вставлена булевская переменная willFilter. Если фильтровать не надо, она равна false/
public abstract class ContainsWords {

    protected ArrayList<Object[]> filterContains(ArrayList<Object[]> data, boolean willFilter, ArrayList<String> words) {

        ArrayList<Object[]> filtData = new ArrayList<>();

        if (willFilter == true) {


            for (Object[] o : data) {
                String s = (String) o[1];

                for (String word : words) {

                    if (s.toLowerCase().contains(word)) {
                        filtData.add(o);
                    }
                }
            }
        }
        return filtData;
    }


}
