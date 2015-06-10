package FileType;

import java.util.LinkedList;

import Parser.ImportFileSystem;

// Data format for the content of experiments.tsv
public class Experiment {
	
	//Attribute
	private String projectIdentifier;
	private String experimentIdentifier;
	private String type;
	private SampleList sampleList;
	private SampleList entityList;
	private LinkedList<Data> summaryData;
	private String typeAnnotation;
	
	
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
	this.typeAnnotation = generateTypeAnnotation(type);
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	// Generates a simple Type annotation which can be displayed to the user
	private String generateTypeAnnotation(String experimentID) 
	{
		if (experimentID.contains("EXPERIMENTAL_DESIGN")) return "Experimental Design";
		if (experimentID.contains("SAMPLE_EXT")) return "Sample Extraction";
		if (experimentID.contains("ANALYSIS")) return "Analysis data";
		return "Unknown Experiment Type";
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



	public String getTypeAnnotation() {
		return typeAnnotation;
	}



	public void setTypeAnnotation(String typeAnnotation) {
		this.typeAnnotation = typeAnnotation;
	}
	
	
}
