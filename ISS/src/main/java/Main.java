import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import persistence.*;
import service.Service;

public class Main extends Application {
    private static SessionFactory sessionFactory;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUp();

        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("login.fxml"));
        System.out.println(getClass().getResource("login.fxml"));
        AnchorPane root = userLoader.load();

         Service serviceApp = createService();

        LoginController controller = userLoader.getController();
        controller.setService(serviceApp,null);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
       primaryStage.setTitle("Library Management System");
        primaryStage.show();
    }

    private static Service createService() {


        BookBorrowingRepository repositoryBookBorrowing = new BookBorrowingRepository(sessionFactory);
        BookRepository repositoryBook = new BookRepository(sessionFactory);
        LibrarianRepository repositoryLibrarian = new LibrarianRepository(sessionFactory);
        SubscriberRepository repositoryUser = new SubscriberRepository(sessionFactory);
        BookRateRepository repositoryBookRate = new BookRateRepository(sessionFactory);

        return new Service(repositoryUser, repositoryBook, repositoryBookBorrowing, repositoryBookRate, repositoryLibrarian);
    }

    private static void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
        tearDown();
    }
}
