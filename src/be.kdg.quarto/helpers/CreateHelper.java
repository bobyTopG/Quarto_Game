package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.view.StatisticsScreen.StatisticsPresenter;
import be.kdg.quarto.view.StatisticsScreen.StatisticsView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CreateHelper {
    public static Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    public static Button createButton(String text, String[] styleClasses) {
        Button button = new Button(text);
        button.getStyleClass().addAll(styleClasses);
        return button;
    }

    public static Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }
    public static Label createLabel(String text, String[] styleClasses) {
        Label label = new Label(text);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }
    public static VBox createVBox(String styleClass) {
        VBox vbox = new VBox();
        vbox.getStyleClass().add(styleClass);
        return vbox;
    }
    public static HBox createHBox(String styleClass) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add(styleClass);
        return hbox;
    }
    public static void createAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

    }
    //we use T instead of Parent to keep the View type instead of sending a generic View
    public static <T extends Parent> T createPopUp(T popupView, Parent mainView, String title, int width, int height) {
        Stage stage = new Stage();

        // Get the owner window directly from the main view
        if (mainView.getScene() != null && mainView.getScene().getWindow() != null) {
            stage.initOwner(mainView.getScene().getWindow());
        }

        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(popupView);

        // Copy stylesheets from the main view
        if (mainView.getScene() != null) {
            scene.getStylesheets().addAll(mainView.getScene().getStylesheets());
        }

        stage.setScene(scene);
        stage.setTitle(title);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(false);
        stage.show();

        // Return the popup view for further configuration
        return popupView;
    }}
