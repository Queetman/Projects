package sample.Filters;


//Предфильтр необходим чтобы отфильтровать первичные данные, а именно определить, является ли этот файл мп3.
public class PreFilter {

    //предфильтр - проверяет является ли файл музыкальной композицией.
    public static boolean isMp3(Object[] data) {

        boolean b = false;

        for (int i = 0; i < data.length; i++) {
            String s = (String) data[1];

            if (s.endsWith(".mp3"))
                b = true;

            else b = false;
        }
        return b;
    }
}
