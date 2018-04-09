package de.jonas.schroeter;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.BigInteger;


public class Main extends Application {

    private static Pane root = new Pane();
    private static TextField[] fields = new TextField[3];
    private static boolean isStandard;

    public static void main(String[] args) {
        launch(args);
    }

    private static void setup(Stage primaryStage) {
        Scene scene = new Scene(root, 800, 500);
        primaryStage.getIcons().add(new Image("de/jonas/schroeter/img/icon.png"));
        primaryStage.setTitle("Fancy Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        root.setBackground(new Background(new BackgroundImage(new Image("de/jonas/schroeter/img/bg.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        root.setVisible(true);
        primaryStage.show();
        createStandartButtons();
        createStandartFields();
        isStandard = true;
        createMenu(true);
    }

    private static void createStandartButtons() {
        //int[][] positions = {{100, 10}, {150, 10}, {200, 10}, {250, 10}, {300, 10}, {350, 10}};
        int posY = 100;
        for(int i = 0; i < Operation.values().length; i++){
            if(Operation.values()[i].isSpecial()) continue;
            Operation o = Operation.values()[i];
            Button button = new Button(o.getName());
            button.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: transparent; -fx-border-color: black; -fx-background-color: DDDDDD");
            button.setLayoutX(10);
            button.setLayoutY(posY);
            button.setPrefSize(200, 20);
            button.setOnAction(event -> evaluate(o));
            root.getChildren().add(button);
            posY += 50;
        }
    }

    private static void createSpecialButtons() {
        int posY = 200;
        for(int i = 0; i < Operation.values().length; i++){
            if(Operation.values()[i].isSpecial()){
                Operation o = Operation.values()[i];
                Button button = new Button(o.getName());
                button.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: transparent; -fx-border-color: black; -fx-background-color: DDDDDD");
                button.setLayoutX(10);
                button.setLayoutY(posY);
                button.setPrefSize(200, 20);
                button.setOnAction(event -> evaluate(o));
                root.getChildren().add(button);
                posY += 50;
            }
        }
    }

    private static void createStandartFields() {
        for(int i = 0; i < fields.length; i++){
            fields[i] = new TextField();
            fields[i].setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: transparent; -fx-border-color: black;");
            fields[i].setPrefSize(100, 20);
            root.getChildren().add(fields[i]);
        }
        TextField textField = fields[0];
        textField.setLayoutX(250);
        textField.setLayoutY(200);
        textField.setPromptText("Input 1");

        textField = fields[1];
        textField.setLayoutX(450);
        textField.setLayoutY(200);
        textField.setPromptText("Input 2");

        textField = fields[2];
        textField.setLayoutX(350);
        textField.setLayoutY(250);
        textField.setEditable(false);
        textField.setPromptText("Output");
    }

    private static void createSpecialFields() {
        for(int i = 0; i < fields.length; i++){
            if(i == 1) continue;
            fields[i] = new TextField();
            fields[i].setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: transparent; -fx-border-color: black");
            fields[i].setPrefSize(100, 20);
            root.getChildren().add(fields[i]);
        }
        TextField textField = fields[0];
        textField.setLayoutX(250);
        textField.setLayoutY(200);
        textField.setPromptText("Input");

        textField = fields[2];
        textField.setLayoutX(250);
        textField.setLayoutY(250);
        textField.setEditable(false);
        textField.setPromptText("Output");
    }

    private static void updateGUI() {
        root.getChildren().clear();
        if(isStandard){
            createSpecialButtons();
            createSpecialFields();
        }
        else{
            createStandartButtons();
            createStandartFields();
        }
        createMenu(false);
        isStandard = !isStandard;
    }

    private static void createMenu(boolean firstActivation) {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-border-color: black; -fx-background-color: DDDDDD");
        Menu standardMenu = new Menu("Fancy Calculator");
        MenuItem exit = new MenuItem("Close");
        exit.setOnAction(event -> {
            createWindow("Closing...", 1, true);
        });
        standardMenu.getItems().add(exit);
        menuBar.getMenus().add(standardMenu);
        Menu operationMenu = new Menu("Operations");
        MenuItem item = new MenuItem();
        item.setOnAction(event -> updateGUI());
        operationMenu.getItems().add(item);
        menuBar.getMenus().add(operationMenu);
        root.getChildren().add(menuBar);
        if(firstActivation){
            item.setText("Single Number Operations");
            return;
        }
        if(isStandard){
            item.setText("Double Number Operations");
        }
        else{
            item.setText("Single Number Operations");
        }
    }

    private static void evaluate(Operation operation) {
        double d1;
        double d2;
        try{
            d1 = Double.parseDouble(fields[0].getText());
            if(!operation.isSpecial()){
                d2 = Double.parseDouble(fields[1].getText());
            }
            else{
                d2 = -1;
            }
        }
        catch(NumberFormatException e){
            createErrorWindow("Please input numbers\nonly.", 1);
            return;
        }
        if(d2 == 0.0 && operation.equals(Operation.DIVISION)){
            createErrorWindow("   Divisor musn't be\nzero!", 1);
            return;
        }
        double d3 = -1;
        switch(operation){
            case ADDITION:
                d3 = d1 + d2;
                break;
            case SUBTRACTION:
                d3 = d1 - d2;
                break;
            case MULTIPLICATION:
                d3 = d1 * d2;
                break;
            case DIVISION:
                d3 = d1 / d2;
                break;
            case INTERVALADDITION:
                d3 = intervalAddition(d1, d2);
                break;
            case FACTORIAL:
                fields[2].setText(factorial((int) d1).toString());
                break;
            case POWER:
                BigDecimal b = power(d1, d2);
                if(b.remainder(new BigDecimal("1")).compareTo(new BigDecimal("0")) == 0){
                    fields[2].setText(b.toBigInteger().toString());
                }
                else{
                    fields[2].setText(b.toString());
                }
                break;
            case ROOT:
                b = new BigDecimal(String.valueOf(Math.sqrt(d1)));
                if(b.remainder(new BigDecimal("1")).compareTo(new BigDecimal("0")) == 0){
                    fields[2].setText(b.toBigInteger().toString());
                }
                else{
                    fields[2].setText(b.toString());
                }
                break;
        }
        if(operation == Operation.POWER){
            return;
        }
        if(!operation.isSpecial()){
            if(d3 % 1 == 0){
                fields[2].setText(String.valueOf((int) d3));
                return;
            }
            fields[2].setText(String.valueOf(d3));
        }
    }

    private static double intervalAddition(double d1, double d2) {
        double d3 = 0;
        for(double d = d1; d <= d2; d++){
            d3 += d;
        }
        return d3;
    }

    private static BigInteger factorial(int x) {
        if(x == 1) return new BigInteger("1");
        return factorial(x - 1).multiply(new BigInteger(String.valueOf(x)));
    }

    private static BigDecimal power(double d, double to) {
        return new BigDecimal(String.valueOf(d)).pow((int) to);
    }

    private static void createErrorWindow(String message, double seconds) {
        Pane pane = new Pane();
        pane.setVisible(true);
        Scene scene = new Scene(pane, 200, 100);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setTitle("Problem");
        stage.getIcons().add(new Image("de/jonas/schroeter/img/error.png"));
        Text text = new Text();
        text.setText(message);
        text.setVisible(true);
        text.setLayoutY(45);
        text.setLayoutX(10);
        text.setStyle("-fx-font-size: 19; -fx-text-alignment: center");
        pane.getChildren().add(text);

        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(event -> stage.close());
        delay.play();
    }

    private static void createWindow(String message, double seconds, boolean close) {
        Pane pane = new Pane();
        pane.setVisible(true);
        Scene scene = new Scene(pane, 150, 50);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setTitle("Quit");
        stage.getIcons().add(new Image("de/jonas/schroeter/img/icon.png"));
        Text text = new Text();
        text.setVisible(true);
        text.setLayoutY(35);
        text.setLayoutX(38);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-font-size: 19;");
        text.setText(message);
        pane.getChildren().add(text);

        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(event -> {
            if(close) System.exit(0);
        });
        delay.play();
        stage.setOnCloseRequest(event -> delay.stop());
    }

    @Override
    public void start(Stage primaryStage) {
        setup(primaryStage);
    }
}
