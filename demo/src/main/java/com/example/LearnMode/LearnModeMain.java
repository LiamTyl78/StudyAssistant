package com.example.LearnMode;

import com.example.StudyMode;

public class LearnModeMain extends StudyMode{
    LearnController controller;
    LearnView view;
    LearnModel model;

    @Override
    public int startMode(){
        return controller.start();
    }

    public LearnModeMain(String filePath){
        this.controller = new LearnController();
        this.view = new LearnView(this.controller);
        this.model = new LearnModel(filePath,this.controller);
        this.controller.setView(view);
        this.controller.setModel(model);
    }

    public int getKnownCards(){
        return controller.getKnownCards();
    }

    public int getTotalCards(){
        return controller.getTotalCards();
    }

    public void resetProgress(){
        controller.resetProgress();
    }
    
}
