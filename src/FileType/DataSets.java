package FileType;

// Data format to store content of datasets.tsv
public class DataSets extends TSVFile{
	//Attributes
	String projectIdentifier;
	String experimentIdentifier;
	String Identifier;

	//Constructor
	public DataSets(String[][] content, String[] header, String path) {
		super(content, header, path);
	}
	public DataSets(TSVFile insert) {
		super(insert.content, insert.header, insert.path);
	}
}
