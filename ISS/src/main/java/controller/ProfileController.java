package controller;

import domain.Book;
import domain.BookBorrowing;
import domain.BookRate;
import domain.Person;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;
import java.util.*;

public class ProfileController implements Controller, observer.Observer {
    public VBox listaDeCarti;
    public Pane bookView;
    public Label usernameLabel;
    private Service service;
    private Person user;
    private List<BookBorrowing> bookBorrowings;


    @Override
    public void setService(Service service, Person user) {
        this.service = service;
        this.user = user;
        usernameLabel.setText(user.getUsername());
        loadBooks();
    }

    private void loadBooks() {
        bookBorrowings = service.findUserBooks(user);
        System.out.println("numarul de carti imprumutate: " + bookBorrowings.size());
        List<Book> books = new ArrayList<>();
        for (BookBorrowing bookBorrowing : bookBorrowings) {
            books.addAll(bookBorrowing.getBooks());
        }
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
        VBox bookDetails = new VBox();

        Label titleLabel = new Label("Title: " + book.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffdcc2; -fx-font-size: medium");
        Label authorLabel = new Label("Author: " + book.getAuthor());
        authorLabel.setStyle("-fx-text-fill: #ffdcc2;");
        Button rateButton = new Button("Rate");
        HBox selectHBox = new HBox();
        selectHBox.setSpacing(5);
        bookDetails.getChildren().addAll(titleLabel, authorLabel, selectHBox);
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

        BookBorrowing bookBorrowing = getBorrowing(book);
        if (bookBorrowing instanceof BookRate) {
            Label ratingLabel = new Label("Grade:"+String.valueOf(((BookRate) bookBorrowing).getGrade()));
            bookDetails.getChildren().add(ratingLabel);
        } else {
            ComboBox<Integer> ratingComboBox = new ComboBox<>();
            rateButton.setOnAction(event -> {
                service.rateBook(Objects.requireNonNull(getBorrowing(book)), ratingComboBox.getValue());
                loadBooks();
            });
            for (int i = 1; i <= 10; i++) {
                ratingComboBox.getItems().add(i);
            }
            bookDetails.getChildren().add(ratingComboBox);
            bookDetails.getChildren().add(rateButton);
        }
        if (imageView != null) {
            imageVBox.getChildren().add(imageView);
        } else {
            Label errorLabel = new Label("Image not available");
            imageVBox.getChildren().add(errorLabel);
        }


        bookHBox.getChildren().addAll(imageVBox, bookDetails);
        bookHBox.setSpacing(10);

        return bookHBox;
    }

    private BookBorrowing getBorrowing(Book book) {
        for (BookBorrowing bookBorrowing : bookBorrowings) {
            if (bookBorrowing.getBooks().contains(book)) {
                return bookBorrowing;
            }
        }
        return null;
    }

    public void logOut(MouseEvent mouseEvent) {
        Stage currentStage = (Stage) usernameLabel.getScene().getWindow();
        currentStage.close();
    }

    public void goBackToMain(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main.fxml"));
        AnchorPane newRoot = (AnchorPane) loader.load();
        Scene scene = new Scene(newRoot);
        //load the profile on the current window
        Stage currentStage = (Stage) usernameLabel.getScene().getWindow();
        currentStage.setScene(scene);
        MainController profileController = loader.getController();
        profileController.setService(service, user);
    }


    @Override
    public void update() {
        loadBooks();
    }
}
