package be.kdg.quarto.view.SettingsScreen;

import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;

public class SettingsPresenter {
    private SettingsView view;

    public SettingsPresenter(SettingsView view) {
        this.view = view;

        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {
    }

    private void updateView() {

    }

}