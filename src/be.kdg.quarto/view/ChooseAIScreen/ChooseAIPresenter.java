package be.kdg.quarto.view.ChooseAIScreen;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Player;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import be.kdg.quarto.helpers.Characters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static be.kdg.quarto.helpers.ImageHelper.getOpponentImage;

public class ChooseAIPresenter {
    private final ChooseAIView view;
    private Player opponentSelected;
    private Characters characters = new Characters();
    private boolean isOnline;

    public ChooseAIPresenter(ChooseAIView view, boolean online) {
        this.view = view;
        List<Image> images = findAICharacterImagesForButtons();
        String pathToNotFound = "/images/aiCharacters/100px/BoxedNotFound.png";
        Image notFoundImage = new Image(pathToNotFound);
        view.initialise(images, characters.getCharacters(), notFoundImage);
        this.isOnline = online;

        updateView();
        addEventHandlers();
    }

    private List<Image> findAICharacterImagesForButtons() {
        List<Player> AiList = characters.getCharacters();
        List<Image> images = new ArrayList<>();
        for (Player ai : AiList) {
            String imagePath = "/images/aiCharacters/100px/Boxed" + ai.getName() + ".png";
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
                opponentSelected = characters.getCharacter(id);
            });
            count++;
        }


        for (Button notFoundButton : view.getNotFoundButtons()) {
            notFoundButton.setOnMouseClicked(event -> {
                view.setSelectedCharacter(-1);
                view.getSelectButton().setDisable(true);
                opponentSelected = null;
            });
        }


        view.getBackButton().setOnMouseClicked(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });


        view.getSelectButton().setOnMouseClicked(event -> {
            if (opponentSelected != null) {
                GameView gameView = new GameView(ImageHelper.getPlayerImage(), getOpponentImage(opponentSelected), opponentSelected.getName());
                GameSession model;
                //if offline play as guest
                try {
                    model = new GameSession(
                            (AuthHelper.isLoggedIn() && isOnline) ? AuthHelper.getLoggedInPlayer() : AuthHelper.getGuestPlayer(),
                            opponentSelected, view.getTurnButton().isSelected()? null : opponentSelected, isOnline
                    );
                    view.getScene().setRoot(gameView);
                    new GamePresenter(model, gameView);
                } catch (SQLException e) {
                    CreateHelper.showAlert("Database Error", e.getMessage(),true);
                }


            }
        });
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }

}