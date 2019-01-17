package sample.Filters;

import java.util.ArrayList;

//Содержит слово Минус
public class Minus extends ContainsWords implements Filterable {

    @Override
    public ArrayList<Object[]> filter(ArrayList<Object[]> data, boolean willFilter) {

        ArrayList<String> words = new ArrayList<>();
        words.add("minus");
        words.add("минус");

        return filterContains(data, willFilter, words);
    }
}

