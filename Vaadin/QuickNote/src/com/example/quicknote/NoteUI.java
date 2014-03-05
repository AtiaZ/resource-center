package com.example.quicknote;

import java.io.File;
import java.io.IOException;

import com.google.gwt.thirdparty.guava.common.io.Files;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@Theme("runo")
public class NoteUI extends UI {

	public static File NOTES_DIR = Files.createTempDir();
	// To use a persistent directory in exo data dir, use this:
	// public static File NOTES_DIR = new File("data/quicknote");
	
	FilesystemContainer notes = new FilesystemContainer(NOTES_DIR);
	Table noteList = new Table("Notes", notes);
	RichTextArea noteView = new RichTextArea();
	Button add = new Button("+");
	String filename;
	Window subWindow = new Window("Enter note name:");
	TextField txtname = new TextField();
	Button ok = new Button("OK");
	Button cancel = new Button("Cancel");

	@Override
	protected void init(VaadinRequest request) {

		VerticalSplitPanel split = new VerticalSplitPanel(noteList, noteView);
		split.setHeight("300px");

		noteView.setSizeFull();
		noteList.setSizeFull();

		tableRefresh(notes);

		noteList.addValueChangeListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				noteView.setPropertyDataSource(new TextFileProperty(
						(File) event.getProperty().getValue()));
			}
		});

		// Dialog box to enter note name
		VerticalLayout subVert = new VerticalLayout();
		subVert.setMargin(true);
		subVert.setSpacing(true);
		subVert.addComponents(txtname, new HorizontalLayout(ok, cancel));
		subWindow.setContent(subVert);

		// Center it in the browser window
		subWindow.setResizable(false);
		subWindow.center();

		// Buttons click listener
		ok.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				filename = txtname.getValue();
				if (!filename.equals("")) {
					File newNode = new File(NOTES_DIR, filename);
					try {
						newNode.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					tableRefresh(notes);
					noteList.select(newNode);
				}
				subWindow.close();
			}
		});

		cancel.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				subWindow.close();
			}
		});

		add.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				addWindow(subWindow);
			}
		});

		// set main layout
		VerticalLayout vert = new VerticalLayout(split, add);
		vert.setWidth("285px");
		vert.setComponentAlignment(add, Alignment.MIDDLE_CENTER);
		setContent(vert);

	}

	private void tableRefresh(FilesystemContainer data) {
		noteList.setContainerDataSource(data);
		noteList.setSelectable(true);
		noteList.setImmediate(true);
		noteList.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		noteList.setVisibleColumns(FilesystemContainer.PROPERTY_NAME);
	}

}
