package controller;

import domain.Book;
import domain.BookStatus;
import domain.Genre;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Service;
import utils.MessageAlert;

public class AddBookController {

    @FXML
    private TextField textISBN;
    @FXML
    private TextField textAuthor;
    @FXML
    private TextField textTitle;
    @FXML
    private TextField textPublicationYear;

    @FXML
    private TextField textGenre;

    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void handleAddBook() {
        String isbn = textISBN.getText();
        String author = textAuthor.getText();
        String title = textTitle.getText();
        String publicationYear = textPublicationYear.getText();
        String genre = textGenre.getText();

        if (isbn.isEmpty() || author.isEmpty() || title.isEmpty() || publicationYear.isEmpty()  || genre.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Please fill in all fields");
            return;
        }



        service.addBook(isbn, author, title, Integer.parseInt(publicationYear), BookStatus.Available, Genre.valueOf(genre));
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Added", "Book added successfully");

        Stage stage = (Stage) textISBN.getScene().getWindow();
        stage.close();
    }
}
