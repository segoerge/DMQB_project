package FileType;

public class TSVFile {
	protected String path;
	protected String[] header;
	protected String[][] content;

	//Constructors
	public TSVFile(String[][] content, String[] header, String path){
		this.setHeader(header);
		this.setContent(content);
		this.path=path;
	}
	
	public TSVFile(TSVFile insert){
		this.content=insert.getContent();
		this.header=insert.getHeader();
		this.path=insert.getPath();
		}
	
	
	public void initialize(){
		System.out.println("Has to be coded!");
		//to write for each class
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String[] getHeader() {
		return header;
	}

	public void setHeader(String[] header) {
		this.header = header;
	}

	public String[][] getContent() {
		return content;
	}

	public void setContent(String[][] content) {
		this.content = content;
	}
}
