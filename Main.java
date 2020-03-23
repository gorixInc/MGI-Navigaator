import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("GPS");

        stage.setWidth(700);
        stage.setHeight(500);

        stage.setX(300);
        stage.setY(100);

        VBox root = new VBox();

        Button nupp1 = new Button("New route");
        Button nupp2 = new Button("Load Map");

        Image pilt = new Image("http://www.thepluspaper.com/wp-content/uploads/2019/01/1.jpg");

        BackgroundImage taust = new BackgroundImage(pilt, null, null,null,null);

        nupp1.setOnAction(e -> {
            System.out.println("Esimene Töötab");
        });

        nupp2.setOnAction(e -> {
            System.out.println("Teine Töötab");
        });

        root.getChildren().add(nupp1);
        root.getChildren().add(nupp2);

        Scene scene1 = new Scene(root);

        root.setBackground(new Background(taust));

        stage.setScene(scene1);

        stage.show();
    }
    public static void main(String[] args) { launch(args);}
}