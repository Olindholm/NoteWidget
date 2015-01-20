package org.lindholm.widget.note.controls;

import javafx.animation.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class Item extends HBox {
	
	boolean checked;
	String name;
	
	EventHandler<ActionEvent> actionHandler;
	
	public Item(String name, boolean checked) {
		this.checked = checked;
		this.name = name;
		
		super.getStyleClass().add("item");
		buildInterface();
	}
	
	void buildInterface() {
		CheckBox checkbox = new CheckBox();
		checkbox.getStyleClass().add("item-box");
		checkbox.setSelected(checked);
		
		EnchantedTextField nameField = new EnchantedTextField();
		nameField.setText(name);
		nameField.setAllowedEmpty(false);
		nameField.getStyleClass().add("name-label");
		HBox.setHgrow(nameField, Priority.ALWAYS);
		nameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				if (
					event.getCode().equals(KeyCode.ESCAPE) ||
					event.getCode().equals(KeyCode.ENTER)
				) {
					name = nameField.getText();
					Item.this.requestFocus();
				}
			}
			
		});
		checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					nameField.getStyleClass().add("checked");
				} else {
					nameField.getStyleClass().remove("checked");
				}
				
				checked = newValue;
			}
			
		});
		
		Button closeButton = new Button();
		closeButton.getStyleClass().add("close-button");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				close();
			}
			
		});
		
		super.getChildren().addAll(checkbox, nameField, closeButton);
	}
	
	public void setOnAction(EventHandler<ActionEvent> actionHandler) {
		this.actionHandler = actionHandler;
	}
	
	public void close() {
		if (actionHandler != null) {
			
			Item.this.setMinHeight(0);
			Item.this.setPrefHeight(Item.this.getHeight());
			
			Timeline heightTransition = new Timeline();
			heightTransition.getKeyFrames().addAll(new KeyFrame(Duration.millis(300), new KeyValue(Item.this.prefHeightProperty(), 0)), new KeyFrame(Duration.millis(300), new KeyValue(Item.this.scaleYProperty(), 0)));
			heightTransition.setOnFinished(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					actionHandler.handle(new ActionEvent(Item.this, event.getTarget()));
				}
			});
			
			heightTransition.play();
		}
	}
	
}