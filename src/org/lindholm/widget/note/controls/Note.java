package org.lindholm.widget.note.controls;

import org.lindholm.widget.note.NoteWidget;
import org.lindholm.widget.note.graphics.*;

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;

public class Note extends VBox {
	
	String name;
	String color;
	
	EventHandler<ActionEvent> actionHandler;
	EnchantedTextField addItemField;
	
	public Note(String name, String color) {
		this.name = name;
		this.color = color;
		
		super.getStyleClass().add("note");
		super.getStyleClass().add(color);
		
		buildHeader();
		buildFooter();
	}
	
	void buildHeader() {
		HBox hBox = new HBox();
		hBox.getStyleClass().add("note-header");
		
		EnchantedTextField nameField = new EnchantedTextField();
		nameField.setText(name);
		nameField.setAllowedEmpty(false);
		nameField.getStyleClass().add("name-label");
		nameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				if (
					event.getCode().equals(KeyCode.ESCAPE) ||
					event.getCode().equals(KeyCode.ENTER)
				) {
					name = nameField.getText();
					hBox.requestFocus();
				}
			}
			
		});
		
		ColorPicker colorBox = new ColorPicker(NoteWidget.COLORS, color);
		colorBox.getStyleClass().add("color-picker");
		colorBox.valueProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Note.super.getStyleClass().remove(oldValue);
				Note.super.getStyleClass().add(newValue);
				color = newValue;
				
				hBox.requestFocus();
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
		
		hBox.getChildren().addAll(nameField, colorBox, closeButton);
		super.getChildren().add(hBox);
	}
	void buildFooter() {
		HBox hBox = new HBox();
		hBox.getStyleClass().add("item");
		
		addItemField = new EnchantedTextField();
		addItemField.setEmptyDisplayLabel("Add item...");
		addItemField.getStyleClass().add("name-label");
		HBox.setHgrow(addItemField, Priority.ALWAYS);
		addItemField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ESCAPE)) {
					hBox.requestFocus();
				}
			}
			
		});
		addItemField.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String label = addItemField.getText();
						
				if (!label.isEmpty()) {
					addItem(label, false);
					addItemField.setText("");
				}
			}
			
		});
		
		hBox.getChildren().add(addItemField);
		super.getChildren().add(hBox);
	}
	
	public String getName() {
		return name;
	}
	public String getColor() {
		return color;
	}
	
	public void setOnAction(EventHandler<ActionEvent> actionHandler) {
		this.actionHandler = actionHandler;
	}
	
	public Item addItem(String name, boolean checked) {
		ObservableList<Node> children = super.getChildren();
		
		Item item = new Item(name, checked);
		item.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				children.remove(item);
				Note.super.requestFocus();
			}
		});
		children.add(children.size() - 1, item);
		
		return item;
	}
	public Item[] getItems() {
		ObservableList<Node> children = super.getChildren();
		
		return children.subList(1, children.size() - 1).toArray(new Item[children.size() - 2]);
	}
	
	public void close() {
		if (actionHandler != null) {
			CloseTransition transition = new CloseTransition(Note.this, actionHandler);
			transition.play();
		}
	}
	
	@Override
	public void requestFocus() {
		addItemField.requestFocus();
	}
	
}