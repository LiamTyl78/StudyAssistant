package com.example.LearnMode;

import java.util.ArrayList;

public interface LearnModelEvents {

    void displayMessage(String string);

    void disposeView();

    void hideView();

    void displayIcorrectMessage(String string);

    void showView();

    void setViewButtons(ArrayList<String> selectedAnswers, int buttonsShown);

    void setViewTitle(int i, int currentRoundTerms);

    void updateView(String currentImage, String currentDefinition);

    void showStart();
    
}
