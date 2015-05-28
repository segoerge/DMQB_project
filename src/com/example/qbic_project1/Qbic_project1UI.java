package com.example.qbic_project1;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("qbic_project1")
public class Qbic_project1UI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Qbic_project1UI.class)
	public static class Servlet extends VaadinServlet {
	}

	

	@SuppressWarnings("deprecation")
	@Override
	protected void init(VaadinRequest request) {
		// Import data models
		
		ImportFileSystem importFS = new ImportFileSystem("/resources");
		BeanContainer<String, Project> projects = importFS.getProjectBeanContainer(); // Projects.tsv
		BeanContainer<String, Experiment> experiments = importFS.getExperimentBeanContainer(); // Experiments.tsv
		BeanContainer<String, Data> dataSets1 = importFS.getDataSetBeanContainer(); // DataSets.tsv
		BeanContainer<String, Data> dataSets2 = importFS.getDataSetBeanContainer(); // DataSets.tsv
		
		
		Label heading = new Label();
		heading.setStyleName("h1");
		heading.setValue("Project Visualizer");
		
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
		ls2.setItemCaptionPropertyId("type");  // Use property "type" for item labeling
		ls2.setNullSelectionAllowed(false); // No empty selection from list possible
		ls2.setEnabled(false); // Disable ls2 until selection in ls1 was made
				
		// Init table to show samples -> not visible until something in ls2 is selected
		Table tb1 = new Table("My samples");
		tb1.setVisible(false);
		tb1.setSelectable(true); // table selectable 
		tb1.setMultiSelect(true);
		tb1.addStyleName("hover-samples");
		//tb1.setMultiSelectMode(MultiSelectMode.SIMPLE);
		tb1.setImmediate(true);  // selection has immediate effect
		// Init table to show sample datasets -> not visible until something in tb1 is selected
		Table tb2 = new Table("My sample datasets");
		tb2.setVisible(false);
		// Init table to show experiment datasets -> not visible until something in exp is selected
		Table tb3 = new Table("My experiment datasets");
		tb3.setVisible(false);
		
		
		// Add listener to ListSelect ls1 -> react to project selection
	    ls1.setImmediate(true);
		ls1.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				// Make Project form visible
				proj_table.setVisible(true);
				// Enable ls2
				ls2.setEnabled(true);
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
				// Show tb1
				tb1.setVisible(true);
				tb3.setVisible(true);
				proj_table.setVisible(false);
				// Hide tb2
				tb2.setVisible(false);
				// Import BeanContainer
				BeanContainer<String, Sample> samples = importFS.getSampleBeanContainer((String)ls1.getValue()); 
				tb1.setContainerDataSource(samples);
				tb3.setContainerDataSource(dataSets1);
				// Select only some columns to reduce width
				tb1.setVisibleColumns(new Object[]{"identifier", "SAMPLE_TYPE", "EXPERIMENT", "q_SECONDARY_NAME", "NCBILink", "q_NCBI_ORGANISM"});
				// Filter the samples for selected experiment
				samples.removeAllContainerFilters();
				samples.addContainerFilter(new SimpleStringFilter("EXPERIMENT", (String)ls2.getValue(), true, false));
				dataSets1.removeAllContainerFilters();
				dataSets1.addContainerFilter(new SimpleStringFilter("parent", (String)ls2.getValue(), true, false));
				tb1.setPageLength(samples.size());
				tb3.setPageLength(dataSets1.size());
				tb3.setVisibleColumns(new Object[]{"dataLink", "path", "annotation", "export"});
				
				
				
			}
		});
		
		// Add listener to Table tb1 -> react to sample selection
		tb1.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				
				
				// Show tb2 -> dataset table & hide sample table
				tb2.setVisible(true);
				
				// Add BeanContainer dataSets to tb2
				dataSets2.removeAllContainerFilters();
				tb2.setContainerDataSource(dataSets2);
				
				
				// Filter the datasets for selected sample(s) or project ID

				if (tb1.getValue()!=null)
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
					// Set visible columns: Link to data, annotation text field and export checkbox
					tb2.setVisibleColumns(new Object[]{"dataLink", "path", "annotation", "export"});
				}
				else
				{
					// Nothing is selected in tb1 (anymore). Hide tb2.
					tb2.setVisible(false);	
				}
				tb2.setPageLength(dataSets2.size());
				
				
				
			}
		});
		
		// Define root layout
		final VerticalLayout root = new VerticalLayout();
		final HorizontalLayout lists = new HorizontalLayout();
		setContent(root);
		// Add components to layout lists
		root.setMargin(true);
		lists.setMargin(true);
		root.addComponent(heading);
		lists.addComponent(ls1);
		lists.addComponent(ls2);
		lists.addComponent(tb3);
		root.addComponent(lists);
		root.addComponent(proj_table);	
		root.addComponent(tb1);
		root.addComponent(tb2);
	}

}
