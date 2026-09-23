package org.example.view;

import org.example.Main;
import org.example.service.SecureStorageService;
import org.example.viewmodel.LoginViewModel;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LoginView {
    private final SecureStorageService secureStorageService;
    private final LoginViewModel viewModel;

    private final StackPane root;

    private TextField usernameField;
    private PasswordField passwordField;

    private Label errorLabel;

    public LoginView(SecureStorageService secureStorageService) {
        this.secureStorageService = secureStorageService;

        this.viewModel = new LoginViewModel(secureStorageService);

        root = createView();
    }

    public Parent getView() {
        return root;
    }

    private StackPane createView() {
        StackPane container = new StackPane();
        container.getStyleClass().add("login-background");

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        VBox loginCard = createLoginCard();
        content.getChildren().addAll(loginCard);

        container.getChildren().add(content);

        return container;
    }

    private VBox createLoginCard() {
        VBox appNameBox = new VBox();
        appNameBox.setAlignment(Pos.CENTER);

        Label appNameBold = new Label("Egypt Meters");
        Label appNameNormal = new Label("C o n t r o l    S y s t e m");

        appNameBold.getStyleClass().add("app-title");
        appNameNormal.getStyleClass().add("normal-app-title");

        appNameBox.getChildren().addAll(appNameBold, appNameNormal);

        VBox card = new VBox(4);

        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(450);
        card.getStyleClass().add("login-card");

        Label usernameLabel = new Label("Username");
        usernameField = new TextField();

        Label passwordLabel = new Label("Password");
        passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.getStyleClass().add("primary-button");

        VBox errorBox = new VBox();
        errorBox.setAlignment(Pos.CENTER);

        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");

        errorBox.getChildren().addAll(errorLabel);

        loginButton.setOnAction(event -> handleLogin());

        passwordField.setOnAction(event -> handleLogin());

        card.getChildren().addAll(
                appNameBox,
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                errorBox,
                loginButton
        );

        return card;
    }

    private void handleLogin() {

        String username = usernameField.getText();

        String password = passwordField.getText();

        if (username == null || username.isBlank()) {
            showError("Username is required");
            return;
        }

        if (password == null || password.isBlank()) {
            showError("Password is required");
            return;
        }

        boolean authenticated = viewModel.login(username, password);

        if (!authenticated) {
            showError("Invalid username or password");
            usernameField.clear();
            passwordField.clear();
            return;
        }

        showHomeScreen();
    }

    private void showHomeScreen() {
        HomeView homeView = new HomeView(secureStorageService);
        Main.getPrimaryStage().getScene().setRoot(homeView.getView());
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}