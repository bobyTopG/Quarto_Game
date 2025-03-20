package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.ChooseAIView.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIView.ChooseAIView;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StatisticsView.StatisticsPresenter;
import be.kdg.quarto.view.StatisticsView.StatisticsView;
import be.kdg.quarto.view.auth.RegisterView.RegisterPresenter;
import be.kdg.quarto.view.auth.RegisterView.RegisterView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.Optional;

public class StartPresenter {

    private Game model;
    private GameSession session;

    private StartView view;
    private BoardView boardView = new BoardView();
    private final String pathToBoard = "/images/Example_Board.png";


    public StartPresenter( StartView view) {
        this.session = session;
        this.model = session.getModel();
        this.view = view;
        Image boardImage = new Image(pathToBoard);
        new BoardPresenter(model, view.getBoard());
        addEventHandlers();
        updateView();
        view.loadBoardImage(boardImage);


    }

    private void addEventHandlers() {
        view.getRegisterB().setOnAction(event -> {
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


        view.getStatistics().setOnAction(event -> {
            StatisticsView statsView = new StatisticsView();
            Stage statsStage = new Stage();
            statsStage.initOwner(view.getScene().getWindow());
            statsStage.initModality(Modality.APPLICATION_MODAL);
            statsStage.setScene(new Scene(statsView));

            statsStage.setTitle("Statistics");
            statsStage.setWidth(400);
            statsStage.setHeight(400);
            statsStage.setResizable(false);

            // view.getScene().setRoot(statisticsView);
            new StatisticsPresenter(statsView, new Statistics());
            statsStage.show();
        });

        view.getQuit().setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quit");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(0);
            }
        });
    }

    private void goToChooseAIScreen() {
        ChooseAIView chooseAIView = new ChooseAIView();
        view.getScene().setRoot(chooseAIView);
        new ChooseAIPresenter(chooseAIView,session);
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}
