package org.lindholm.widget.note.controls;

import javafx.beans.value.*;
import javafx.scene.control.*;

public class EnchantedTextField extends TextField {
	
	boolean allowedEmpty = true;
	String previousText;
	
	String emptyDisplayLabel = "";
	
	public EnchantedTextField() {
		
		textProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.isEmpty()) previousText = newValue;
			}
			
		});
		
		focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				focusChanged(newValue.booleanValue());
			}
			
		});
		
	}
	
	public void setAllowedEmpty(boolean allowedEmpty) {
		this.allowedEmpty = allowedEmpty;
	}
	public boolean isAllowedEmpty() {
		return allowedEmpty;
	}
	
	public void setEmptyDisplayLabel(String emptyDisplayLabel) {
		this.emptyDisplayLabel = emptyDisplayLabel;
	}
	public String getEmptyDisplayLabel() {
		return emptyDisplayLabel;
	}
	
	void focusChanged(boolean hasFocus) {
		if (!allowedEmpty) {
			if (super.getText().isEmpty()) {
				super.setText(previousText);
			}
		} else if (hasFocus) {
			
			super.getStyleClass().remove("emptyDisplayLabel");
			
			if (super.getText().equals(emptyDisplayLabel)) {
				super.setText("");
			}
		} else {
			
			super.getStyleClass().add("emptyDisplayLabel");
			
			if (super.getText().isEmpty()) {
				super.setText(emptyDisplayLabel);
			}
		}
	}
	
}