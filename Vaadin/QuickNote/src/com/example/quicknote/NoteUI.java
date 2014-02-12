package com.example.quicknote;

import java.io.File;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
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

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = NoteUI.class)
	public static class Servlet extends VaadinServlet {
	}
	
	FilesystemContainer notes = new FilesystemContainer(new File("data/quicknote"));
	Table noteList = new Table("Notes", notes);
  RichTextArea noteView = new RichTextArea();
  Button add = new Button("+"); 
  String filename;
  Window subWindow = new Window("Enter note name:");
  TextField txtname = new TextField();
  Button submit = new Button("OK");
  Button cancel = new Button("Cancel");
	
	@Override
	protected void init(VaadinRequest request) {
	  
	  VerticalSplitPanel split = new VerticalSplitPanel();
	  
	  VerticalLayout vert = new VerticalLayout();
	  
	  vert.addComponent(split);	  
	  vert.addComponent(add);
	  vert.setComponentAlignment(add, Alignment.MIDDLE_CENTER);
	  
	  split.addComponent(noteList);
	  split.addComponent(noteView);
	  split.setWidth("285px");
	  split.setHeight("300px");
	  
	  noteView.setSizeFull();
	  noteList.setSizeFull();
	  
	  tableRefresh(notes);
	  
	  noteList.addValueChangeListener(new ValueChangeListener() {      
      public void valueChange(ValueChangeEvent event) {
        noteView.setPropertyDataSource(new TextFileProperty((File) event.getProperty().getValue()));        
      }
    });
	  
	  //Dialog box to enter note name
	  HorizontalLayout subHor = new HorizontalLayout();
    VerticalLayout subVert = new VerticalLayout();
    subVert.setMargin(true);
    
    subWindow.setContent(subVert);
    // Put some components in it
    subVert.addComponent(txtname);
    subVert.addComponent(subHor);
    
    subHor.addComponent(submit);
    subHor.addComponent(cancel);
        
    // Center it in the browser window
    subWindow.setResizable(false);
    subWindow.center();
    
    //Buttons click listener
    submit.addClickListener(new ClickListener() {
      public void buttonClick(ClickEvent event) {
        filename = txtname.getValue();
        if (!filename.equals("")) {
          File newNode = new File("data/quicknote/"+filename+".txt");
          
          try {
            newNode.createNewFile();          
                      
          } catch (IOException e) {
            e.printStackTrace();
          }         
          tableRefresh(notes);
          for (Object itemId : noteList.getItemIds()){
            if(itemId.toString().equals(newNode.getPath())){
              noteList.select(itemId);
              break;
            }
          }
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
	  
	  //set main layout
	  setContent(vert);
	  
	}
	
	private void tableRefresh(FilesystemContainer data){
	  noteList.setContainerDataSource(data);
	  noteList.setSelectable(true);
    noteList.setImmediate(true);    
    noteList.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
    noteList.setVisibleColumns(FilesystemContainer.PROPERTY_NAME);
	}

}