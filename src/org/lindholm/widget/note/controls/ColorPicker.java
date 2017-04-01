/* 
 * Copyright 2017 (C) Wiggy boy <Lindholm>
 * (formally known as Osvald Lindholm)
 */
package org.lindholm.widget.note.controls;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.util.Callback;

public class ColorPicker extends ComboBox<String> {
	
	public ColorPicker(String[] colors, String selectedColor) {
		super.getItems().addAll(colors);
		super.getSelectionModel().select(selectedColor);
		
		super.setPrefWidth(50);
		
		super.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				param.setPrefSize(25 * colors.length + 10, 30 + 10);
				param.setOrientation(Orientation.HORIZONTAL);
				
				return new ListCell<String>() {
					
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						
						super.setPrefSize(25, 25);
						
						super.getStyleClass().remove(item);
						super.getStyleClass().add(item);
					}
					
				};
			}
			
		});
		
	}
	
}