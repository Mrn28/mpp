import Controller.LoginController;
import Repository.*;
import Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TurismFXMain extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = fxmlLoader.load();

        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        UserRepository userRepository = new UserDBRepository(props);
        ZborRepository zborRepository = new ZborDBRepository(props);
        BiletRepository biletRepository = new BiletDBRepository(props, zborRepository);

        Service service = new Service(userRepository, biletRepository, zborRepository);

        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
