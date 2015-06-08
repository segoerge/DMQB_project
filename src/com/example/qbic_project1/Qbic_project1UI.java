package com.example.qbic_project1;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import FileType.Data;
import FileType.Experiment;
import FileType.Project;
import FileType.QMOUS;
import FileType.Sample;
import FileType.SampleList;
import Filter.MultiSelectFilter;
import Parser.ImportFileSystem;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import org.vaadin.haijian.*;

@SuppressWarnings("serial")
@Theme("qbic_project1")
public class Qbic_project1UI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Qbic_project1UI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		
		// Import data models		
		ImportFileSystem importFS = new ImportFileSystem("/resources");
		BeanContainer<String, Project> projects = importFS.getProjectBeanContainer(); // Projects.tsv
		BeanContainer<String, Experiment> experiments = importFS.getExperimentBeanContainer(); // Experiments.tsv
		BeanContainer<String, Data> dataSets1 = importFS.getDataSetBeanContainer(); // DataSets.tsv
		BeanContainer<String, Data> dataSets2 = importFS.getDataSetBeanContainer(); // DataSets.tsv
		BeanContainer<String, Sample> QCOFFsamples = importFS.getSampleBeanContainer("QCOFF");
		BeanContainer<String, Sample> QMOUSsamples = importFS.getSampleBeanContainer("QMOUS");
		
		Label heading = new Label();
		heading.setStyleName("h1");
		heading.setValue("Project Visualizer");
		
		MenuBar menu = new MenuBar();
		menu.setEnabled(false);
		
		// Show imported projects
		ListSelect ls1 = new ListSelect("My projects", projects);

		ls1.setItemCaptionMode(ListSelect.ItemCaptionMode.PROPERTY);
		ls1.setItemCaptionPropertyId("name");
		ls1.setNullSelectionAllowed(false);
		
		// Grouping of Field from project	
		Table proj_table = new Table("Project");
		// Define two columns for the built-in container
		proj_table.addContainerProperty("Field", String.class, null);
		proj_table.addContainerProperty("Value",  String.class, null);
		
		// Hide on load
		proj_table.setVisible(false);

		ls1.setItemCaptionMode(ListSelect.ItemCaptionMode.PROPERTY); // Use a property for item labeling
		ls1.setItemCaptionPropertyId("name"); // Use property "name" for item labeling
		ls1.setNullSelectionAllowed(false);  // No empty selection from list possible

		// Show experiments 
		ListSelect ls2 = new ListSelect("My experiments", experiments);
		ls2.setItemCaptionMode(ListSelect.ItemCaptionMode.PROPERTY); // Use a property for item labeling
		ls2.setItemCaptionPropertyId("typeAnnotation");  // Use property "type" for item labeling
		ls2.setNullSelectionAllowed(false); // No empty selection from list possible
		ls2.setEnabled(false); // Disable ls2 until selection in ls1 was made
		ls2.setVisible(false);
				
		// Init table to show samples -> not visible until something in ls2 is selected
		Table tb1 = new Table("My samples");
		tb1.setVisible(false);
		tb1.setSelectable(true); // table selectable 
		tb1.setMultiSelect(true);
		tb1.addStyleName("hover-samples");
		//tb1.setMultiSelectMode(MultiSelectMode.SIMPLE);
		tb1.setImmediate(true);  // selection has immediate effect
		// Init table to show summary -> not visible until menu item is selected
		Table tb2 = new Table("Project Summary");
		tb2.setVisible(false);
		// Init table to show experiment datasets -> not visible until something in exp is selected
		Table tb3 = new Table("Available data");
		tb3.setVisible(false);
		
		
		// Add listener to ListSelect ls1 -> react to project selection
	    ls1.setImmediate(true);
		ls1.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				// Make Project form visible
				proj_table.setVisible(true);
				// Make Exporter visible
				menu.setEnabled(true);
				// Enable ls2
				ls2.setEnabled(true);
				ls2.setVisible(true);
				// Hide tb1 & tb2 -> a new project was chosen, hide tb1 & tb2 until selection in ls2 was made
				tb1.setVisible(false);
				tb2.setVisible(false);
				tb3.setVisible(false);

				// Retrieve the currently selected item
				Item currItem = ls1.getItem(ls1.getValue());
				proj_table.removeAllItems();

				proj_table.addItem(new Object[]{"Project name",currItem.getItemProperty("name").getValue()}, 1);
				proj_table.addItem(new Object[]{"Description",currItem.getItemProperty("description").getValue()}, 2);
				proj_table.addItem(new Object[]{"Members",currItem.getItemProperty("members").getValue()}, 3);

				// Show exactly the currently contained rows (items)
				proj_table.setPageLength(proj_table.size());
				
				// Filter the experiments for the selected project
				experiments.removeAllContainerFilters();
				experiments.addContainerFilter(new SimpleStringFilter("projectIdentifier", (String) ls1.getValue(), true, false));
			}
		});
		
		
		
		// Add listener to ListSelect ls2 -> react to experiment selection
		ls2.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				// Check if experiment selection contains something with "Analysis"
				Boolean analysisExperiment = ls2.getItemCaption(ls2.getValue()).toString().contains("Analysis");
				// Show tb1 
				tb1.setVisible(true);
				tb3.setVisible(true);
				proj_table.setVisible(false);
				// Hide tb2
				tb2.setVisible(false);
				// Import BeanContainer
				//BeanContainer<String, Sample> samples = importFS.getSampleBeanContainer((String)ls1.getValue()); 
				String project = (String) ls1.getValue();
				BeanContainer<String, Sample> samples = null;

				if (project.equals("QMOUS"))
				{
					samples = QMOUSsamples;
				}
				if (project.equals("QCOFF"))
				{
					samples = QCOFFsamples;
				}
				
				tb1.setContainerDataSource(samples);
				tb3.setContainerDataSource(dataSets1);
				
				//clean up column headers
				tb1.setColumnHeader("identifier", "Identifier");
				tb1.setColumnHeader("SAMPLE_TYPE", "Type");
				tb1.setColumnHeader("EXPERIMENT", "Experiment");
				tb1.setColumnHeader("q_SECONDARY_NAME", "Origin/Tissue");
				tb1.setColumnHeader("NCBILink", "NCBI Link");
				tb1.setColumnHeader("q_NCBI_ORGANISM", "NCBI Organism");
				tb1.setColumnHeader("annotation", "Annotation");
				
				// Select only some columns to reduce width
				tb1.setVisibleColumns(new Object[]{"identifier", "SAMPLE_TYPE", "EXPERIMENT", "q_SECONDARY_NAME", "NCBILink", "q_NCBI_ORGANISM", "annotation"});
				// Filter the samples for selected experiment
				samples.removeAllContainerFilters();
				samples.addContainerFilter(new SimpleStringFilter("EXPERIMENT", (String)ls2.getValue(), true, false));
				dataSets1.removeAllContainerFilters();
				dataSets1.addContainerFilter(new SimpleStringFilter("parent", (String)ls2.getValue(), true, false));
				tb1.setPageLength(samples.size());
				tb3.setPageLength(dataSets1.size());
				
				//clean up table headers
				tb3.setColumnHeader("dataLink", "Download");
				tb3.setColumnHeader("path", "File Name");
				tb3.setColumnHeader("annotation", "Annotation");
				//tb3.setColumnHeader("export", "Export"); //Kann wahrscheinlich noch weg
				
				tb3.setVisibleColumns(new Object[]{"dataLink", "path", "annotation"});
				// If Experiment is Type "Analysis", table1 is not needed -> setVisible to false
				if (analysisExperiment) tb1.setVisible(false);
				
			}
		});
		
		// Add listener to Table tb1 -> react to sample selection
		tb1.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				// Check if experiment selection is "Sample Extraction"
				Boolean sampleExperiment = ls2.getItemCaption(ls2.getValue()).toString().contains("Sample");
				
				
				// Show tb2 -> dataset table & hide sample table
				tb3.setVisible(true); //changed from tb2 to tb3
				
				// Add BeanContainer dataSets to tb3 (changed from tb2 to tb3)
				dataSets2.removeAllContainerFilters();
				if (tb1.getValue()!=null && sampleExperiment)
				tb3.setContainerDataSource(dataSets2); //changed from tb2 to tb3
				
			
				
				
				// Filter the datasets for selected sample(s) or project ID

				if (tb1.getValue()!=null && sampleExperiment)
				{
					// Get list of selected itemIDs
					Set<Item> sampleMultiSelect  = (Set<Item>) tb1.getValue();
					// Put itemIDs in an ArrayList<String>
					ArrayList<String> multiSelectCollect = new ArrayList<String>();
					for (Object i:sampleMultiSelect){
					    multiSelectCollect.add(tb1.getItem(i).getItemProperty("identifier").getValue().toString());
					}
					// Use custom filter MultiSelectFilter to filter dataSets
					dataSets2.addContainerFilter(new MultiSelectFilter(multiSelectCollect, "parent"));
					
					tb3.setColumnHeader("dataLink", "Download"); //changed from tb2 to tb3
					tb3.setColumnHeader("path", "File Name"); //changed from tb2 to tb3
					tb3.setColumnHeader("annotation", "Annotation"); //changed from tb2 to tb3
					//tb2.setColumnHeader("export", "Export"); //Kann wahrscheinlich noch weg
					
					// Set visible columns: Link to data, annotation text field and export checkbox
					tb3.setVisibleColumns(new Object[]{"dataLink", "path", "annotation"}); //changed from tb2 to tb3
					tb3.setPageLength(dataSets2.size()); //changed from tb2 to tb3
				}
				else
				{
					// Nothing is selected in tb1 (anymore). Hide tb2.
					tb2.setVisible(false);	
				}
				
				
				
				
			}
		});
		
		// Define root layout
		final VerticalLayout root = new VerticalLayout();
		final HorizontalLayout lists = new HorizontalLayout();
		setContent(root);
		// Add components to layout lists
		root.setMargin(true);
		lists.setMargin(true);
		
		// A top-level menu item that opens a submenu
		menu.setWidth("100%");
		menu.setStyleName("mymenubar");
		MenuItem export = menu.addItem("Export", null, null);		
		root.addComponent(menu);
		root.addComponent(heading);
		
		lists.addComponent(ls1);
		lists.addComponent(ls2);
		lists.addComponent(tb3);
		root.addComponent(lists);
		root.addComponent(proj_table);	
		root.addComponent(tb1);
		root.addComponent(tb2);
		
		// Define a menu command for pdf export.
		MenuBar.Command proj_summary = new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	// call report generation here
		    	tb2.setVisible(true);

				String project = (String) ls1.getValue();
				BeanContainer<String, Sample> samples = null;

				if (project.equals("QMOUS"))
				{
					samples = QMOUSsamples;
				}
				if (project.equals("QCOFF"))
				{
					samples = QCOFFsamples;
				}
		    	
		    	IndexedContainer sumContainer = generateProjSummary(ls1, samples);
		    	tb2.setContainerDataSource(sumContainer);
		    	tb2.setPageLength(sumContainer.size());
		    	tb1.setPageLength(tb1.size());
		    	
		    	// Open table in a new window
		    	Window subWindow = new Window("Sub-window");
		        VerticalLayout subContent = new VerticalLayout();
		        subContent.setMargin(true);
		        subWindow.setContent(subContent);
		        subContent.addComponent(tb2);
		        
		        //export addon
		        
		        HorizontalLayout buttons = new HorizontalLayout();
		        ExcelExporter excelExporter = new ExcelExporter(tb2);
		        excelExporter.setCaption("Export to Excel");
		        buttons.addComponent(excelExporter);
		        
		        PdfExporter pdfExporter = new PdfExporter(tb2);
		        pdfExporter.setCaption("Export to PDF");
		        pdfExporter.setWithBorder(false);
		        buttons.addComponent(pdfExporter);
		        
		        subContent.addComponent(buttons);
		        
		        // Center it in the browser window
		        subWindow.center();
		        
		        // Open it in the UI
		        addWindow(subWindow);
		 
		    }  
		};
		MenuItem report = export.addItem("Project Summary", null, proj_summary);
		
	}
	private IndexedContainer generateProjSummary(ListSelect ls1, BeanContainer<String, Sample> samples)
	{
		IndexedContainer sumContainer = new IndexedContainer();
    	sumContainer.addContainerProperty("Entry", String.class, "");
    	sumContainer.addContainerProperty("Value", String.class, "");
    	// 1. Project Info
    	Item ls1Item = ls1.getItem(ls1.getValue()); // Get data from ls1
    	Object newEntry = sumContainer.addItem();
    	sumContainer.getContainerProperty(newEntry, "Entry").setValue("-- Project Information --");
    	sumContainer.getContainerProperty(newEntry, "Value").setValue("");
    	 newEntry = sumContainer.addItem();
    	sumContainer.getContainerProperty(newEntry, "Entry").setValue("Project Name:");
    	sumContainer.getContainerProperty(newEntry, "Value").setValue(ls1Item.getItemProperty("name").getValue());
    	 newEntry = sumContainer.addItem();
    	sumContainer.getContainerProperty(newEntry, "Entry").setValue("Description:");
    	sumContainer.getContainerProperty(newEntry, "Value").setValue(ls1Item.getItemProperty("description").getValue());
    	 newEntry = sumContainer.addItem();
    	sumContainer.getContainerProperty(newEntry, "Entry").setValue("Members:");
    	sumContainer.getContainerProperty(newEntry, "Value").setValue(ls1Item.getItemProperty("members").getValue());
    	sumContainer.getContainerProperty(newEntry, "Entry").setValue("");
    	sumContainer.getContainerProperty(newEntry, "Value").setValue("");
    	// 2. Design Info
    	samples.removeAllContainerFilters();
		samples.addContainerFilter(new SimpleStringFilter("SAMPLE_TYPE", "Q_BIOLOGICAL_ENTITY", true, false));
		newEntry = sumContainer.addItem();
		sumContainer.getContainerProperty(newEntry, "Entry").setValue("-- Experiment Design Information --");
    	sumContainer.getContainerProperty(newEntry, "Value").setValue("");
    	 newEntry = sumContainer.addItem();
     	sumContainer.getContainerProperty(newEntry, "Entry").setValue("Number of Species:");
     	sumContainer.getContainerProperty(newEntry, "Value").setValue(samples.size()+"");
     	// Iterate over samples to get unique sample conditions
     	Set<String> items = new HashSet<String>();
     	for (Iterator i = samples.getItemIds().iterator(); i.hasNext();)
     	{
     	// Get the current item identifier, which is an integer.
     	    String iid =  (String) i.next();
     	    
     	    // Now get the actual item from the table.
     	    Item item = samples.getItem(iid);
     	    
     	    // Do something
     	   items.add((String) item.getItemProperty("q_SECONDARY_NAME").getValue());
     	}
     	newEntry = sumContainer.addItem();
     	sumContainer.getContainerProperty(newEntry, "Entry").setValue("Conditions:");
     	sumContainer.getContainerProperty(newEntry, "Value").setValue(items.toString());
     // Iterate over samples and add all entries containing annotations to summary
     	
     	for (Iterator i = samples.getItemIds().iterator(); i.hasNext();)
     	{
     	// Get the current item identifier, which is an integer.
     	    String iid =  (String) i.next();
     	    
     	    // Now get the actual item from the table.
     	    Item item = samples.getItem(iid);
     	    
     	    // Do something
     	    TextField annotation = (TextField) item.getItemProperty("annotation").getValue();
     	    if (!annotation.getValue().isEmpty())
     	    {
     	    	newEntry = sumContainer.addItem();
     	     	sumContainer.getContainerProperty(newEntry, "Entry").setValue(item.getItemProperty("identifier").getValue());
     	     	sumContainer.getContainerProperty(newEntry, "Value").setValue(annotation.getValue());
     	    }
     	}
    	return sumContainer;
	}

}
