import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class YABoids extends Application
{
    Board board;

    Stage primaryStage;
    Scene primaryScene;

    VBox toolbar;

    Canvas canvas;

    //public static void main(String[] args)
    //{
    //    System.out.println("Hello World!");
    //}

    public static void main(String[] args)
    {
        launch(args);
    }

    public void init()
    {
        System.out.println("Hello World!");
        board = new Board(750, 600);
        board.addBoid(new Boid(100, 100, board.getBoids()));
        board.addBoid(new Boid(150, 150, board.getBoids()));
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        initGui();

    }

    void initGui()
    {
        primaryStage.setTitle("YABoids");

        initCanvas();
        initToolbar();

        HBox root = new HBox(0, canvas, toolbar);
        //root.setPrefWidth(900);
        root.setStyle("-fx-background-color: #FF00FF;");

        primaryScene = new Scene(root);

        primaryStage.setScene(primaryScene);

        drawBoids();

        primaryStage.setResizable(false);

        primaryStage.show();

    }

    void initToolbar()
    {
        toolbar = new VBox(0);

        toolbar.getChildren().add(new Label("Test!"));

        toolbar.setPrefWidth(250);
        toolbar.setStyle("-fx-background-color: #FFFF00;");
    }

    void drawBoids()
    {
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        board.getBoids().forEach(this::drawBoid);
    }

    void initCanvas()
    {
        canvas = new Canvas(board.getWidth(), board.getHeight());
    }

    void drawBoid(Boid boid)
    {
        canvas.getGraphicsContext2D().fillOval(boid.getX(), boid.getY(), 10, 10);
    }
}