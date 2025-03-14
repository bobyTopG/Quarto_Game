package be.kdg.quarto.helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
}
