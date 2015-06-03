package FileType;

import java.util.LinkedList;

public class Experiment {
	
	//Attribute
	private String projectIdentifier;
	private String experimentIdentifier;
	private String type;
	private SampleList sampleList;
	private SampleList entityList;
	private LinkedList<Data> summaryData;
	
	//Constructor
	
	public Experiment(String projectIdentifier, String experimentIdentifier, String type, SampleList samples, SampleList entities){
		this.setProjectIdentifier(projectIdentifier);
		this.setExperimentIdentifier(experimentIdentifier);
		this.setType(experimentIdentifier + "_" + type);
		this.setSamples(samples);
		this.setEntities(entities);
		summaryData = new LinkedList<Data>();
	}
	
	// Constructor for smaller Experiment-Object -> used in Vaadin-GUI
	public Experiment(String experimentIdentifier, String projectIdentifier,
			String type) {
		this.experimentIdentifier = experimentIdentifier;
		this.projectIdentifier = projectIdentifier;
		this.type = experimentIdentifier + "_" + type;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}


	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

	public String getExperimentIdentifier() {
		return experimentIdentifier;
	}

	public void setExperimentIdentifier(String experimentIdentifier) {
		this.experimentIdentifier = experimentIdentifier;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SampleList getEntities() {
		return entityList;
	}

	public void setEntities(SampleList entities) {
		this.entityList = entities;
	}

	public SampleList getSamples() {
		return sampleList;
	}

	public void setSamples(SampleList samples) {
		this.sampleList = samples;
	}

	public LinkedList<Data> getSummaryData() {
		return summaryData;
	}

	public void setSummaryData(LinkedList<Data> summaryData) {
		this.summaryData = summaryData;
	}
}
