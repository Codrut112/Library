package controller;
import java.util.List;

import domain.Book;
import domain.Person;
import domain.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import observer.Observer;
import service.Service;

import java.util.ArrayList;
import java.util.HashMap;

public class MainController implements Observer,Controller{
    @FXML
    private Label usernameLabel;

    @FXML
    private Pane bookView;

    @FXML
    private VBox listaDeCarti;

    private Service service;
    private Person user;
    private List<Book> selectedBooks = new ArrayList<>();
    private HashMap<Book, Label> bookLabels = new HashMap<>();



    public void setService(Service service, Person user) {
        this.service = service;
        this.user = user;
        usernameLabel.setText(user.getUsername());
        loadBooks();
    }

    private void loadBooks() {
        List<Book> books = service.findAvailableBooks();
        VBox container = new VBox();
        for (Book book : books) {
            HBox bookHBox = createBookHBox(book);
            container.getChildren().add(bookHBox);
        }
        bookView.getChildren().clear();
        bookView.getChildren().add(container);
    }

    private HBox createBookHBox(Book book) {
        HBox bookHBox = new HBox();
        VBox imageVBox = new VBox();

        ImageView imageView = null;
        try {
            Image image = new Image(book.getIsbn() + ".jpg");
            imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(250);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            // Provide a fallback image or display a placeholder image
            imageView = new ImageView(new Image("placeholder.jpg"));
        }

        Label titleLabel = new Label("Title: " + book.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffdcc2; -fx-font-size: medium");
        Label authorLabel = new Label("Author: " + book.getAuthor());
        authorLabel.setStyle("-fx-text-fill: #ffdcc2;");
        Label selectedLabel = new Label("Selected");
        selectedLabel.setStyle("-fx-text-fill: #ffdcc2;");
        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                selectedBooks.add(book);
                Label label = new Label(book.getTitle());
                listaDeCarti.getChildren().add(label);
                bookLabels.put(book, label);
            } else {
                selectedBooks.remove(book);
                Label label = bookLabels.remove(book);
                listaDeCarti.getChildren().remove(label);
            }
        });

        if (imageView != null) {
            imageVBox.getChildren().add(imageView);
        } else {
            Label errorLabel = new Label("Image not available");
            imageVBox.getChildren().add(errorLabel);
        }

        HBox selectHBox = new HBox(selectedLabel, checkBox);
        selectHBox.setSpacing(5);
        VBox bookDetails = new VBox(titleLabel, authorLabel, selectHBox);
        bookHBox.getChildren().addAll(imageVBox, bookDetails);
        bookHBox.setSpacing(10);

        return bookHBox;
    }

    public void reserveSelectedBooks(ActionEvent actionEvent) {
        try {
            if (user instanceof Subscriber) { // Check type before casting
                service.reserveBooks((Subscriber) user, selectedBooks);
                // Only clear selected books if reservation is successful
                selectedBooks.clear();
                listaDeCarti.getChildren().clear();
                bookLabels.clear();
                update();
            }
        } catch (Exception e) {
            System.err.println("Reservation failed: " + e.getMessage());
        }
    }

    public void logOut(MouseEvent mouseEvent) {
        service.removeObserver(this);
        Stage currentStage = (Stage) usernameLabel.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void update() {
        loadBooks();
    }
}
