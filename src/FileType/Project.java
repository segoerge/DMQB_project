package FileType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Pattern;

public class Project {

	//Attribute
	private String identifier;
	private String name;
	private String description;
	private String members;
	private ExperimentsList experimentsList;
	private LinkedList<Experiment> experiments;
	




	public Project (String identifier, String name, String description, String members){
		this.setIdentifier(identifier);
		this.setName(name);
		this.setDescription(description);
		this.setMembers(members);
		this.experiments= new LinkedList<Experiment>();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public ExperimentsList getExperimentsList() {
		return experimentsList;
	}

	public void setExperiments(ExperimentsList experimentsList) {
		this.experimentsList = experimentsList;
		experimentsList.setProjectIdentifier(identifier);
		DataSets tempDataSet = experimentsList.getSamples().getDataset();
		for(int i=0; i<experimentsList.getContent().length; i++){
			SampleList tempSamples = new SampleList(splitContent(experimentsList.getSamples(), experimentsList.getContent()[i][0]), experimentsList.getSamples().getHeader(), experimentsList.getSamples().getPath());
			SampleList tempEntities =  new SampleList(splitContent(experimentsList.getEntities(), experimentsList.getContent()[i][0]), experimentsList.getEntities().getHeader(), experimentsList.getEntities().getPath());;
			Experiment a = new Experiment(identifier, experimentsList.getContent()[i][0], experimentsList.getContent()[i][2], tempSamples, tempEntities);
			experiments.add(a);
		}

		for(int i=0; i<tempDataSet.getContent().length; i++){
			Iterator<Experiment> iterating = experiments.iterator();
			Experiment t;
			while(iterating.hasNext()){
				t = iterating.next();
				if(t.getExperimentIdentifier() != null && Pattern.matches(t.getExperimentIdentifier(), tempDataSet.getContent()[i][1])){
					Data tempData = new Data(tempDataSet.getContent()[i][0], tempDataSet.getContent()[i][1],  tempDataSet.getContent()[i][2],  tempDataSet.getContent()[i][3]);
					t.getSummaryData().add(tempData);
				}

			}
		}
	}
	//Methods
	public String[][] splitContent(TSVFile input, String search){
		String[][] result= new String[fileCounter(input.getContent(), search)][input.getHeader().length];
		int resultRow=0;
		for(int i=0; i<input.getContent().length; i++){
			boolean containsSearch=false;
			for(int j=0; j<input.getContent()[i].length; j++){
				if(Pattern.matches(search+".*", input.getContent()[i][j])){
					containsSearch=true;
				}
			}
			if(containsSearch==true){
				for(int j=0; j<result[resultRow].length; j++){
					result[resultRow][j]=input.getContent()[i][j];
				}
				resultRow++;
			}
		}
		return result;
	}


	public int fileCounter(String[][] content, String search){
		int res=0;
		for(int i=0; i<content.length; i++){
			for(int j=0; j<content[i].length; j++){
				if( Pattern.matches(search+".*", content[i][j])){
					res++;
				}
			}
		}

		return res;
	}
}
