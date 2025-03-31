package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.model.Leaderboard;
import be.kdg.quarto.view.LeaderboardScreen.LeaderboardPresenter;
import be.kdg.quarto.view.LeaderboardScreen.LeaderboardView;
import be.kdg.quarto.view.GameScreen.BoardView.BoardView;
import be.kdg.quarto.view.ChooseAIView.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIView.ChooseAIView;
import be.kdg.quarto.view.auth.LoginView.LoginPresenter;
import be.kdg.quarto.view.auth.LoginView.LoginView;
import be.kdg.quarto.view.auth.RegisterView.RegisterPresenter;
import be.kdg.quarto.view.auth.RegisterView.RegisterView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;


import java.util.Optional;

public class StartPresenter {

    private StartView view;
    private BoardView boardView = new BoardView();
    private final String pathToBoard = "/images/Example_Board.png";


    public StartPresenter(StartView view) {

        this.view = view;

        String pathToBoard = "/images/Example_Board.png";
        Image boardImage = new Image(pathToBoard);
        addEventHandlers();
        updateView();
        view.loadBoardImage(boardImage);


    }

    private void addEventHandlers() {
        view.getNewGame().setOnAction(event -> {
            // Check if user is already logged in
            if (AuthHelper.isLoggedIn()) {
                // User is logged in, go directly to Choose AI screen
                goToChooseAIScreen();
            } else {
                // User is not logged in, go to Register screen
                RegisterView registerView = new RegisterView();
                view.getScene().setRoot(registerView);
                new RegisterPresenter(registerView);
            }
        });

        // "Continue" button handler
        view.getContinueButton().setOnAction(event -> {
            // Check if user is already logged in
            if (AuthHelper.isLoggedIn()) {
                // User is logged in, go directly to Choose AI screen
                goToChooseAIScreen();
            } else {
                // User is not logged in, go to Login screen
                LoginView loginView = new LoginView();
                view.getScene().setRoot(loginView);
                new LoginPresenter(loginView);
            }
        });


        view.getStatistics().setOnAction(event -> {

        });

        view.getLeaderboard().setOnAction(event -> {
            LeaderboardView leaderboardView = new LeaderboardView();
            view.getScene().setRoot(leaderboardView);
            new LeaderboardPresenter(leaderboardView, new Leaderboard());
        });

        view.getQuit().setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quit");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.exit(0);
            }
        });
    }

    /**
     * Helper method to navigate to the Choose AI screen
     */
    private void goToChooseAIScreen() {
        ChooseAIView chooseAIView = new ChooseAIView();
        view.getScene().setRoot(chooseAIView);
        new ChooseAIPresenter(chooseAIView);
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}
