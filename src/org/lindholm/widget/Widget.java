package org.lindholm.widget;

import javafx.stage.*;

public class Widget {
	
	protected Stage stage;
	
	public Widget(Stage primaryStage) {
		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.setMaxWidth(0);
		primaryStage.setMaxHeight(0);
		primaryStage.setX(Double.MAX_VALUE);
		primaryStage.show();
		
		stage = new Stage() {
			
			@Override
			public void close() {
				super.close();
				primaryStage.close();
			}
			
		};
		stage.initOwner(primaryStage);
	}
	
	
}