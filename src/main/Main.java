package main;

import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class Main extends Application {
    private static User currentUser;
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	this.primaryStage = primaryStage;
    	primaryStage.setTitle("CaLouselF");
    	showLoginView();
    	primaryStage.show();
    }
    
    private void showLoginView() {
        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("CaLouselF");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        
        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(300);
        
        Button registerButton = new Button("Register");
        registerButton.setMaxWidth(300);
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (username.equals("admin") && password.equals("admin")) {
                currentUser = new User("admin", "admin", "admin", "", "", "admin");
//                showAdminView();
                return;
            }
            
            User user = UserController.login(username, password);
            if (user != null) {
                currentUser = user;
                if (user.getRole().equals("seller")) {
//                    showSellerView();
                } else {
//                    showBuyerView();
                }
            } else {
                messageLabel.setText("Invalid username or password");
            }
        });
        
        registerButton.setOnAction(e -> showRegisterView());
        
        loginBox.getChildren().addAll(
            titleLabel,
            new Label("Username:"),
            usernameField,
            new Label("Password:"),
            passwordField,
            loginButton,
            registerButton,
            messageLabel
        );
        
        Scene scene = new Scene(loginBox, 800, 600);
        primaryStage.setScene(scene);
    }
    
    private void showRegisterView() {
        VBox registerBox = new VBox(10);
        registerBox.setPadding(new Insets(20));
        registerBox.setAlignment(Pos.CENTER);
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username (min 3 characters)");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (min 8 characters, include special char)");
        
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number (+62...)");
        
        TextArea addressField = new TextArea();
        addressField.setPromptText("Address");
        addressField.setPrefRowCount(3);
        
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton sellerButton = new RadioButton("Seller");
        RadioButton buyerButton = new RadioButton("Buyer");
        sellerButton.setToggleGroup(roleGroup);
        buyerButton.setToggleGroup(roleGroup);
        
        HBox roleBox = new HBox(20);
        roleBox.getChildren().addAll(sellerButton, buyerButton);
        
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String role = ((RadioButton) roleGroup.getSelectedToggle()).getText().toLowerCase();
            
            String result = UserController.registerUser(username, password, phone, address, role);
            if (result.equals("Success")) {
                showLoginView();
            } else {
                messageLabel.setText(result);
            }
        });
        
        backButton.setOnAction(e -> showLoginView());
        
        registerBox.getChildren().addAll(
            new Label("Username:"),
            usernameField,
            new Label("Password:"),
            passwordField,
            new Label("Phone Number:"),
            phoneField,
            new Label("Address:"),
            addressField,
            new Label("Role:"),
            roleBox,
            registerButton,
            backButton,
            messageLabel
        );
        
        Scene scene = new Scene(registerBox, 800, 600);
        primaryStage.setScene(scene);
    }
    
    // Other view methods would go here (showAdminView, showSellerView, showBuyerView)
    
    public static void main(String[] args) {
        launch(args);
    }

}