package sample.Filters;

import java.util.ArrayList;

/**
 * Created by koloskov on 06.07.17.
 */

//Интерфейс для фильтрации
public interface Filterable {

  ArrayList<Object[]> filter(ArrayList<Object[]> data, boolean willFilter);

}
