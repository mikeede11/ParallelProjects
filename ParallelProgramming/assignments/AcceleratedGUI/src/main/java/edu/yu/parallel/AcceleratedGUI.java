package edu.yu.parallel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.swing.text.html.ImageView;
import java.util.ArrayList;

/**
 * This demo program uses a thread to compute an image "in the background".
 * As rows of pixels in the image are computed, they are copied to the
 * screen.  (The image is a small piece of the famous Mandelbrot set, which
 * is used just because it takes some time to compute.  There is no need
 * to understand what the image means.)  The user starts the computation by
 * clicking a "Start" button.  A separate thread is created and is run at
 * a lower priority, which will make sure that the GUI thread will get a
 * chance to run to repaint the display as necessary.  All changes to the
 * GUI from the animation thread are made by calling Platform.runLater().
 */

public class AcceleratedGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    //-----------------------------------------------------------------------------
    private int numOfThreads = 1;//default
    private ArrayList<Runner> runner = new ArrayList<>();
    private volatile boolean running;
    private static volatile int row;
    private static double referenceDuration = 12.789;//8.275;
    private long startTime;
    private long endTime;

    private Button startButton; // button the user can click to start or abort the thread

    private MenuButton dropDown;

    private Canvas canvas;      // the canvas where the image is displayed
    private GraphicsContext g;  // the graphics context for drawing on the canvas

    private Color[] palette;    // the color palette, containing the colors of the spectrum

    int width, height;          // the size of the canvas


    /**
     * Set up the GUI and event handling.  The canvas will be 1200-by-1000 pixels,
     * if that fits comfortably on the screen; otherwise, size will be reduced to fit.
     * This method also makes the color palette, containing colors in spectral order.
     */
    public void start(Stage stage) {

        palette = new Color[256];
        for (int i = 0; i < 256; i++)
            palette[i] = Color.hsb(360*(i/256.0), 1, 1);

        int screenWidth = (int)Screen.getPrimary().getVisualBounds().getWidth();
        int screenHeight = (int)Screen.getPrimary().getVisualBounds().getHeight();
        width = Math.min(1200,screenWidth - 50);
        height = Math.min(1000, screenHeight - 120);

        canvas = new Canvas(width,height);
        g = canvas.getGraphicsContext2D();
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0,0,width,height);
        startButton = new Button("Start!");
        startButton.setOnAction( e -> doStartOrStop() );
        dropDown = new MenuButton("Use 1 Thread");
        dropDown.getItems().addAll(new MenuItem("Use 1 Thread"),new MenuItem("Use 2 Threads"), new MenuItem("Use 3 Threads"), new MenuItem("Use 4 Threads"), new MenuItem("Use 5 Threads"), new MenuItem("Use 6 Threads"), new MenuItem("Use 7 Threads"), new MenuItem("Use 8 Threads"));
        for(int i = 0 ; i < 8; i++){
            final int num = i;
            dropDown.getItems().get(i).setOnAction(a -> numOfThreads = num + 1);
        }
        HBox bottom = new HBox(startButton, dropDown);
        bottom.setStyle("-fx-padding: 6px; -fx-border-color:black; -fx-border-width: 2px 0 0 0");
        bottom.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane(canvas);
        root.setBottom(bottom);
        root.setStyle("-fx-border-color:black; -fx-border-width: 2px");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Demo: Background Computation in a Thread");
        stage.setResizable(false);
        /*startTime = System.currentTimeMillis();
        running = true;
        Thread t1 = new Runner();
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();
        referenceDuration = (endTime - startTime)/1000.0;
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0,0,width,height);*/
        stage.show();

    }

    /**
     * This method is called from the animation thread when one row of pixels needs
     * to be added to the image.
     * @param rowNumber the row of pixels whose colors are to be set
     * @param colorArray an array of colors, one for each pixel
     */
    private void drawOneRow( int rowNumber, Color[] colorArray ) {
        for (int i = 0; i < width; i++) {
            // Color an individual pixel by filling in a 1-by-1 pixel
            // rectangle.  Not the most efficient way to do this, but
            // good enough for this demo.
            g.setFill(colorArray[i]);
            g.fillRect(i,rowNumber,1,1);
        }
    }


    /**
     * This method is called when the user clicks the Start button.
     * If no thread is running, it sets the signaling variable, running,
     * to true and creates and starts a new thread. Note that
     * the thread is responsible for changing the text on the button.
     * Note that the priority of the thread is set to be one less
     * than the priority of the thread that calls this method, that
     * is of the JavaFX application thread.  This means that the application
     * thread is run in preference to the computation thread.  When there is an
     * event to be handled, such as updating the display or reacting to a
     * button click, the event-handling thread should wake up immediately
     * to handle the event.
     */
    private void doStartOrStop() {
        if (running == false) { // create a thread and start it
            startButton.setDisable(true);  // will be re-enabled by the thread
            g.setFill(Color.LIGHTGRAY);
            g.fillRect(0,0,width,height);
            for(int i = 0; i < numOfThreads; i++){
                runner.add(i, new Runner());
            }
            try {
                for(int i = 0; i < numOfThreads; i++) {
                    runner.get(i).setPriority(Thread.currentThread().getPriority() - 1);
                }
            }
            catch (Exception e) {
            }
            running = true;  // Set the signal before starting the thread!
            startTime = System.currentTimeMillis();
            for(int i = 0; i < numOfThreads; i++){
                runner.get(i).start();
            }
        }
        else {  // stop the thread
            startButton.setDisable(true);  // will be re-enabled by the thread
            running = false;
            for(int i = 0; i < numOfThreads; i++){
                runner.add(i, null);
            }
        }
    }

    private void printPerformance(double duration){
        System.out.println("With 1 thread (out of " + Runtime.getRuntime().availableProcessors() + "), GUI rendering took " + referenceDuration);
        System.out.println("With " + numOfThreads + " threads (out of " + Runtime.getRuntime().availableProcessors() + "), GUI rendering took " + duration);
        System.out.println("Ratio to single threaded: " + duration/referenceDuration);
    }


    /**
     * This class defines the thread that does the computation.  The
     * run method computes the image one pixel at a time.  After computing
     * the colors for each row of pixels, the colors are copied into the
     * image, and the part of the display that shows that row is repainted.
     * All modifications to the GUI are made using Platform.runLater().
     * (Since the thread runs in the background, at lower priority than
     * the event-handling thread, the event-handling thread wakes up
     * immediately to repaint the display.)
     */
    private class Runner extends Thread {
        double xmin, xmax, ymin, ymax;
        int maxIterations;
        Runner() {
            xmin = -1.6744096740931858;
            xmax = -1.674409674093473;
            ymin = 4.716540768697223E-5;
            ymax = 4.716540790246652E-5;
            maxIterations = 10000;
        }
        public void run() {
            try {
                Platform.runLater( () -> startButton.setDisable(false) );
                Platform.runLater( () -> startButton.setText("Abort!") );
                double x, y;
                double dx, dy;
                dx = (xmax-xmin)/(width-1);
                dy = (ymax-ymin)/(height-1);
                for (row = 0; row < height;) {  // Compute one row of pixels.
                    final Color[] rgb = new Color[width];
                    y = ymax - dy*row;
                    for (int col = 0; col < width; col++) {
                        x = xmin + dx*col;
                        int count = 0;
                        double xx = x;
                        double yy = y;
                        while (count < maxIterations && (xx*xx + yy*yy) < 4) {
                            count++;
                            double newxx = xx*xx - yy*yy + x;
                            yy = 2*xx*yy + y;
                            xx = newxx;
                        }
                        if (count == maxIterations)
                            rgb[col] = Color.BLACK;
                        else
                            rgb[col] = palette[count%palette.length];
                        if (! running) {  // Check for the signal to abort the computation.
                            return;
                        }
                    }
                    final int rowNum = row;
                    synchronized (this){
                        row++;
                    }
                    Platform.runLater( () -> drawOneRow(rowNum,rgb) );
                }
            }
            finally {
                // Make sure the state is correct after the thread ends for any reason.
                Platform.runLater( () -> startButton.setText("Start Again") );
                Platform.runLater( () -> startButton.setDisable(false) );
                synchronized (this) {
                    if (running) {
                        endTime = System.currentTimeMillis();
                        printPerformance((endTime - startTime) / 1000.0);
                        running = false;
                    }
                }
                for(int i = 0; i < numOfThreads; i++){
                    runner.add(i, null);
                }

            }
        }
    }
}