package com.example;

import java.util.ArrayList;

/**
 * This class contains shared methods for the different study modes
 */
public class StudyMode {
    public int startMode(){
        return 0;
    };

    public void shuffle(ArrayList<Card> list) {
        RandomInteger random = new RandomInteger(0, list.size() - 1);
        for (int i = 0; i < 40; i++) {
            int randomindex = random.Generate();
            Card temp = list.get(randomindex);
            temp = list.set(random.Generate(), temp);
            list.set(randomindex, temp);
        }
    }
}
