package org.lindholm.widget.note;

import java.io.File;

import javafx.stage.Stage;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.lindholm.widget.note.controls.*;
import org.w3c.dom.*;

public class NoteWidgetFactory {
	
	public static NoteWidget load(Stage primaryStage, File file) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(file);
		doc.getDocumentElement().normalize();
		
		// root element
		Element widgetElement = doc.getDocumentElement();
		
		boolean pinned = Boolean.parseBoolean(widgetElement.getAttribute("pinned"));
		String widgetColor = widgetElement.getAttribute("color");
		
		NoteWidget widget = new NoteWidget(primaryStage, pinned, widgetColor);
		
		// note element(s)
		NodeList notes = widgetElement.getChildNodes();
		for (int i = notes.getLength() - 1; i >= 0; i--) {
			Node noteNode = notes.item(i);
			
			if (noteNode.getNodeType() == Node.ELEMENT_NODE) {
				Element noteElement = (Element) noteNode;
				
				String noteName = noteElement.getAttribute("name");
				String noteColor = noteElement.getAttribute("color");
				
				Note note = widget.addNote(noteName, noteColor);
				
				// item element(s)
				NodeList items = noteElement.getChildNodes();
				for (int j = 0; j < items.getLength(); j++) {
					Node itemNode = items.item(j);
					
					if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
						Element itemElement = (Element) itemNode;
						
						String itemName = itemElement.getAttribute("name");
						boolean itemChecked = Boolean.parseBoolean(widgetElement.getAttribute("checked"));
						
						note.addItem(itemName, itemChecked);
					}
				}
			}
		}
		
		return widget;
	}
	
	public static void store(NoteWidget widget, File file) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		// root element
		Element widgetElement = doc.createElement("note-widget");
		widgetElement.setAttribute("pinned", String.valueOf(widget.isPinned()));
		widgetElement.setAttribute("color", widget.getColor());
		doc.appendChild(widgetElement);
		
		// note element(s)
		for (Note note : widget.getNotes()) {
			
			Element noteElement = doc.createElement("note");
			noteElement.setAttribute("name", note.getName());
			noteElement.setAttribute("color", note.getColor());
			widgetElement.appendChild(noteElement);
			
			// item element(s)
			for (Item item : note.getItems()) {
				
				Element itemElement = doc.createElement("item");
				itemElement.setAttribute("name", item.getName());
				itemElement.setAttribute("checked", String.valueOf(item.isChecked()));
				noteElement.appendChild(itemElement);
				
			}
			
		}
		
		// write DOMDocuement to xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		// pretty-printing setup
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);
		
		transformer.transform(source, result);
	}
	
}