package ru.jader.xsdtool.gui;

import ru.jader.xsdtool.gui.controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

	public static FXMLLoader loader;

    public static void main(String[] args) throws Exception {
    	Thread.setDefaultUncaughtExceptionHandler(MainApplication::handleError);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
    	String fxmlFile = "/fxml/main.fxml";
        loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("xsdtool");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private static void handleError(Thread t, Throwable e) {
    	try {
    		e.printStackTrace();
    		MainController controller = loader.getController();
        	controller.logError(e);
    	} catch (Exception exception) {
    		exception.printStackTrace();
        	Platform.exit();
    	}
    }
}