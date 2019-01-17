package sample.Filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

//Конечный фильтр будет учитывать все мини-фильтры и выдавать финальное значение.
public class FinalFilter {

    //Это данные, которые передаются в конструкторе. С этих данных необходимо выделить позиции для удаления
    private ArrayList<Object[]> data;

    private boolean willFIltMinus;
    private boolean willFiltMix;
    private boolean willFiltEnd;
    private boolean willFIltContWord;


    public FinalFilter(ArrayList<Object[]> data, boolean willFIltMinus, boolean willFiltMix, boolean willFiltEnd, boolean willFIltContWord) {
        this.data = data;
        this.willFIltMinus = willFIltMinus;
        this.willFiltMix = willFiltMix;
        this.willFiltEnd = willFiltEnd;
        this.willFIltContWord = willFIltContWord;
        //Получение отфильтрованных данных
    }

    // переменные word и containsOrNot нужны только для одного фильтра - ContainsWord()
    public ArrayList<Object[]> filterAll(String word, boolean containsOrNot) {

        //создание листа для отфильтрованных данных
        ArrayList<Object[]> finalData = new ArrayList<>();

        ArrayList<Object[]> filterData;

        //Разрозненные отфильтрованные данные
        ArrayList<Object[]> conCopy = new ContainsCopy().filter(data, willFiltEnd);
        ArrayList<Object[]> mixData = new Mix().filter(data, willFiltMix);
        ArrayList<Object[]> minus = new Minus().filter(data, willFIltMinus);
        ArrayList<Object[]> containsWord = new ContainsWord(word, containsOrNot).filter(data, willFIltContWord);

        //Добавление данных в один лист
        addToData(conCopy, finalData);
        addToData(mixData, finalData);
        addToData(minus, finalData);
        addToData(containsWord, finalData);

        filterData = checkCopy(finalData);

        return filterData;
    }
    //Геттер для получения отфильтрованных данных
    public ArrayList<Object[]> getFiltData(String word, boolean containsOrNot) {

        return filterAll(word, containsOrNot);
    }
    //Вспомогательный метод для добавления отфильтрованных данных
    private void addToData(ArrayList<Object[]> initData, ArrayList<Object[]> finData) {

        for (Object[] o : initData) {
            finData.add(o);
        }
    }
    //При фильтриовке могут образовываться одинаковые данные. Этот метод перегоняет эррэйлист через хэшсет и обратно.
    public static ArrayList<Object[]> checkCopy(ArrayList<Object[]> data) {

        HashSet<Object[]> copyDel = new HashSet<>();
        ArrayList<Object[]> endData = new ArrayList<>();

        for (Object[] o : data) {
            copyDel.add(o);
        }
        Iterator it = copyDel.iterator();

        while (it.hasNext()) {
            endData.add((Object[]) it.next());
        }
        return endData;
    }
}
