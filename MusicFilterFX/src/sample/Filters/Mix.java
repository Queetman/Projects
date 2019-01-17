package sample.Filters;

import java.util.ArrayList;

public class Mix extends ContainsWords implements Filterable {

    @Override
    public ArrayList<Object[]> filter(ArrayList<Object[]> data, boolean willFilter) {

        ArrayList<String> words = new ArrayList<>();
        words.add("mix");
        words.add("микс");

        return filterContains(data, willFilter, words);
    }
}
