package Parser;




import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Pattern;

import FileType.*;


public class ImportFileSystem {
	//Attributes
	LinkedList<Project> projects;
	ProjectList projects_raw;
	ExperimentsList experiments_raw;
	LinkedList<SampleList> samples_raw;
	DataSets dataSets_raw;



	//Contstructor
	public ImportFileSystem(String path){
		projects= new LinkedList();
		samples_raw = new LinkedList();
		TSVParser projectsParse = new TSVParser(path+"/projects.tsv");
		TSVParser experimentsParse = new TSVParser(path+"/experiments.tsv");
		TSVParser dataSetsParse = new TSVParser(path+"/datasets.tsv");

		experiments_raw =  new ExperimentsList(experimentsParse.getTSVFile());
		projects_raw = new ProjectList(projectsParse.getTSVFile());
		dataSets_raw = new DataSets(dataSetsParse.getTSVFile());

		for(int i=0; i< projects_raw.getContent().length; i++){
			TSVParser sampleParser = new TSVParser(path+"/"+projects_raw.getContent()[i][0]+".tsv");
			samples_raw.add(new SampleList(sampleParser.getTSVFile()));
		}
		
		//auftrennen in Listen
		String[][] content = projects_raw.getContent();
		for(int i=0; i<projects_raw.getContent().length; i++){
			Project a = new Project(content[i][0],content[i][1], content[i][2],content[i][3]);
			ExperimentsList b = new ExperimentsList(splitContent(experiments_raw, a.getIdentifier()),experiments_raw.getHeader(),experiments_raw.getPath());
			ListIterator<SampleList> c = samples_raw.listIterator();
			DataSets dataSet = new DataSets(splitContent(dataSets_raw, a.getIdentifier()), dataSets_raw.getHeader(), dataSets_raw.getPath()); 
			SampleList temp;
			SampleList samples;
			SampleList entities;
			boolean found = false;
			while(found == false && c!=null){
				temp= c.next();
				if(fileCounter(temp.getContent(), a.getIdentifier())!=0){
					found=true;
					samples = new SampleList(splitContent(temp, "Q_BIOLOGICAL_SAMPLE" ), temp.getHeader(), temp.getPath());
					entities = new SampleList(splitContent(temp, "Q_BIOLOGICAL_ENTITY" ), temp.getHeader(), temp.getPath());
					samples.setDataset(dataSet);
					b.setSamples(samples);
					b.setEntities(entities);
				}
			}
			a.setExperiments(b);
			projects.add(a);
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
