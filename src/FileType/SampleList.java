package FileType;

public class SampleList extends TSVFile{

	private DataSets dataset;
	private String projectIdentifier;
	private String experimentIdentifier;
	
	//Constructor
	public SampleList(String[][] content, String[] header, String path) {
		super(content, header, path);
	}

	public SampleList(TSVFile insert) {
		super(insert.content, insert.header, insert.path);
	}

	public DataSets getDataset() {
		return dataset;
	}

	public void setDataset(DataSets dataset) {
		this.dataset = dataset;
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
}
