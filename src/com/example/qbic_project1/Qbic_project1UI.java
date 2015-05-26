package com.example.qbic_project1;

import java.util.Arrays;

import javax.servlet.annotation.WebServlet;

import FileType.Project;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("qbic_project1")
public class Qbic_project1UI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Qbic_project1UI.class)
	public static class Servlet extends VaadinServlet {
	}

	// Method to import the content of "projects.tsv" as VAADIN data model
	private BeanContainer<String, Project> importProjects() {

		// Container for projects
		BeanContainer<String, Project> projects = new BeanContainer<String, Project>(
				Project.class);

		// Define which property (ID,name, Descr, Members) to use as internal ID
		// -> identifier
		projects.setBeanIdProperty("identifier");

		// Use dummy data until TSV parser is available
		Project p1 = new Project("PID01", "Project 1",
				"Description to Project 1", "A1, A2");
		Project p2 = new Project("PID02", "Project 2",
				"Description to Project 2", "B1, B2");

		// Add dummy data
		projects.addBean(p1);
		projects.addBean(p2);

		// Return container
		return projects;

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void init(VaadinRequest request) {
		// Import data models
		BeanContainer<String, Project> projects = importProjects();

		// Show imported projects
		ListSelect ls1 = new ListSelect("My projects", projects);
		ls1.setItemCaptionMode(ListSelect.ItemCaptionMode.PROPERTY);
		ls1.setItemCaptionPropertyId("name");
		ls1.setNullSelectionAllowed(false);
		Label l1 = new Label("ID: ");
		Label l2 = new Label("Project name: ");
		Label l3 = new Label("Description: ");
		Label l4 = new Label("Members: ");

		// Add listener to ListSelect ls1
		ls1.addListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// Retrieve the currently selected item
				Item currItem = ls1.getItem(ls1.getValue());
				// Retrieve the properties of the item
				l1.setValue("ID: "
						+ currItem.getItemProperty("identifier").getValue());
				l2.setValue("Project name: "
						+ currItem.getItemProperty("name").getValue());
				l3.setValue("Description: "
						+ currItem.getItemProperty("description").getValue());
				l4.setValue("Members: "
						+ currItem.getItemProperty("members").getValue());

			}
		});

		// Define root layout
		final VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		// Add components to layout layout
		layout.setMargin(true);
		layout.addComponent(ls1);
		layout.addComponent(l1);
		layout.addComponent(l2);
		layout.addComponent(l3);
		layout.addComponent(l4);
	}

}