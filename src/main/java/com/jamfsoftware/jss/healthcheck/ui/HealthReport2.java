package com.jamfsoftware.jss.healthcheck.ui;

import com.jamfsoftware.jss.healthcheck.JavaToJavascriptBridge;
import com.jamfsoftware.jss.healthcheck.service.CalculatorService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HealthReport2 extends Application {

    private final String PAGE = "ViewResources/indexNew.html";


    @Override
    public void start(Stage primaryStage) {
        System.out.print("here2");
        createWebView(primaryStage, PAGE);
    }

    private void createWebView(Stage primaryStage, String page) {

        // create the JavaFX webview
        final WebView webView = new WebView();

        // connect the CalculatorService instance as "calculatorService"
        // javascript variable
        JavaToJavascriptBridge.connectBackendObject(
                webView.getEngine(),
                "calculatorService", new CalculatorService());

        // show "alert" Javascript messages in stdout (useful to debug)
        webView.getEngine().setOnAlert(e -> System.err.println("alertwb1: " + e.getData()));

        // load index.html
        webView.getEngine().load(
                getClass().getResource(page).
                        toExternalForm());

        primaryStage.setScene(new Scene(webView));
        primaryStage.setTitle("WebView with Java backend");
        primaryStage.show();
    }

    public static void launchHealthReport2(String[] args){
        System.setProperty("prism.lcdtext", "false"); // enhance fonts
        System.out.print("here");
        launch(args);
    }


}
