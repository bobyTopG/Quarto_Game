package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.helpers.DbConnection;
import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.Leaderboard;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.view.ContinueScreen.ContinuePresenter;
import be.kdg.quarto.view.ContinueScreen.ContinueView;
import be.kdg.quarto.view.LeaderboardScreen.LeaderboardPresenter;
import be.kdg.quarto.view.LeaderboardScreen.LeaderboardView;
import be.kdg.quarto.view.ChooseAIScreen.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIScreen.ChooseAIView;
import be.kdg.quarto.view.auth.LoginView.LoginPresenter;
import be.kdg.quarto.view.auth.LoginView.LoginView;
import be.kdg.quarto.view.auth.RegisterView.RegisterPresenter;
import be.kdg.quarto.view.auth.RegisterView.RegisterView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import static be.kdg.quarto.helpers.DbConnection.connectedToDb;

public class StartPresenter {

    private final StartView view;
    private Timer animationTimer;
    private boolean isLoading = true;

    //simple-constructor
    public StartPresenter(StartView view) {

        this.view = view;

        addEventHandlers();


        String pathToBoard = "/images/Example_Board.png";
        Image boardImage = new Image(pathToBoard);
        view.loadBoardImage(boardImage);

        checkConnectionInBackground(connectedToDb());

        updateView();
    }
    //first time-load constructor
    public StartPresenter(StartView view, boolean firstTime) {

        this.view = view;

        addEventHandlers();


        String pathToBoard = "/images/Example_Board.png";
        Image boardImage = new Image(pathToBoard);
        view.loadBoardImage(boardImage);

        if(firstTime) {
            DbConnection.startConnection(this::checkConnectionInBackground);
            startConnectingAnimation();
        }else{
            checkConnectionInBackground(connectedToDb());
        }
        updateView();

    }

    private void addEventHandlers() {
        view.getRegister().setOnAction(event -> {
            // Check if user is already logged in
            if (AuthHelper.isLoggedIn()) {
                // User is logged in, go directly to Choose AI screen
                goToChooseAIScreen(true);
            } else {
                // User is not logged in, go to Register screen
                RegisterView registerView = new RegisterView();
                view.getScene().setRoot(registerView);
                new RegisterPresenter(registerView);
            }
        });

        view.getLogin().setOnAction(event -> {
            // Check if user is already logged in
            if (AuthHelper.isLoggedIn()) {
                // User is logged in, go directly to Choose AI screen
                goToContinueGame();
            } else {
                // User is not logged in, go to Log in screen
                LoginView loginView = new LoginView();
                view.getScene().setRoot(loginView);
                new LoginPresenter(loginView);
            }
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

        // Set hover handlers (just the event setup, without the logic)
        view.getOnline().setOnMouseEntered(e -> {
            if(!isLoading)
                setOnlineButton("hover");

        });

        view.getOnline().setOnMouseExited(e -> {
            if(!isLoading)
                setOnlineButton("");
        });

        view.getOnline().setOnMousePressed(e -> {
            setOnlineButton("press");

            if(connectedToDb() && !isLoading) {
                closeConnection();
                view.switchOnlineMode(false);
            } else {
                isLoading = true;
                // Start the animation before attempting connection

                // Then start connection in background
                DbConnection.startConnection(this::checkConnectionInBackground);
                startConnectingAnimation();

            }
        });

        view.getOnline().setOnMouseReleased(e -> {
            //to make the button work only when it is not loading
            if(!isLoading)
                setOnlineButton("");
        });

        view.getPlay().setOnMousePressed(e -> {
            goToChooseAIScreen(false);
        });

    }
    private void goToChooseAIScreen(boolean online) {
        ChooseAIView chooseAIView = new ChooseAIView();
        view.getScene().setRoot(chooseAIView);
        new ChooseAIPresenter(chooseAIView, online);
    }
    private void checkConnectionInBackground(boolean isConnected) {

        if (animationTimer != null) {
            animationTimer.cancel();
            animationTimer = null;
        }

        setOnlineButton("");
        view.getOnlineText().setText(isConnected ? "Online" : "Offline");
        isLoading = false;

        view.switchOnlineMode(isConnected);
    }
    private void closeConnection(){
        DbConnection.closeConnection();
        setOnlineButton("");
        view.getOnlineText().setText("Offline");

    }
    //if state = "" it just updates the button
    private void setOnlineButton(String state) {
        // Update button image to hover state
        Image hoverImage = new Image(
                ImageHelper.getButtonPath(connectedToDb(), state)
        );

        ImageView imageView = new ImageView(hoverImage);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        view.getOnline().setGraphic(imageView);
    }

    private void startConnectingAnimation() {
        // Cancel any existing timer first
        if (animationTimer != null) {
            animationTimer.cancel();
        }
        animationTimer = null;
        view.setOnlineButtonToConnecting();
        final String[] loadingTexts = {"Connecting.", "Connecting..", "Connecting..."};
        final int[] currentIndex = {0};

        // Create a timer that changes the text every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the text on the JavaFX Application Thread
                Platform.runLater(() -> {
                    view.getOnlineText().setText(loadingTexts[currentIndex[0]]);
                    // Move to the next state in the cycle
                    currentIndex[0] = (currentIndex[0] + 1) % loadingTexts.length;
                });
            }
        }, 0, 1000); // Start immediately, repeat every 1000ms (1 second)

        this.animationTimer = timer;
    }

    private void goToContinueGame() {
        ContinueView continueView = new ContinueView();
        view.getScene().setRoot(continueView);
        new ContinuePresenter(continueView);
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");

    }
}
