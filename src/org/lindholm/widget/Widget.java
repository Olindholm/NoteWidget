package org.lindholm.widget;

import javafx.event.*;
import javafx.stage.*;

public abstract class Widget {
	
	EventHandler<ActionEvent> closeHandler;
	
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
				if (closeHandler != null) {
					closeHandler.handle(new ActionEvent(this, null));
				}
				
				super.close();
				primaryStage.close();
			}
			
		};
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				
				// Consume event, and do nothing.
				event.consume();
				
			}
		});
		stage.initOwner(primaryStage);
	}
	
	public void setOnClose(EventHandler<ActionEvent> closeHandler) {
		this.closeHandler = closeHandler;
	}
	
	public void show() {
		stage.show();
	}
	
	public abstract void close();
	
}