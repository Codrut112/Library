package controller;



import domain.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.Service;
import utils.MessageAlert;

import java.io.IOException;

public class LoginController implements Controller {
    public TextField textUserName;
    public PasswordField textPassword;
    public TextField passwordViewText;
    public CheckBox passwordButtonLogin;
    private Service service;



    public LoginController() {
    }

    public void loginUser() {
        String username = textUserName.getText();
        String password = textPassword.getText();
        passwordViewText.clear();
        textPassword.clear();

        try {
            Person user = service.verifyUser(username, password);

            if (user.getClass().getName().contains("Subscriber")) {

                Stage mainStage = new Stage();
                mainStage.setTitle("Main");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/main.fxml"));
                AnchorPane newRoot = (AnchorPane) loader.load();
                Scene scene = new Scene(newRoot);

                mainStage.setScene(scene);
                mainStage.setResizable(true);
                // Pass the service and user to the controller
                MainController userController = loader.getController();
                userController.setService(service, user);
                service.addObserver(userController);
                System.out.println("gata sa afiseze");
                mainStage.show();
                System.out.println("dada");

            }
            else if(user.getClass().getName().contains("Librarian")){
                Stage mainStage = new Stage();
                mainStage.setTitle("Admin");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/admin.fxml"));
                AnchorPane newRoot = (AnchorPane) loader.load();
                Scene scene = new Scene(newRoot);

                mainStage.setScene(scene);
                mainStage.setResizable(true);
                // Pass the service and user to the controller
                AdminController userController = loader.getController();
                userController.setService(service, user);
                service.addObserver(userController);
                System.out.println("gata sa afiseze");
                mainStage.show();
                System.out.println("dada");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MessageAlert.showErrorMessage(null, "login fail");
        }
    }




    public void addUser(ActionEvent actionEvent) throws IOException {

        Stage newStage = new Stage();
        newStage.setTitle("Register");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/EditUser.fxml"));
        AnchorPane newRoot = (AnchorPane) loader.load();
        Scene newScene = new Scene(newRoot);
        newStage.setScene(newScene);
        RegisterController editUserController = loader.getController();
        editUserController.setService(service, null);
        newStage.show();
    }

    public void managePassword(ActionEvent actionEvent) {
        if (passwordButtonLogin.isSelected()) {
            passwordViewText.setVisible(true);
            textPassword.setVisible(false);
            passwordViewText.setText(textPassword.getText());
        } else {
            passwordViewText.setVisible(false);
            textPassword.setVisible(true);
            textPassword.setText(passwordViewText.getText());

        }

    }

    public void setService(Service serviceApp,Person user) {
        this.service = serviceApp;
    }
}
