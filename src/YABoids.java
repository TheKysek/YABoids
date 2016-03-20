import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class YABoids extends Application
{
    private boolean running = true;

    private long then;

    private Board board;

    private Stage primaryStage;

    private VBox toolbar;

    private Canvas canvas;
    private GraphicsContext gc;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void init()
    {
        board = new Board(1800, 960, 100);
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        initGui();
    }

    private void initGui()
    {
        primaryStage.setTitle("YABoids");

        initCanvas();
        //initToolbar();

        HBox root = new HBox(0, canvas);
        root.setStyle("-fx-background-color: #FF00FF;");

        Scene primaryScene = new Scene(root);

        primaryStage.setScene(primaryScene);

        drawBoids();

        //primaryStage.setResizable(false);

        // Workaround, see: https://stackoverflow.com/questions/20732100/javafx-why-does-stage-setresizablefalse-cause-additional-margins
        primaryStage.sizeToScene();

        primaryStage.show();

        new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if (then == 0)
                {
                    then = now;
                    return;
                }

                //System.out.println(now - then);
                then = now;

                if (running)
                {
                    tick();
                }
            }
        }.start();

    }

    private void initToolbar()
    {
        toolbar = new VBox(20);

        toolbar.setPadding(new Insets(20));
        toolbar.setAlignment(Pos.TOP_CENTER);
        toolbar.setPrefWidth(250);
        toolbar.setStyle("-fx-background-color: #EEEEEE;");

        Button b_tick = new Button("tick()");
        b_tick.setOnAction(event -> tick());
        toolbar.getChildren().add(b_tick);

        toolbar.getChildren().add(new Label("Test!"));
        toolbar.getChildren().add(new Label("Test!"));

    }

    private void drawBoids()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        board.getBoids().forEach(this::drawBoid);

        gc.setFill(Color.RED);
        gc.fillOval(board.getScare().getX() - 10, board.getScare().getY() - 10, 20, 20);
    }

    private void initCanvas()
    {
        canvas = new Canvas(board.getWidth(), board.getHeight());

        canvas.setOnMouseMoved(event -> {
            board.getScare().setX(event.getX());
            board.getScare().setY(event.getY());
        });

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawBoid(Boid boid)
    {
        gc.setFill(Color.GREEN);
        gc.fillOval(boid.getX() - 10, boid.getY() - 10, 20, 20);
    }

    private void tick()
    {
        long start = System.currentTimeMillis();
        board.moveBoids();
        drawBoids();
        System.out.println(System.currentTimeMillis() - start);
    }

}