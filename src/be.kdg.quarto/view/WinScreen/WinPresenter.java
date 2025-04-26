package be.kdg.quarto.view.WinScreen;

import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;

public class WinPresenter {
    private WinView view;

    public WinPresenter(WinView view) {
        this.view = view;

        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getExitButton().setOnAction(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });
    }

    private void updateView() {

    }

}
