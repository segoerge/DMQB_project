package FileType;

import java.io.File;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Link;

public class Data {
	private String identifier;
	private String parent;
	private String type;
	private String path;
	// Custom data format for external link to data file
	private Link DataLink;
	
	public Data(String identifier, String parent, String type, String path){
		this.setIdentifier(identifier);
		this.setParent(parent);
		this.setType(type);
		this.setPath(path);
		this.DataLink=createDataLink(type,path);
	}
	
	// Method to build a link from the data
	private Link createDataLink(String type, String path)
	{
		// Get absolute path from webserver
		
		String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()+"/VAADIN/";
		
		
		// Decide by data type how to define the link
		if (type.equals("NGS_RAW"))
		{
			
			FileResource resource = new FileResource(new File(basepath+
					"/data/"+path));
			
			
			Link link = new Link(null,
					resource);
			link.setIcon(new ThemeResource("images/fastq.jpeg"));
			// Open the URL in a new window
			link.setTargetName("_blank");
			link.setTargetBorder(BorderStyle.NONE);
			link.setTargetHeight(10);
			link.setTargetWidth(10);

			return link;
		}
		if (type.equals("MS_RAW"))
		{
			FileResource resource = new FileResource(new File(basepath+
					"/data/"+path));
			
			
			Link link = new Link(null,
					resource);
			link.setIcon(new ThemeResource("images/MS.png"));
			// Open the URL in a new window
			link.setTargetName("_blank");
			link.setTargetBorder(BorderStyle.NONE);
			link.setTargetHeight(10);
			link.setTargetWidth(10);

			return link;
		
			
		}
		if (type.equals("NGS_QC"))
		{
			
			FileResource resource = new FileResource(new File(basepath+
					"/data/"+path+"/fastqc_report.html"));
			
			
			Link link = new Link(null,
					resource);
			link.setIcon(new ThemeResource("images/fastqc_icon.png"));
			// Open the URL in a new window
			link.setTargetName("_blank");
			
			return link;
			
			
		}

		return null;
	}
	
	public Link getDataLink() {
		return DataLink;
	}
	public void setDataLink(Link dataLink) {
		DataLink = dataLink;
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
