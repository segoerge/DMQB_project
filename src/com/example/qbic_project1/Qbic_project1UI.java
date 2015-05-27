package com.example.qbic_project1;

import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;

import FileType.Data;
import FileType.Experiment;
import FileType.Project;
import FileType.SampleList;
import Parser.ImportFileSystem;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
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
		BeanContainer<String, Data> dataSets = importFS.getDataSetBeanContainer(); // DataSets.tsv
		LinkedList<SampleList> samples = importFS.getSamples();	 // QCOFF.tsv & QMOUS.tsv -> converted to BeanContainer when needed
		
		// Show imported projects
		ListSelect ls1 = new ListSelect("My projects", projects);
		ls1.setItemCaptionMode(ListSelect.ItemCaptionMode.PROPERTY);
		ls1.setItemCaptionPropertyId("name");
		ls1.setNullSelectionAllowed(false);
		
		// Grouping of Field from project
		FormLayout project_form = new FormLayout();		
		Table proj_table = new Table("Project");
		// Define two columns for the built-in container
		proj_table.addContainerProperty("Field", String.class, null);
		proj_table.addContainerProperty("Value",  String.class, null);
		project_form.addComponent(proj_table);
		
		// Hide on load
		project_form.setVisible(false);

		// Add listener to ListSelect ls1 -> react to project selection
		ls1.addListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// Make Project form visible
				project_form.setVisible(true);
				// Retrieve the currently selected item
				Item currItem = ls1.getItem(ls1.getValue());
				
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
		
		// Show experiments 
		ListSelect ls2 = new ListSelect("My experiments", experiments);
		ls2.setItemCaptionMode(ListSelect.ItemCaptionMode.PROPERTY);
		ls2.setItemCaptionPropertyId("type");
		ls2.setNullSelectionAllowed(false);
		
		
		// Add listener to ListSelect ls2 -> react to experiment selection
		ls2.addListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// Hide project form
				project_form.setVisible(false);
				// Retrieve the currently selected item
				Item currItem = ls2.getItem(ls2.getValue());
			}
		});
		

		// Define root layout
		final VerticalLayout root = new VerticalLayout();
		final HorizontalLayout lists = new HorizontalLayout();
		setContent(root);
		// Add components to layout layout
		root.setMargin(true);
		lists.setMargin(true);
		lists.addComponent(ls1);
		lists.addComponent(ls2);
		root.addComponent(lists);
		root.addComponent(project_form);
		
	}

}