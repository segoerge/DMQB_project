package FileType;

public class Data {
	private String identifier;
	private String parent;
	private String type;
	private String path;
	
	public Data(String identifier, String parent, String type, String path){
		this.setIdentifier(identifier);
		this.setParent(parent);
		this.setType(type);
		this.setPath(path);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
