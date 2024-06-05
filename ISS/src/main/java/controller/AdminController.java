package controller;

import domain.Book;
import domain.BookStatus;
import domain.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import observer.Observer;
import service.Service;
import utils.MessageAlert;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController implements Controller, Observer {

    @FXML
    public TableColumn<Book, String> columnISBNAvailable;
    @FXML
    public TableColumn<Book, String> columnTitleAvailable;
    @FXML
    public TableColumn<Book, String> columnISBNreserved;
    @FXML
    public TableColumn<Book, String> columnTitleReserved;
    @FXML
    public TableColumn<Book, String> columnISBNBorrowed;
    @FXML
    public TableColumn<Book, String> columnTitleBorrowed;
    @FXML
    public TableView<Book> tableViewAvailable;
    @FXML
    public TableView<Book> tableViewReserved;
    @FXML
    public TableView<Book> tableViewBorrowed;
    public TextField textUsername;

    private Service service;
    private Person user;

    private ObservableList<Book> modelAvailableBook = FXCollections.observableArrayList();
    private ObservableList<Book> modelReservedBook = FXCollections.observableArrayList();
    private ObservableList<Book> modelBorrowedBook = FXCollections.observableArrayList();

    @FXML
    public void logOut(MouseEvent mouseEvent) {
        service.removeObserver(this);
        Stage stage = (Stage) tableViewAvailable.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setService(Service service, Person user) {
        this.service = service;
        this.user = user;
        loadData();
    }

    @FXML
    private void initialize() {
        initializeTableColumns(columnISBNBorrowed, columnTitleBorrowed);
        initializeTableColumns(columnISBNreserved, columnTitleReserved);
        initializeTableColumns(columnISBNAvailable, columnTitleAvailable);

        tableViewAvailable.setItems(modelAvailableBook);
        tableViewBorrowed.setItems(modelBorrowedBook);
        tableViewReserved.setItems(modelReservedBook);
    }

    private void initializeTableColumns(TableColumn<Book, String> isbnColumn,
                                        TableColumn<Book, String> titleColumn) {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    }

    private void loadData() {
        List<Book> allBooks = service.findAllBooks();

        initModel(modelAvailableBook, allBooks, BookStatus.Available);
        initModel(modelReservedBook, allBooks, BookStatus.Reserved);
        initModel(modelBorrowedBook, allBooks, BookStatus.Borrowed);
    }

    private void initModel(ObservableList<Book> model, List<Book> books, BookStatus status) {
        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getStatus().equals(status))
                .collect(Collectors.toList());
        model.setAll(filteredBooks);
    }

    @FXML
    private void borrowBook() {
        Book book = tableViewReserved.getSelectionModel().getSelectedItem();
        if (book == null) {
            MessageAlert.showErrorMessage(null, "No book selected");
            return;
        }
        var subscriber = service.findUserByUsername(textUsername.getText());
        if (subscriber == null) {
            MessageAlert.showErrorMessage(null, "User not found");
            return;
        }
        var reservation = service.verifyReservation(subscriber, book);
        if (reservation==null) {
            MessageAlert.showErrorMessage(null, "Reservation not found");
            return;
        }
        service.borrowBook(reservation,book);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Borrowed", "Book borrowed");
        loadData();
    }

    @FXML
    public void returnBook(ActionEvent actionEvent) {
        Book book = tableViewBorrowed.getSelectionModel().getSelectedItem();
        if (book == null) {
            MessageAlert.showErrorMessage(null, "No book selected");
            return;
        }
        service.returnBook(book);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Returned", "Book returned");
        //loadData();
    }

    @FXML
    public void addBook(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddBookForm.fxml"));
            VBox root = loader.load();
            AddBookController addBookController = loader.getController();
            addBookController.setService(service);

            Stage stage = new Stage();
            stage.setTitle("Add Book");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            //loadData();
        } catch (IOException e) {
            MessageAlert.showErrorMessage(null, "Failed to open the add book form");
        }
    }

    @FXML
    public void updateBook(ActionEvent actionEvent) {
        Book selectedBook = tableViewAvailable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            MessageAlert.showErrorMessage(null, "No book selected");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateForm.fxml"));
            VBox root = loader.load();
            UpdateBookController updateBookController = loader.getController();
            updateBookController.setService(service);
            updateBookController.setBookId(selectedBook.getId());

            Stage stage = new Stage();
            stage.setTitle("Update Book");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            //loadData();
        } catch (IOException e) {
            MessageAlert.showErrorMessage(null, "Failed to open the update book form");
        }
    }

    @FXML
    public void deleteBook(ActionEvent actionEvent) {
        Book selectedBook = tableViewBorrowed.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            MessageAlert.showErrorMessage(null, "No book selected");
            return;
        }
        service.deleteBook(selectedBook);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Deleted", "Book deleted successfully");
        //loadData();
    }

    @Override
    public void update() {
        loadData();
    }

    public void clearOldReservation(ActionEvent actionEvent) {
        service.clearOldReservations();
    }
}
