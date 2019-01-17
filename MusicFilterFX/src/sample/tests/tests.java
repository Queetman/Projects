package sample.tests;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Filters.*;

import java.util.ArrayList;


/**
 * Created by koloskov on 24.07.17.
 */
public class tests {


    ArrayList<Object[]> data = new ArrayList<>();
    ContainsWord containsWord = new ContainsWord("rem", true);
    ContainsCopy contCopy = new ContainsCopy();
    Minus minus = new Minus();
    Mix mix = new Mix();
    FinalFilter finalFilter = new FinalFilter(data, false, false, false, true);

    @Before
    public void initTest() {
        data.add(new Object[]{1, "one remix", "opa"});
        data.add(new Object[]{2, "one remixxx", "poza"});
        data.add(new Object[]{3, "one dalai", "lada"});
        data.add(new Object[]{4, "one minus", "zapo"});
        data.add(new Object[]{5, "one минус", "apoz"});
        data.add(new Object[]{6, "one dala", "dala"});
        data.add(new Object[]{7, "one (1)minus", "zopa"});
        data.add(new Object[]{8, "one  минус", "apoz"});
        data.add(new Object[]{9, "one копия", "lada"});
    }

    @Test
    public void contWordTest() {


        ArrayList<Object[]> flist = containsWord.filter(data, true);

        Assert.assertEquals(flist.get(0), data.get(0));
        Assert.assertEquals(flist.get(1), data.get(1));

    }

    @Test
    public void contCopy() {

        ArrayList<Object[]> flist = contCopy.filter(data, true);

        Assert.assertEquals(flist.get(0), data.get(6));
        Assert.assertEquals(flist.get(1), data.get(8));
    }

    @Test
    public void contMinus() {
        ArrayList<Object[]> flist = minus.filter(data, true);

        Assert.assertEquals(flist.get(0), data.get(3));
        Assert.assertEquals(flist.get(1), data.get(4));
        Assert.assertEquals(flist.get(2), data.get(6));
        Assert.assertEquals(flist.get(3), data.get(7));
    }

    @Test
    public void contMix() {
        ArrayList<Object[]> flist = mix.filter(data, true);

        Assert.assertEquals(flist.get(0), data.get(0));
        Assert.assertEquals(flist.get(1), data.get(1));
    }

    @Test
    public void finFilter() {
        ArrayList<Object[]> flist = finalFilter.getFiltData("ala", false);

        Assert.assertEquals(flist.size(), 7);

    }


}
