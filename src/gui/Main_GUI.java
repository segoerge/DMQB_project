package gui;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import Export.SummaryExport;
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
public class Main_GUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Main_GUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		
		// Import data models		
		ImportFileSystem importFS = new ImportFileSystem("/resources");
		BeanContainer<String, Project> projects = importFS.getProjectBeanContainer(); // Projects.tsv
		BeanContainer<String, Experiment> experiments = importFS.getExperimentBeanContainer(); // Experiments.tsv
		BeanContainer<String, Data> dataSets = importFS.getDataSetBeanContainer(); // DataSets.tsv
		BeanContainer<String, Sample> QCOFFsamples = importFS.getSampleBeanContainer("QCOFF"); //QCOFF.tsv
		BeanContainer<String, Sample> QMOUSsamples = importFS.getSampleBeanContainer("QMOUS"); //QMOUS.tsv
		
		// Show some nice title labeling
		Label heading = new Label();
		heading.setStyleName("h1");
		heading.setValue("Project Visualizer");
		
		// Initialize menu bar 
		MenuBar menu = new MenuBar();
		menu.setEnabled(false);
		
		// Show imported projects
		ListSelect proj_Select = new ListSelect("My projects", projects);
		proj_Select.setImmediate(true);
		proj_Select.setItemCaptionMode(ListSelect.ItemCaptionMode.PROPERTY); // Use a property for item labeling
		proj_Select.setItemCaptionPropertyId("name"); // Use property "name" for item labeling
		proj_Select.setNullSelectionAllowed(false);  // No empty selection from list possible
		
		// Show a table with project information
		Table proj_table = new Table("Project Information");
		proj_table.addContainerProperty("Field", String.class, null);// Define two columns for the built-in container
		proj_table.addContainerProperty("Value",  String.class, null);
		proj_table.setVisible(false);
		
		// Show experiments 
		ListSelect expr_Select = new ListSelect("Project parts", experiments);
		expr_Select.setItemCaptionMode(ListSelect.ItemCaptionMode.PROPERTY); // Use a property for item labeling
		expr_Select.setItemCaptionPropertyId("typeAnnotation");  // Use property "type" for item labeling
		expr_Select.setNullSelectionAllowed(false); // No empty selection from list possible
		expr_Select.setVisible(false); // Disable expr_Select until selection in proj_Select was made
						
		// sample_Table to show samples -> not visible until something in expr_Select is selected
		Table sample_Table = new Table("My samples");
		sample_Table.setVisible(false);
		sample_Table.setSelectable(true); // table selectable 
		sample_Table.setMultiSelect(true);
		sample_Table.addStyleName("hover-samples");
		sample_Table.setImmediate(true);  // selection has immediate effect
		
		// data_Table to show datasets -> not visible until something in expr_Select is selected
		Table data_Table = new Table("Available data");
		data_Table.setVisible(false);
				
		// Add listener to ListSelect proj_Select -> react to project selection
		proj_Select.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				// Make project information table visible
				proj_table.setVisible(true);
				// Make Exporter visible
				menu.setEnabled(true);
				// Enable expr_Select
				expr_Select.setVisible(true);
				// Ensure that sample_Table and data_Table are invisible until something in expr_Select is selected
				sample_Table.setVisible(false);
				data_Table.setVisible(false);

				// Retrieve the currently selected item from proj_Select
				Item currItem = proj_Select.getItem(proj_Select.getValue());
				proj_table.removeAllItems(); // clear project information table
				
				// Fill Project description table
				proj_table.addItem(new Object[]{"Project name",currItem.getItemProperty("name").getValue()}, 1);
				proj_table.addItem(new Object[]{"Description",currItem.getItemProperty("description").getValue()}, 2);
				proj_table.addItem(new Object[]{"Members",currItem.getItemProperty("members").getValue()}, 3);
				proj_table.setPageLength(proj_table.size()); // Adjust table length 
				
				// Filter the project parts ("experiments") for the selected project
				experiments.removeAllContainerFilters();
				experiments.addContainerFilter(new SimpleStringFilter("projectIdentifier", (String) proj_Select.getValue(), true, false));
			}
		});
		
		
		
		// Add listener to ListSelect expr_Select -> react to experiment selection
		expr_Select.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				
				
				
				// Check if experiment selection contains something with "Analysis"
				Boolean analysisExperiment = expr_Select.getItemCaption(expr_Select.getValue()).toString().contains("Analysis");
				
				// Show sample_Table 
				sample_Table.setVisible(true);
				data_Table.setVisible(true);
				proj_table.setVisible(false);
				
				// If Experiment is Type "Analysis", sample_Table is not needed -> setVisible to false
				if (analysisExperiment) sample_Table.setVisible(false);
				
				// Choose project data 
				String project = (String) proj_Select.getValue();
				BeanContainer<String, Sample> samples = null;

				if (project.equals("QMOUS"))
				{
					samples = QMOUSsamples;
				}
				if (project.equals("QCOFF"))
				{
					samples = QCOFFsamples;
				}
				
				
				sample_Table.setContainerDataSource(samples);
				data_Table.setContainerDataSource(dataSets);
				
				//clean up column headers
				sample_Table.setColumnHeader("identifier", "Identifier");
				sample_Table.setColumnHeader("q_SECONDARY_NAME", "Origin/Tissue");
				sample_Table.setColumnHeader("NCBILink", "NCBI Link");
				sample_Table.setColumnHeader("q_PRIMARY_TISSUE", "Tissue");
				sample_Table.setColumnHeader("annotation", "Annotation");
				
				// Define fixed column widths
				sample_Table.setColumnWidth("identifier", 180);
				sample_Table.setColumnWidth("q_SECONDARY_NAME", 150);
				sample_Table.setColumnWidth("NCBILink", 80);
				sample_Table.setColumnWidth("q_PRIMARY_TISSUE", 180);
				sample_Table.setColumnWidth("annotation", 170);
				
				
				// Select only relevant columns 
				sample_Table.setVisibleColumns(new Object[]{"identifier","q_SECONDARY_NAME", "q_PRIMARY_TISSUE","NCBILink", "annotation"});
				
				// Filter the samples for selected experiment
				samples.removeAllContainerFilters();
				samples.addContainerFilter(new SimpleStringFilter("EXPERIMENT", (String)expr_Select.getValue(), true, false));
				dataSets.removeAllContainerFilters();
				dataSets.addContainerFilter(new SimpleStringFilter("parent", (String)expr_Select.getValue(), true, false));
				sample_Table.setPageLength(samples.size());
				data_Table.setPageLength(dataSets.size());
				sample_Table.setWidthUndefined();
				
				//Add intuitiv column headers to data table
				data_Table.setColumnHeader("dataLink", "Download");
				data_Table.setColumnHeader("path", "File Name");
				data_Table.setColumnHeader("annotation", "Annotation");
				data_Table.setVisibleColumns(new Object[]{"dataLink", "path", "annotation"});
				
				// Change caption of sample table
				if (expr_Select.getItemCaption(expr_Select.getValue()).toString().contains("Design"))
					sample_Table.setCaption("Species used in project");
				if (expr_Select.getItemCaption(expr_Select.getValue()).toString().contains("Sample"))
					sample_Table.setCaption("Samples collected in project");
				
			}
		});
		
		// Add listener to Table sample_Table -> react to sample selection and show associated datasets
		// Only needed for "sample extraction"  -> show associated data
		sample_Table.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				// Check if experiment selection is "Sample Extraction"
				Boolean sampleExperiment = expr_Select.getItemCaption(expr_Select.getValue()).toString().contains("Sample");

				// Filter the datasets for selected sample(s) 
				if (sample_Table.getValue()!=null && sampleExperiment)
				{
					dataSets.removeAllContainerFilters();
					// Get list of selected itemIDs
					Set<Item> sampleMultiSelect  = (Set<Item>) sample_Table.getValue();
					// Put itemIDs in an ArrayList<String>
					ArrayList<String> multiSelectCollect = new ArrayList<String>();
					for (Object i:sampleMultiSelect){
					    multiSelectCollect.add(sample_Table.getItem(i).getItemProperty("identifier").getValue().toString());
					}
					// Use custom filter MultiSelectFilter to filter dataSets
					dataSets.addContainerFilter(new MultiSelectFilter(multiSelectCollect, "parent"));
					
					data_Table.setColumnHeader("dataLink", "Download"); 
					data_Table.setColumnHeader("path", "File Name"); 
					data_Table.setColumnHeader("annotation", "Annotation"); 
					
					// Set visible columns: Link to data, annotation text field and export checkbox
					data_Table.setVisibleColumns(new Object[]{"dataLink", "path", "annotation"}); 
					data_Table.setPageLength(dataSets.size()); 
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
		
		lists.addComponent(proj_Select);
		lists.addComponent(expr_Select);
		lists.addComponent(data_Table);
		root.addComponent(lists);
		root.addComponent(proj_table);	
		root.addComponent(sample_Table);
		
		
		// Define a menu command for pdf export.
		MenuBar.Command proj_summary = new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	// call report generation here
		    	Table sum_Table = new Table();
		    	sum_Table.setVisible(true);

				String project = (String) proj_Select.getValue();
				BeanContainer<String, Sample> samples = null;

				if (project.equals("QMOUS"))
				{
					samples = QMOUSsamples;
				}
				if (project.equals("QCOFF"))
				{
					samples = QCOFFsamples;
				}
		    	
				SummaryExport sumExporter = new SummaryExport(proj_Select, expr_Select, samples, dataSets, experiments);
		    	IndexedContainer sumContainer = sumExporter.generateExportTable();
		    	 // Clear the GUI
		        sample_Table.setVisible(false);
		        data_Table.setVisible(false);
		        proj_table.setVisible(true);
		        expr_Select.setData(expr_Select.getData());
		        
		        //
		    	sum_Table.setContainerDataSource(sumContainer);
		    	sum_Table.setPageLength(sumContainer.size());
		    	sample_Table.setPageLength(sample_Table.size());
		    	
		    	// Open table in a new window
		    	Window subWindow = new Window("Sub-window");
		        VerticalLayout subContent = new VerticalLayout();
		        subContent.setMargin(true);
		        subWindow.setContent(subContent);
		        subContent.addComponent(sum_Table);
		        
		        //export addon
		        
		        HorizontalLayout buttons = new HorizontalLayout();
		        ExcelExporter excelExporter = new ExcelExporter(sum_Table);
		        excelExporter.setCaption("Export to Excel");
		        buttons.addComponent(excelExporter);
		        
		        PdfExporter pdfExporter = new PdfExporter(sum_Table);
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
	

}
