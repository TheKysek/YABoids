package net.YABoids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.YABoids.geometry.Vector;

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
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        board = new Board(bounds.getWidth() * 0.9, bounds.getHeight() * 0.9, 150);
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

        primaryStage.setResizable(false);

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

                if (running)
                {
                    tick();
                }
                gc.setFill(Color.BLACK);
                gc.fillText(String.valueOf(1000000000 / (now - then)), 15, 10);
                then = now;
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

    private void initCanvas()
    {
        canvas = new Canvas(board.getWidth(), board.getHeight());

        canvas.setOnMouseMoved(event -> board.getScare().set(event.getX(), event.getY()));

        gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LINEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFont(new Font(20));
        gc.setTextBaseline(VPos.TOP);
    }

    private void drawGrid()
    {
        gc.setStroke(Color.LIGHTGRAY);

        for (int i = 0; i <= board.cellsVertically; i++)
        {
            double y = i * Boid.VIEW_DISTANCE;
            gc.strokeLine(0, y, board.width, y);
        }

        for (int i = 0; i <= board.cellsHorizontally; i++)
        {
            double x = i * Boid.VIEW_DISTANCE;
            gc.strokeLine(x, 0, x, board.height);
        }
    }

    private void drawBoids()
    {
        gc.setFill(Color.LINEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawGrid();

        board.getBoids().forEach(this::drawBoid);

        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(board.getScare().getX() - 10, board.getScare().getY() - 10, 20, 20);
    }

    private void drawBoid(Boid boid)
    {
        gc.setFill(Color.THISTLE);
        gc.fillOval(boid.getX() - 10, boid.getY() - 10, 20, 20);

        gc.setFill(Color.MEDIUMPURPLE);
        Vector velocity = boid.getVelocity();
        gc.fillOval(boid.getX() +  2 * velocity.getLength() * Math.cos(velocity.getRotationRad()) - 3, boid.getY() +  2 * velocity.getLength() * Math.sin(velocity.getRotationRad()) - 3, 6, 6);
    }

    private void tick()
    {
        board.moveBoids();
        drawBoids();
    }

}