package org.example.view;

import javafx.scene.control.*;
import org.example.Main;
import org.example.model.MeterField;
import org.example.service.SecureStorageService;
import org.example.viewmodel.HomeViewModel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Parent;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class HomeView {
    private final SecureStorageService secureStorageService;
    private final HomeViewModel viewModel;

    private final BorderPane root;

    private final TableView<MeterField> meterTable;
    private final Label meterStatusLabel;

    public HomeView(SecureStorageService secureStorageService) {
        this.secureStorageService = secureStorageService;
        this.viewModel = new HomeViewModel(secureStorageService);

        this.meterTable = new TableView<>();
        this.meterStatusLabel = new Label("Disconnected");

        root = createView();
    }

    public Parent getView() {
        return root;
    }

    private BorderPane createView() {
        BorderPane container = new BorderPane();
        container.getStyleClass().add("home-background");

        // =====================================================
        // TOP BAR
        // =====================================================

        container.setTop(createTopBar());

        // =====================================================
        // CENTER
        // =====================================================

        VBox centerContent = createMeterContent();

        container.setCenter(centerContent);

        return container;
    }

    // =========================================================
    // TOP BAR
    // =========================================================
    private HBox createTopBar() {

        HBox topBar = new HBox(12);

        topBar.setAlignment(Pos.BASELINE_CENTER);

        topBar.setPadding(new Insets(15, 20, 15, 20));

        topBar.getStyleClass().add("top-bar");

        Label appNameBold = new Label("Egypt Meters  |");
        Label appNameNormal = new Label("C o n t r o l   S y s t e m");

        appNameBold.getStyleClass().add("home-title");
        appNameNormal.getStyleClass().add("normal-home-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button connectMeterButton = new Button("Connect Meter");
        Button settingsButton = new Button("Settings");

        connectMeterButton.getStyleClass().add("secondary-button");
        connectMeterButton.setMinWidth(120);

        settingsButton.getStyleClass().add("secondary-button");
        settingsButton.setMinWidth(100);

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        connectMeterButton.setOnAction(event -> showMeterPasswordDialog());
        settingsButton.setOnAction(event -> showSettingsMenu(settingsButton));

        topBar.getChildren().addAll(
                appNameBold,
                appNameNormal,
                spacer,
                connectMeterButton,
                settingsButton
        );

        return topBar;
    }

    // =========================================================
    // SETTINGS MENU
    // =========================================================
    private void showSettingsMenu(Button settingsButton) {
        MenuItem changePasswordItem = new MenuItem("Change Password");
        MenuItem logoutItem = new MenuItem("Log out");

        changePasswordItem.setOnAction(event -> openChangePasswordDialog());

        logoutItem.setOnAction(event -> logout());

        ContextMenu settingsMenu = new ContextMenu();

        settingsMenu.getItems().addAll(
                changePasswordItem,
                logoutItem
        );

        settingsMenu.show(settingsButton, javafx.geometry.Side.BOTTOM, -5, 5);
    }

    // =========================================================
    // METER PASSWORD DIALOG
    // =========================================================
    private void showMeterPasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Meter Authentication");
        dialog.setHeaderText("Enter Meter Password");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Meter Password");
        passwordField.setPrefWidth(260);

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        VBox content = new VBox(12, passwordField, errorLabel);

        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(content);

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);

        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(
                confirmButtonType,
                cancelButtonType
        );

        Button confirmButton = (Button) dialog.getDialogPane()
                        .lookupButton(confirmButtonType);

        // =====================================================
        // PASSWORD VALIDATION
        // =====================================================

        confirmButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                    if (passwordField.getText().equals("EM26!@#$")) {
                        dialog.close();
                        connectMeter();
                    } else {
                        errorLabel.setText("Incorrect meter password.");
                        errorLabel.setVisible(true);
                        event.consume();
                    }
                }
        );

        dialog.showAndWait();
    }

    // =========================================================
    // METER CONTENT
    // =========================================================
    private VBox createMeterContent() {
        VBox content = new VBox(15);

        content.setPadding(new Insets(20));

        // =====================================================
        // STATUS
        // =====================================================

        HBox statusContainer = createMeterStatus();

        // =====================================================
        // TABLE
        // =====================================================

        createMeterTable();

        // =====================================================
        // GET READINGS BUTTON
        // =====================================================

        Button getReadingsButton = new Button("Get Readings");
        getReadingsButton.getStyleClass().add("primary-button");
        getReadingsButton.setMinWidth(130);

        getReadingsButton.setOnAction(event -> getReadings());

        HBox buttonContainer = new HBox(getReadingsButton);

        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        VBox.setVgrow(meterTable, Priority.ALWAYS);

        content.getChildren().addAll(
                statusContainer,
                meterTable,
                buttonContainer
        );

        return content;
    }

    // =========================================================
    // STATUS
    // =========================================================
    private HBox createMeterStatus() {
        Label statusTitle = new Label("Meter Status:");

        statusTitle.getStyleClass().add("status-title");

        meterStatusLabel.getStyleClass().add("status-disconnected");

        HBox statusContainer = new HBox(8, statusTitle, meterStatusLabel);

        statusContainer.setAlignment(Pos.CENTER_LEFT);

        return statusContainer;
    }

    // =========================================================
    // TABLE
    // =========================================================
    private void createMeterTable() {
        meterTable.setItems(viewModel.getMeterFields());

        meterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        meterTable.setPlaceholder(new Label("No meter data available"));

        // =====================================================
        // READ CHECKBOX
        // =====================================================

        TableColumn<MeterField, Boolean> selectColumn = new TableColumn<>("Read");

        selectColumn.setMinWidth(70);
        selectColumn.setMaxWidth(90);

        selectColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isSelected()));

        selectColumn.setCellFactory(column -> new TableCell<>() {
                    private final CheckBox checkBox = new CheckBox();

                    {
                        checkBox.setOnAction(event -> updateSelection());
                    }

                    private void updateSelection() {

                        if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                            return;
                        }

                        MeterField field = getTableView().getItems().get(getIndex());

                        field.setSelected(checkBox.isSelected());
                    }

                    @Override
                    protected void updateItem(Boolean selected, boolean empty) {
                        super.updateItem(selected, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            MeterField field = getTableView().getItems().get(getIndex());

                            checkBox.setSelected(field.isSelected());

                            setGraphic(checkBox);
                        }
                    }
                }
        );

        // =====================================================
        // DATA
        // =====================================================

        TableColumn<MeterField, String> nameColumn = new TableColumn<>("Data");

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        // =====================================================
        // DESCRIPTION
        // =====================================================

        TableColumn<MeterField, String> descriptionColumn = new TableColumn<>("Description");

        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        // =====================================================
        // VALUE
        // =====================================================

        TableColumn<MeterField, String> valueColumn = new TableColumn<>("Value");

        valueColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue()));

        // =====================================================
        // ADD COLUMNS
        // =====================================================

        meterTable.getColumns().addAll(
                selectColumn,
                nameColumn,
                descriptionColumn,
                valueColumn
        );
    }

    // =========================================================
    // CONNECT METER
    // =========================================================
    private void connectMeter() {
        boolean connected = viewModel.connectMeter();

        if (connected) {
            meterStatusLabel.setText("Connected");
            meterStatusLabel.getStyleClass().remove("status-disconnected");

            if (!meterStatusLabel.getStyleClass().contains("status-connected")) {
                meterStatusLabel.getStyleClass().add("status-connected");
            }

        } else {
            meterStatusLabel.setText("Disconnected");

            meterStatusLabel.getStyleClass().remove("status-connected");

            if (!meterStatusLabel.getStyleClass().contains("status-disconnected")) {
                meterStatusLabel.getStyleClass().add("status-disconnected");
            }
        }
    }

    // =========================================================
    // GET READINGS
    // =========================================================
    private void getReadings() {
        if (Objects.equals(meterStatusLabel.getText(), "Connected")) {
            viewModel.getSelectedReadings();
            meterTable.refresh();
        }
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================
    private void openChangePasswordDialog() {
        ChangePasswordDialog dialog = new ChangePasswordDialog(viewModel);
        dialog.showAndWait();
    }

    // =========================================================
    // LOGOUT
    // =========================================================
    private void logout() {

        viewModel.disconnectMeter();

        LoginView loginView =
                new LoginView(
                        secureStorageService
                );

        Main.getPrimaryStage()
                .getScene()
                .setRoot(
                        loginView.getView()
                );
    }
}