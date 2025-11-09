package com.example.LearnMode;

import java.util.ArrayList;

public interface LearnModelEvents {

    void displayMessage(String string);

    void disposeView();

    void hideView();

    void displayIcorrectMessage(String string);

    void showView();

    void onUpdate(String currentImage, String currentDef, ArrayList<String> selectedAnswers, int buttonsShown, int currentTermIndex, int currentRoundTerms);

    void showStart();
    
}
