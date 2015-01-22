package org.lindholm.widget.note;

import javafx.event.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;

import org.lindholm.widget.Widget;
import org.lindholm.widget.note.controls.*;
import org.lindholm.widget.note.controls.ColorPicker;
import org.lindholm.widget.note.graphics.CloseTransition;

public class NoteWidget extends Widget {
	
	public static final String[] COLORS = {"grey", "pink", "red", "orange", "yellow", "green", "teal", "blue"};
	
	boolean pinned;
	String color;
	
	VBox root;
	
	public NoteWidget(Stage primaryStage, boolean pinned, String color) {
		super(primaryStage);
		this.pinned = pinned;
		this.color = color;
		
		root = new VBox();
		root.getStyleClass().add("widget");
		root.getStyleClass().add(color);
		
		Scene scene = new Scene(root, 300, Screen.getPrimary().getBounds().getHeight(), Color.TRANSPARENT);
		scene.getStylesheets().add(super.getClass().getResource("graphics/Colors.css").toString());
		scene.getStylesheets().add(super.getClass().getResource("graphics/NoteWidget.css").toString());
		
		
		buildInterface();
		initStage(scene);
	}
	
	void buildInterface() {
		HBox hBox = new HBox();
		hBox.getStyleClass().add("note-header");
		
		EnchantedTextField addNoteField = new EnchantedTextField();
		addNoteField.setEmptyDisplayLabel("Add note...");
		addNoteField.getStyleClass().add("name-label");
		addNoteField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ESCAPE)) {
					hBox.requestFocus();
				}
			}
			
		});
		addNoteField.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String label = addNoteField.getText();
						
				if (!label.isEmpty()) {
					addNoteField.setText("");
					addNote(label, color);
				}
			}
			
		});
		
		ColorPicker colorBox = new ColorPicker(NoteWidget.COLORS, color);
		colorBox.getStyleClass().add("color-picker");
		colorBox.valueProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				root.getStyleClass().remove(oldValue);
				root.getStyleClass().add(newValue);
				color = newValue;
				
				root.requestFocus();
			}
			
		});
		
		ToggleButton pinButton = new ToggleButton();
		pinButton.setSelected(pinned);
		pinButton.getStyleClass().add("pin-button");
		pinButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				stage.setAlwaysOnTop(!stage.alwaysOnTopProperty().getValue());
				pinned = stage.isAlwaysOnTop();
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
		
		hBox.getChildren().addAll(addNoteField, pinButton, colorBox, closeButton);
		root.getChildren().add(hBox);
	}
	void initStage(Scene scene) {
		stage.setAlwaysOnTop(pinned);
		
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.setX(Screen.getPrimary().getBounds().getWidth() - scene.getWidth());
		stage.setY(0);
	}
	
	public boolean isPinned() {
		return pinned;
	}
	public String getColor() {
		return color;
	}
	
	public Note addNote(String name, String color) {
		ObservableList<Node> children = root.getChildren();
		
		Note note = new Note(name, color);
		note.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				children.remove(note);
				root.requestFocus();
			}
		});
		children.add(1, note);
		note.requestFocus();
		
		return note;
	}
	public Note[] getNotes() {
		ObservableList<Node> children = root.getChildren();
		
		return children.subList(1, children.size()).toArray(new Note[children.size() - 1]);
	}
	
	@Override
	public void close() {
		CloseTransition transition = new CloseTransition(root, new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
			
		});
		transition.play();
	}
	
}