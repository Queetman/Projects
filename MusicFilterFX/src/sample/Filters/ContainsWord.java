package sample.Filters;

import java.util.ArrayList;

//Если композиция содержит какое-либо слово или не содержит, тогда используется это фильтр. Композиция вводится с интерфейса
public class ContainsWord implements Filterable {

    //слово
    private String word;
    //фильтровать или со словом или нет
    private boolean contains;

    public ContainsWord(String word, boolean contains) {
        this.word = word;
        this.contains = contains;
    }

    @Override
    public ArrayList<Object[]> filter(ArrayList<Object[]> data, boolean willFilter) {

        ArrayList<Object[]> filtData = new ArrayList<>();

        if (willFilter == true) {

            for (Object[] o : data) {
                String s = (String) o[1];

                if (contains == true) {

                    if (s.toLowerCase().contains(word)) {
                        filtData.add(o);
                    }
                }

                if (contains == false) {
                    if (!s.toLowerCase().contains(word)) {
                        filtData.add(o);
                    }
                }
            }
        }
        return filtData;
    }
}
