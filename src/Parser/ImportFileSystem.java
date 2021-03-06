package Parser;




import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import com.vaadin.data.util.BeanContainer;

import FileType.*;


public class ImportFileSystem {
	//Attributes
	private LinkedList<Project> projects;
	private ProjectList projects_raw;
	private ExperimentsList experiments_raw;
	private LinkedList<SampleList> samples_raw;
	private DataSets dataSets_raw;



	//Contstructor
	public ImportFileSystem(String path){
		projects= new LinkedList<Project>();
		samples_raw = new LinkedList<SampleList>();
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
	
	//Getter und Setter
	// Content of Projects
	// - Projects.tsv as String[][] w/o header row
	// - Experiments.tsv as String[][] w/o header row
	// - Datasets.tsv as String[][] w/o header row
	// - samples QCOFF & QMOUSE (and any future samples) in samples_raw-List (linked-list)
	public LinkedList<Project> getProjects(){
		return projects;
	}
	// Projects.tsv
	public BeanContainer<String, Project> getProjectBeanContainer()
	{
		// Fetch raw TSV-data in String[][] format
		String[][] projectRawData = projects_raw.getContent();
		// Bean Container for projects
		BeanContainer<String, Project> projects = new BeanContainer<String, Project>(
				Project.class);
		// Define which property (ID,name, Descr, Members) to use as internal ID
		// -> identifier
		projects.setBeanIdProperty("identifier");
		// Add data objects (as project objects) to container
		for (int i=0; i< projectRawData.length;i++)
		{
			Project tmp = new Project(projectRawData[i][0], projectRawData[i][1],
					projectRawData[i][2], projectRawData[i][3]);
			projects.addBean(tmp);
		}
		
		return projects;
		
	}
	// Experiments.tsv
	public BeanContainer<String, Experiment> getExperimentBeanContainer()
	{
		// Fetch raw TSV-data in String[][] format
		String[][] ExperimentRawData = experiments_raw.getContent();
		// Bean Container for Experiments
				BeanContainer<String, Experiment> experiments = new BeanContainer<String, Experiment>(
						Experiment.class);
		// Define which property (ID,name, Descr, Members) to use as internal ID
		// -> experimentIdentifier (e.g. QMOUSE1)
		experiments.setBeanIdProperty("experimentIdentifier");
		// Add data objects (as experiment objects) to container
				for (int i=0; i< ExperimentRawData.length;i++)
				{
					Experiment tmp = new Experiment(ExperimentRawData[i][0], ExperimentRawData[i][1],
							ExperimentRawData[i][2]);
					experiments.addBean(tmp);
				}
		return experiments;
	}
	// DataSets.tsv
	public BeanContainer<String, Data> getDataSetBeanContainer()
	{
		// Fetch raw TSV-data in String[][] format
		String[][] dataSetsRawData = dataSets_raw.getContent();
		// Bean Container for DataSets
				BeanContainer<String, Data> dataSets = new BeanContainer<String, Data>(
						Data.class);
		// Define which property (ID,name, Descr, Members) to use as internal ID
		// -> identifier (e.g. DS1)
				dataSets.setBeanIdProperty("identifier");
		// Add data objects (as Data objects) to container
				for (int i=0; i< dataSetsRawData.length;i++)
				{
					Data tmp = new Data(dataSetsRawData[i][0], dataSetsRawData[i][1],
							dataSetsRawData[i][2], dataSetsRawData[i][3]);
					dataSets.addBean(tmp);
				}
		return dataSets;
	}
	
	// Method to retrieve String[][] raw sample data from String projectID
	private String[][] getSampleRawData(String projectID)
	{
		for (int i=0; i< samples_raw.size();i++)
		{
			if (samples_raw.get(i).getContent()[0][0].startsWith(projectID))
			{
				return samples_raw.get(i).getContent();
			}
		}
		return null;
	}
	
	// QMOUS.tsv
	public BeanContainer<String,QMOUS> getQMOUSBeanContainer()
	{
		// Fetch raw sample data in String[][] format
		String[][] SampleRawData = getSampleRawData("QMOUS");
		// Bean Container for DataSets
		BeanContainer<String, QMOUS> QMOUSData = new BeanContainer<String, QMOUS>(
				QMOUS.class);
		// Define which property (ID,name, Descr, Members) to use as internal ID
				// -> identifier (e.g. QMOUSENTITY-1)
		QMOUSData.setBeanIdProperty("identifier");
		// Add data objects (as QMOUS objects) to container
				for (int i=0; i< SampleRawData.length;i++)
				{
					QMOUS tmp = new QMOUS(SampleRawData[i][0], SampleRawData[i][1],
								SampleRawData[i][2], SampleRawData[i][3], SampleRawData[i][4],
										SampleRawData[i][5],SampleRawData[i][6],SampleRawData[i][7],
										SampleRawData[i][8],SampleRawData[i][9],SampleRawData[i][10],
												SampleRawData[i][11]);
					QMOUSData.addBean(tmp);
					}
		return QMOUSData;
	}
	
	// QCOFF.tsv
	public BeanContainer<String,QCOFF> getQCOFFBeanContainer()
	{
		// Fetch raw sample data in String[][] format
		String[][] SampleRawData = getSampleRawData("QCOFF");
		// Bean Container for DataSets
		BeanContainer<String, QCOFF> QCOFFData = new BeanContainer<String, QCOFF>(
				QCOFF.class);
		// Define which property (ID,name, Descr, Members) to use as internal ID
				// -> identifier (e.g. QCOFFENTITY-1)
		QCOFFData.setBeanIdProperty("identifier");
		// Add data objects (as QMOUS objects) to container
				for (int i=0; i< SampleRawData.length;i++)
				{
					QCOFF tmp = new QCOFF(SampleRawData[i][0], SampleRawData[i][1],
								SampleRawData[i][2], SampleRawData[i][3], SampleRawData[i][4],
										SampleRawData[i][5],SampleRawData[i][6],SampleRawData[i][7],
										SampleRawData[i][8],SampleRawData[i][9],SampleRawData[i][10],
												SampleRawData[i][11]);
					QCOFFData.addBean(tmp);
					}
		return QCOFFData;
	}
	
	
	// (Almost) generic Method to import sample TSV-files like QMOUS or QCOFF
	public BeanContainer getSampleBeanContainer(String projectID)
	{
		// Fetch raw sample data in String[][] format
			String[][] SampleRawData = getSampleRawData(projectID);
			// Bean Container for DataSets
			BeanContainer SampleBC = null; 
			// Manually decide from projectID -> not very generic atm.
			// ProjectID is QMOUS
			if (projectID.equals("QMOUS"))
			{
				
				SampleBC = new BeanContainer<String, QMOUS>(QMOUS.class);
				// Define which property (ID,name, Descr, Members) to use as internal ID
				// -> identifier (e.g. QCOFFENTITY-1)
				SampleBC.setBeanIdProperty("identifier");
		// Add data objects (as QMOUS objects) to container
				for (int i=0; i< SampleRawData.length;i++)
				{
					QMOUS tmp = new QMOUS(SampleRawData[i][0], SampleRawData[i][1],
								SampleRawData[i][2], SampleRawData[i][3], SampleRawData[i][4],
										SampleRawData[i][5],SampleRawData[i][6],SampleRawData[i][7],
										SampleRawData[i][8],SampleRawData[i][9],SampleRawData[i][10],
												SampleRawData[i][11]);
					SampleBC.addBean(tmp);
					}
			}
			// ProjectID is QCOFF
			if (projectID.equals("QCOFF"))
			{
				SampleBC = new BeanContainer<String, QCOFF>(QCOFF.class);
				// Define which property (ID,name, Descr, Members) to use as internal ID
				// -> identifier (e.g. QCOFFENTITY-1)
				SampleBC.setBeanIdProperty("identifier");
		// Add data objects (as QMOUS objects) to container
				for (int i=0; i< SampleRawData.length;i++)
				{
					QCOFF tmp = new QCOFF(SampleRawData[i][0], SampleRawData[i][1],
								SampleRawData[i][2], SampleRawData[i][3], SampleRawData[i][4],
										SampleRawData[i][5],SampleRawData[i][6],SampleRawData[i][7],
										SampleRawData[i][8],SampleRawData[i][9],SampleRawData[i][10],
												SampleRawData[i][11]);
					SampleBC.addBean(tmp);
					}	
			}
			return SampleBC;
			
			
	}
	
	
	
	// Main method to test Class Methods
	public static void main (String[] args)
	{
	
	}	
	
}
