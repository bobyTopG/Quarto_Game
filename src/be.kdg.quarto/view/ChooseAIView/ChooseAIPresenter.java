package be.kdg.quarto.view.ChooseAIView;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Human;
import be.kdg.quarto.model.Player;
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
    private Player aiSelected;
    private AICharacters aiCharacters = new AICharacters();

    public ChooseAIPresenter(ChooseAIView view) {
        this.view = view;
        List<Image> images = findAICharacterImagesForButtons();
        String pathToNotFound = "/images/aiCharacters/BoxedNotFound.png";
        Image notFoundImage = new Image(pathToNotFound);
        view.initialise(images, aiCharacters.getCharacters(), notFoundImage);
        updateView();
        addEventHandlers();
    }

    private List<Image> findAICharacterImagesForButtons() {
        List<Player> AiList = aiCharacters.getCharacters();
        List<Image> images = new ArrayList<>();
        for (Player ai : AiList) {
            String imagePath = "/images/aiCharacters/Boxed" + ai.getName() + ".png";
            Image image = new Image(getClass().getResource(imagePath).toString());
            images.add(image);
        }
        return images;
    }

    private void addEventHandlers() {

        int count = 0;

        for (Button characterButton : view.getCharacterButtons()) {
            final int id = count;
            characterButton.setOnMouseClicked(event -> {
                view.setSelectedCharacter(id);
                view.getSelectButton().setDisable(false);
                aiSelected = aiCharacters.getCharacters().get(id);
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


        view.getBackButton().setOnMouseClicked(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });


        view.getSelectButton().setOnMouseClicked(event -> {
            if (aiSelected != null) {
                GameView gameView = new GameView();
                GameSession model = new GameSession(
                        AuthHelper.isLoggedIn() ? AuthHelper.getLoggedInPlayer() : AuthHelper.getGuestPlayer(),
                        aiSelected , view.getTurnButton().isSelected()? null : aiSelected
                );



                view.getScene().setRoot(gameView);
                new GamePresenter(model, gameView);
            }
        });
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}