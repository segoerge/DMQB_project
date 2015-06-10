package FileType;

import java.util.LinkedList;

// Helper class for the collection of data from experiments.tsv
public class ExperimentsList extends TSVFile{

	//special Attributes
	private String projectIdentifier;
	private SampleList sampleList;
	private SampleList entityList;
	//Constructors
	public ExperimentsList(String[][] content, String[] header, String path) {
		super(content, header, path);
		
	}
	
	public ExperimentsList(TSVFile insert) {
		super(insert.content, insert.header, insert.path);
	}

//methodes
	public void initialize(){
		
	}

	public SampleList getSamples() {
		return sampleList;
	}

	public void setSamples(SampleList samples) {
		this.sampleList = samples;
	}

	public SampleList getEntities() {
		return entityList;
	}

	public void setEntities(SampleList entities) {
		this.entityList = entities;
		entities.setProjectIdentifier(projectIdentifier);
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		projectIdentifier = projectIdentifier;
	}

}
