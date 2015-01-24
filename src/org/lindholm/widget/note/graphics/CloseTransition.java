package org.lindholm.widget.note.graphics;

import javafx.animation.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class CloseTransition {
	
	SequentialTransition transition;
	
	public CloseTransition(Region node, EventHandler<ActionEvent> actionHandler) {
		node.setMinHeight(0);
		node.setPrefHeight(node.getHeight());
		
		Timeline slideTransition = new Timeline();
		slideTransition.getKeyFrames().addAll(new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), 300)), new KeyFrame(Duration.millis(200), new KeyValue(node.opacityProperty(), 0)));
		
		Timeline heightTransition = new Timeline();
		heightTransition.getKeyFrames().add(new KeyFrame(Duration.millis(node.getPrefHeight()), new KeyValue(node.prefHeightProperty(), 0)));
		
		transition = new SequentialTransition();
		transition.getChildren().addAll(slideTransition, heightTransition);
		transition.setOnFinished(actionHandler);
	}
	
	public void play() {
		transition.play();
	}
}