package FileType;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Project implements Serializable  {

	// Field names as in "projects.tsv"
	String identifier, name, description, members;

	// Constructor to create one new project
	public Project(String identifier, String name, String description,
			String members) {
		this.identifier = identifier;
		this.name = name;
		this.description = description;
		this.members = members;
	}
	
	
	// Getters/Setters required by VAADINs data model
	public String getIdentifier() {
		return identifier;
	}


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	
	

	
	
}
