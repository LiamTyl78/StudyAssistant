package com.example;
public class RandomInteger {
    private int min, max, range;

    public RandomInteger() {
        this.min = 0;
        this.max = 100;
        range = max - min + 1;
    }

    public RandomInteger(int min, int max) {
        this.min = min;
        this.max = max;
        range = max - min + 1;
    }

    public void SetMax(int max) {
        this.max = max;
        range = max - min +  1;
    }

    public void SetMin(int min) {
        this.min = min;
        range = max - min +  1;
    }

    public int GetMin() {
        return min;
    }

    public int GetMax() {
        return max;
    }
    public int Generate(){
        return (int)((range) *  Math.random()) + min;
    }
}
