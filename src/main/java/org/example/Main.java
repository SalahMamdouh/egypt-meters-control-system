package org.example;

import org.example.service.SecureStorageService;
import org.example.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static Stage primaryStage;

    private static final double MIN_WIDTH = 900;
    private static final double MIN_HEIGHT = 600;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        primaryStage.setTitle("EMCS");

        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        SecureStorageService secureStorageService = new SecureStorageService();

        showLoginScreen(secureStorageService);

        primaryStage.show();
    }

    public static void showLoginScreen(SecureStorageService secureStorageService) {
        LoginView loginView = new LoginView(secureStorageService);

        Scene scene = new Scene(loginView.getView(), 1100, 700);

        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/css/app.css")).toExternalForm());

        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}