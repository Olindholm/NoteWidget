import java.io.File;

import org.lindholm.widget.note.NoteWidget;
import org.lindholm.widget.note.NoteWidgetFactory;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.*;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		File file = new File("cache.xml");
		
		NoteWidget widget = null;
		if (file.exists()) {
			try {
				widget = NoteWidgetFactory.load(primaryStage, new File("cache.xml"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (widget == null) {
			widget = new NoteWidget(primaryStage, false, NoteWidget.COLORS[0]);
		}
		
		final NoteWidget thisWidget = widget;
		widget.setOnClose(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				try {
					
					NoteWidgetFactory.store(thisWidget, new File("cache.xml"));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		widget.requestFocus();
		widget.show();
	}
}