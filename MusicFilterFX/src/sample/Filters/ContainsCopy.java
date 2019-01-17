package sample.Filters;

import java.util.ArrayList;

//заканчивается на
public class ContainsCopy extends ContainsWords implements Filterable {

    @Override
    public ArrayList<Object[]> filter(ArrayList<Object[]> data, boolean willFilter) {

        ArrayList<String> words = new ArrayList<>();
        words.add("(1)");
        words.add("(2)");
        words.add("копия");

        return filterContains(data, willFilter, words);
    }
}