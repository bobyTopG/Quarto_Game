package be.kdg.quarto.helpers;

import java.sql.SQLException;

public class ErrorHelper {

    public static void showDBError(SQLException error) {
        CreateHelper.showAlert("Database Error", error.getMessage(),true);
    }

    public static void showError(Exception error, String title) {
        CreateHelper.showAlert(title, error.getMessage(),true);
    }
}
