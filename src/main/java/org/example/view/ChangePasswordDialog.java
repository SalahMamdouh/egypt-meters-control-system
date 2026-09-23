package org.example.view;

import org.example.viewmodel.HomeViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class ChangePasswordDialog {
    private final HomeViewModel viewModel;

    private final Stage dialog;

    private PasswordField oldPasswordField;
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;

    private Label errorLabel;

    public ChangePasswordDialog(HomeViewModel viewModel) {
        this.viewModel = viewModel;
        dialog = new Stage();
        dialog.setTitle("Change Password");
        dialog.initModality(Modality.APPLICATION_MODAL);
        createContent();
    }

    private void createContent() {
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(25));

        oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Current password");
        oldPasswordField.setPrefWidth(250);

        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New password");
        newPasswordField.setPrefWidth(250);

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        confirmPasswordField.setPrefWidth(250);

        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");

        Button changeButton = new Button("Change Password");
        changeButton.getStyleClass().add("primary-button");
        changeButton.setMaxWidth(Double.MAX_VALUE);

        changeButton.setOnAction(event -> handleChangePassword());

        grid.add(new Label("Old Password"), 0, 0);
        grid.add(oldPasswordField, 1, 0);

        grid.add(new Label("New Password"), 0, 1);
        grid.add(newPasswordField, 1, 1);

        grid.add(new Label("Confirm Password"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        grid.add(errorLabel, 0, 3, 2, 1);

        grid.add(changeButton, 0, 4, 2, 1);

        dialog.setScene(new javafx.scene.Scene(grid, 450, 300));

        dialog.getScene().getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/css/app.css")).toExternalForm());
    }

    private void handleChangePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (oldPassword == null || oldPassword.isBlank()) {
            showError("Old password is required");
            return;
        }

        if (newPassword == null || newPassword.isBlank()) {
            showError("New password is required");
            return;
        }

        if (confirmPassword == null || confirmPassword.isBlank()) {
            showError("Confirm password is required");
            return;
        }

        if (!viewModel.isCurrentPassword(oldPassword)) {
            showError("Old password is incorrect");
            return;
        }

        if (oldPassword.equals(newPassword)) {
            showError("New password must be different from old password");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        viewModel.updatePassword(newPassword);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Password Changed");
        alert.setHeaderText(null);
        alert.setContentText("Password changed successfully.");

        alert.showAndWait();

        dialog.close();
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }

    public void showAndWait() {
        dialog.showAndWait();
    }
}