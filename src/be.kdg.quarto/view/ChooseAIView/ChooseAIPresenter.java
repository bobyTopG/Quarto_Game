package be.kdg.quarto.view.ChooseAIView;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Human;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import be.kdg.quarto.helpers.AICharacters;

import java.util.ArrayList;
import java.util.List;

public class ChooseAIPresenter {
    private final ChooseAIView view;
    private Ai aiSelected;

    public ChooseAIPresenter(ChooseAIView view) {
        this.view = view;
        List<Image> images = findAICharacterImagesForButtons();
        String pathToNotFound = "/images/aiCharacters/BoxedNotFound.png";
        Image notFoundImage = new Image(pathToNotFound);
        view.initialise(images, AICharacters.getCharacters(), notFoundImage);
        updateView();
        addEventHandlers();
    }

    private List<Image> findAICharacterImagesForButtons() {
        List<Ai> AiList = AICharacters.getCharacters();
        List<Image> images = new ArrayList<>();
        for (Ai ai : AiList) {
            String imagePath = "/images/aiCharacters/Boxed" + ai.getName() + ".png";
            Image image = new Image(getClass().getResource(imagePath).toString());
            images.add(image);
        }
        return images;
    }

    private void addEventHandlers() {
        addEventListenersForAICharacterButtons();

        view.getBackButton().setOnMouseClicked(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });

        view.getSelectButton().setOnMouseClicked(event -> {
            if (aiSelected != null) {
                GameView gameView = new GameView();
                GameSession model = new GameSession(
                        AuthHelper.isLoggedIn() ? AuthHelper.getLoggedInPlayer() : new Human("Guest", null),
                        aiSelected
                );
                view.getScene().setRoot(gameView);
                new GamePresenter(model, gameView);
            }
        });
    }

    private void addEventListenersForAICharacterButtons() {
        int count = 0;
        for (Button characterButton : view.getCharacterButtons()) {
            final int id = count;
            characterButton.setOnMouseClicked(event -> {
                view.setSelectedCharacter(id);
                view.getSelectButton().setDisable(false);
                aiSelected = AICharacters.getCharacters().get(id);
            });
            count++;
        }

        for (Button notFoundButton : view.getNotFoundButtons()) {
            notFoundButton.setOnMouseClicked(event -> {
                view.setSelectedCharacter(-1);
                view.getSelectButton().setDisable(true);
                aiSelected = null;
            });
        }
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}