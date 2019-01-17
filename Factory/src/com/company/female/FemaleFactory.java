package com.company.female;

import com.company.AbstractFactory;
import com.company.Human;

public class FemaleFactory implements AbstractFactory {
    public Human getPerson(int age) {
        if (age <= KidGirl.MAX_AGE) {
            return new KidGirl();
        } else if (age <= TeenGirl.MAX_AGE && age > KidGirl.MAX_AGE) {
            return new TeenGirl();
        } else {
            return new Woman();
        }
    }

}
