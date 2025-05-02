package be.kdg.quarto.helpers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    public static ToggleButton createToggleButton(String text, String[] styleClasses) {
        ToggleButton button = new ToggleButton(text);
        button.getStyleClass().addAll(styleClasses);
        return button;
    }

    public static Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
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

    public static void showAlert(String title, String message, boolean isError) {
        Alert alert = new Alert(isError ? Alert.AlertType.WARNING : Alert.AlertType.INFORMATION);
        alert.setTitle(isError ? "Error" : "");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
