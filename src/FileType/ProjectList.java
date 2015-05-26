package FileType;

import java.util.LinkedList;

public class ProjectList extends TSVFile{

	private Experiment experiments;

	public ProjectList(String[][] content, String[] header, String path) {
		super(content, header, path);
	}
	public ProjectList(TSVFile insert) {
		super(insert.content, insert.header, insert.path);
	}
	public Experiment getExperiments() {
		return experiments;
	}
	public void setExperiments(Experiment experiments) {
		this.experiments = experiments;
	}
}

