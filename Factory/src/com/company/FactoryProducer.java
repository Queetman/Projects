package com.company;

import com.company.female.FemaleFactory;
import com.company.male.MaleFactory;

public class FactoryProducer {

    public static AbstractFactory getFactory(HumanFactoryType humanFactoryType) {
        if (humanFactoryType.equals(HumanFactoryType.MALE)) {
            return new MaleFactory();
        } else {
            return new FemaleFactory();
        }
    }

    public static enum HumanFactoryType {
        MALE, FEMALE
    }
}
