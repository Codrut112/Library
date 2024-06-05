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

public class UpdateBookController {

    @FXML
    private TextField textISBN;
    @FXML
    private TextField textAuthor;
    @FXML
    private TextField textTitle;
    @FXML
    private TextField textPublicationYear;
    @FXML
    private TextField textStatus;
    @FXML
    private TextField textGenre;

    private Service service;
    private String bookId;

    public void setService(Service service) {
        this.service = service;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
        loadBookDetails();
    }

    private void loadBookDetails() {
        Book book = service.findBookById(bookId);
        if (book != null) {
            textISBN.setText(book.getIsbn());
            textAuthor.setText(book.getAuthor());
            textTitle.setText(book.getTitle());
            textPublicationYear.setText(String.valueOf(book.getPublicationYear()));
            textStatus.setText(book.getStatus().name());
            textGenre.setText(String.valueOf(book.getGenre()));
        } else {
            MessageAlert.showErrorMessage(null, "Book not found");
            closeWindow();
        }
    }

    @FXML
    public void handleUpdateBook() {
        String isbn = textISBN.getText();
        String author = textAuthor.getText();
        String title = textTitle.getText();
        String publicationYear = textPublicationYear.getText();
        String status = textStatus.getText();
        String genre = textGenre.getText();

        if (isbn.isEmpty() || author.isEmpty() || title.isEmpty() || publicationYear.isEmpty() || status.isEmpty() || genre.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Please fill in all fields");
            return;
        }

        BookStatus bookStatus;
        try {
            bookStatus = BookStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            MessageAlert.showErrorMessage(null, "Invalid status");
            return;
        }

        Book updatedBook = new Book(isbn, author, title, Integer.parseInt(publicationYear), bookStatus, Genre.valueOf(genre));
        updatedBook.setId(bookId);
        service.updateBook(updatedBook);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Updated", "Book updated successfully");

        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) textISBN.getScene().getWindow();
        stage.close();
    }
}
