package Export;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import FileType.Data;
import FileType.Experiment;
import FileType.Sample;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;

public class SummaryExport {
	private ListSelect proj_Select;
	private ListSelect expr_Select;
	private BeanContainer<String, Sample> samples;
	private BeanContainer<String, Data> dataSet;
	private BeanContainer<String, Experiment> experiments;
	private IndexedContainer sumContainer;

	public SummaryExport(ListSelect proj_Select, ListSelect expr_Select,
			BeanContainer<String, Sample> samples,
			BeanContainer<String, Data> dataSet,
			BeanContainer<String, Experiment> experiments) {
		this.proj_Select = proj_Select;
		this.expr_Select = expr_Select;
		this.samples = samples;
		this.dataSet = dataSet;
		this.experiments = experiments;
		this.sumContainer = new IndexedContainer();
		sumContainer.addContainerProperty("Item", String.class, "");
		sumContainer.addContainerProperty("Description", String.class, "");
	}

	private void addEntry(Object where, Object what) {
		Object newEntry = sumContainer.addItem();
		sumContainer.getContainerProperty(newEntry, "Item").setValue(where);
		sumContainer.getContainerProperty(newEntry, "Description").setValue(what);
	}

	private void addEmptyLine() {
		Object newEntry = sumContainer.addItem();
		sumContainer.getContainerProperty(newEntry, "Item").setValue("");
		sumContainer.getContainerProperty(newEntry, "Description").setValue("");
	}

	public IndexedContainer generateExportTable() {

		String currProj = (String) proj_Select.getValue();
		// 1. Project Info
		Item proj_SelectItem = proj_Select.getItem(proj_Select.getValue()); // Get
																			// data
																			// from
																			// proj_Select 

		addEntry("-- Project Information --", "");
		addEntry("Project Name:", proj_SelectItem.getItemProperty("name")
				.getValue());
		addEntry("Description:", proj_SelectItem.getItemProperty("description")
				.getValue());
		addEntry("Members:", proj_SelectItem.getItemProperty("members")
				.getValue());
		addEmptyLine();
		// 2. Design Info
		samples.removeAllContainerFilters();
		samples.addContainerFilter(new SimpleStringFilter("SAMPLE_TYPE",
				"Q_BIOLOGICAL_ENTITY", true, false));
		addEntry("-- Experiment Design Information --", "");
		addEntry("Number of Species:", samples.size() + "");
		
		// Iterate over samples to get unique sample conditions
		Set<String> items = new HashSet<String>();
		for (Iterator i = samples.getItemIds().iterator(); i.hasNext();) {
			// Get the current item identifier, which is an integer.
			String iid = (String) i.next();

			// Now get the actual item from the table.
			Item item = samples.getItem(iid);

			// Add current qSecondaryName to Set of Strings
			items.add((String) item.getItemProperty("q_SECONDARY_NAME")
					.getValue());
		}
		addEntry("Conditions:", items.toString());

		// Iterate over samples and add all entries containing annotations to
		// summary
		// Add descriptive line befor the first row containing the annotation
		boolean firstAnnotated = true;
		for (Iterator i = samples.getItemIds().iterator(); i.hasNext();) {
			// Get the current item identifier, which is an integer.
			String iid = (String) i.next();

			// Now get the actual item from the table.
			Item item = samples.getItem(iid);

			// Add sample to summary if item is annotated
			TextField annotation = (TextField) item.getItemProperty(
					"annotation").getValue();
			if (!annotation.getValue().isEmpty()) {
				if (firstAnnotated)
				{
					addEntry("Annotated species","");
					firstAnnotated=false;
				}
				addEntry(item.getItemProperty("identifier").getValue(),
						annotation.getValue());
			}
		}

		// Iterate over dataSet and add all entries containing annotations to
		// summary
		dataSet.removeAllContainerFilters();
		for (Iterator i = dataSet.getItemIds().iterator(); i.hasNext();) {
			// Get the current item identifier, which is an integer.
			String iid = (String) i.next();

			// Now get the actual item from the table.
			Item item = dataSet.getItem(iid);

			// Add dataSet to summary if item is annotated and belongs to
			// selected experiment
			TextField annotation = (TextField) item.getItemProperty(
					"annotation").getValue();
			String parentID = (String) item.getItemProperty("parent")
					.getValue();
			firstAnnotated=true;
			if (!annotation.getValue().isEmpty()
					&& parentID.startsWith(currProj)
					&& (parentID.length() < 10) && !isAnalysisData(parentID)) {
				if (firstAnnotated)
				{
					addEntry("Annotated data","");
					firstAnnotated=false;
				}
				addEntry(item.getItemProperty("path").getValue(),
						annotation.getValue());
			}
		}
		addEmptyLine();

		// 3. Sample info
		samples.removeAllContainerFilters();
		samples.addContainerFilter(new SimpleStringFilter("SAMPLE_TYPE",
				"Q_BIOLOGICAL_SAMPLE", true, false));
		addEntry("-- Sample Extraction Information --", "");
		addEntry("Number of Samples:", samples.size() + "");

		// Iterate over samples to get unique sample conditions
		items = new HashSet<String>();
		for (Iterator i = samples.getItemIds().iterator(); i.hasNext();) {
			// Get the current item identifier, which is an integer.
			String iid = (String) i.next();

			// Now get the actual item from the table.
			Item item = samples.getItem(iid);

			// Add current qSecondaryName to Set of Strings
			items.add((String) item.getItemProperty("q_SECONDARY_NAME")
					.getValue());
		}
		addEntry("Conditions:", items.toString());
		// Iterate over samples and add all entries containing annotations to
		// summary
		firstAnnotated=true;
		for (Iterator i = samples.getItemIds().iterator(); i.hasNext();) {
			// Get the current item identifier, which is an integer.
			String iid = (String) i.next();

			// Now get the actual item from the table.
			Item item = samples.getItem(iid);

			// Add sample to summary if item is annotated
			TextField annotation = (TextField) item.getItemProperty(
					"annotation").getValue();
			if (!annotation.getValue().isEmpty()) {
				if (firstAnnotated)
				{
					addEntry("Annotated sample","");
					firstAnnotated=false;
				}
				addEntry(item.getItemProperty("identifier").getValue(),
						annotation.getValue());
			}
		}
		// Iterate over dataSet and all all entries containing annotations to
		// summary
		dataSet.removeAllContainerFilters();
		firstAnnotated=true;
		for (Iterator i = dataSet.getItemIds().iterator(); i.hasNext();) {
			// Get the current item identifier, which is an integer.
			String iid = (String) i.next();

			// Now get the actual item from the table.
			Item item = dataSet.getItem(iid);

			// Add dataSet to summary if item is annotated and belongs to
			// selected experiment
			TextField annotation = (TextField) item.getItemProperty(
					"annotation").getValue();

			String parentID = (String) item.getItemProperty("parent")
					.getValue();
			
			if (!annotation.getValue().isEmpty()
					&& parentID.startsWith(currProj)
					&& (parentID.length() > 8)
					&& !isAnalysisData(parentID)) {
				if (firstAnnotated)
				{
					addEntry("Annotated data from sample(s)","");
					firstAnnotated=false;
				}
				addEntry(item.getItemProperty("path").getValue(),
						annotation.getValue());
			}
		}
		addEmptyLine();

		// 4. Add information about analysis data
		addEntry("-- Analytic Data Information --", "");
		dataSet.removeAllContainerFilters();
		int dataCount = 0;
		ArrayList<String[]> analyticData = new ArrayList<String[]>();
		for (Iterator i = dataSet.getItemIds().iterator(); i.hasNext();) {
			// Get the current item identifier, which is an integer.
			String iid = (String) i.next();

			// Now get the actual item from the table.
			Item item = dataSet.getItem(iid);
			String parentID = (String) item.getItemProperty("parent")
					.getValue();
			if (!isAnalysisData(parentID))
				continue;
			dataCount++;

			// Add dataSet to summary if item is annotated and belongs to
			// selected experiment
			TextField annotation = (TextField) item.getItemProperty(
					"annotation").getValue();

			String currExperiment = (String) expr_Select.getValue();

			if (!annotation.getValue().isEmpty()) {
				String path = (String) item.getItemProperty("path").getValue();
				String annot = annotation.getValue();
				String[] newEntry = { path, annot };
				analyticData.add(newEntry);
			}
		}
		addEntry("Number of analytic data:", dataCount + "");
		firstAnnotated=true;
		for (int i = 0; i < analyticData.size(); i++) {
			if (firstAnnotated)
			{
				addEntry("Annotated data","");
				firstAnnotated=false;
			}
			String[] currEntry = analyticData.get(i);
			addEntry(currEntry[0], currEntry[1]);
		}

		return sumContainer;

	}

	private boolean isAnalysisData(String ID) {
		for (Iterator i = experiments.getItemIds().iterator(); i.hasNext();) {
			// Get the current item identifier, which is an integer.
			String iid = (String) i.next();
			// Now get the actual item from the table.
			Item item = experiments.getItem(iid);
			item.getItemPropertyIds().toArray().toString();
			String typeExpr = (String) item.getItemProperty("type").getValue();
			// Check if current experiment type is "analysis"
			if (!typeExpr.contains("ANALYSIS"))
				continue;
			String parentID = (String) item.getItemProperty(
					"experimentIdentifier").getValue();
			if (parentID.equals(ID))
				return true;
		}
		return false;
	}

}
