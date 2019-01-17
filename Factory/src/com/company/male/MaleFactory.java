package com.company.male;

import com.company.AbstractFactory;
import com.company.Human;

public class MaleFactory implements AbstractFactory
{
    public Human getPerson(int age) {

        if (age <= KidBoy.MAX_AGE) {
            return new KidBoy();
        }
        else if (age <= TeenBoy.MAX_AGE && age > KidBoy.MAX_AGE) {
            return new TeenBoy();
        }
        else {
            return new Man();
        }
    }

}
