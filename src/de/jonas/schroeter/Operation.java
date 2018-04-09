package de.jonas.schroeter;

import java.util.Arrays;

public enum Operation {

    ADDITION("Addition"),
    SUBTRACTION("Subtraction"),
    MULTIPLICATION("Multiplication"),
    DIVISION("Division"),
    INTERVALADDITION("Interval Addition"),
    FACTORIAL("Factorial"),
    POWER("Power"),
    ROOT("Square Root");

    private final String name;

    Operation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    //returns if the operation is a single number one
    public boolean isSpecial() {
        Operation[] operations = {FACTORIAL, ROOT};
        return Arrays.asList(operations).contains(this);
    }
}
