package controller;


import domain.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.Service;
import utils.MessageAlert;

public class RegisterController {

    public TextField passwordText;
    public PasswordField passwordField;
    public CheckBox passwordButton;
    public Button addButton;
    public PasswordField phoneField;
    public TextField NameText;
    public TextField CNPText;
    public TextField usernameText;
    @FXML
    private Stage stage;
    private Service service;
    private Person user;

    public RegisterController() {
    }

    public void setService(Service service, Person user) {
        this.service = service;
     //   this.stage = newStage;
        this.user = user;

        passwordText.setVisible(false);

    }

    public void handleSave() {
        //check that the fields are not empty
        if (NameText.getText().isEmpty() || phoneField.getText().isEmpty() || CNPText.getText().isEmpty() || passwordField.getText().isEmpty() || usernameText.getText().isEmpty()) {
            MessageAlert.showErrorMessage(null, "Please complete all fields!");
            return;
        }
        String name = NameText.getText();
        String phone =phoneField.getText();
        String cnp =CNPText.getText();
        String password =passwordField.getText();
        String username =usernameText.getText();


        try {
            if (user == null) {
                addUser(name, phone,cnp,username,password);
                //stage.close();
                Stage stage= (Stage) usernameText.getScene().getWindow();
                stage.close();
            }
        } catch (RuntimeException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }




    //addUser
    private void addUser(String name, String phone, String cnp, String username, String password) {
        service.addSubscriber(username, password, name, cnp, phone);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "User added", "User added successfully");
    }

    public void handleCancel() {
        stage.close();
    }

    public void managePassword(ActionEvent mouse){
        if(passwordButton.isSelected()) {
            passwordField.setVisible(false);
            passwordText.setText(passwordField.getText());
            passwordText.setVisible(true);
        }
        else {
            passwordField.setVisible(true);
            passwordText.setVisible(false);
            passwordField.setText(passwordText.getText());

        }
    }


}
