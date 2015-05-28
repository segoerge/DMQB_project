package FileType;

import java.io.File;

import Parser.ImportFileSystem;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextField;

public class Data {
	private String identifier;
	private String parent;
	private String type;
	private String path;
	// Custom data format for external link to data file
	private Link DataLink;
	private TextField annotation;
	private CheckBox export; // Set true if data + annotation should be included in report
	
	public Data(String identifier, String parent, String type, String path){
		this.setIdentifier(identifier);
		this.setParent(parent);
		this.setType(type);
		this.setPath(path);
		this.DataLink=createDataLink(type,path);
		this.annotation=new TextField();
		annotation.setValue(generateDefaultAnnotation(parent));
		this.export=new CheckBox();
		export.setValue(false);
	}
	
	
	

	// Method to create a default annotation for data
	private String generateDefaultAnnotation(String parentID)
	{
		
		// Does not do too much at the moment
		return "Write something...";
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

			return link;
		
			
		}
		if (type.equals("INFO"))
		{
			FileResource resource = new FileResource(new File(basepath+
					"/data/"+path));
			
			
			Link link = new Link(null,
					resource);
			link.setIcon(new ThemeResource("images/info.png"));
			// Open the URL in a new window
			link.setTargetName("_blank");

			return link;
		
			
		}
		if (type.equals("GRAPHIC"))
		{
			FileResource resource = new FileResource(new File(basepath+
					"/data/"+path));
			
			
			Link link = new Link(null,
					resource);
			link.setIcon(new ThemeResource("images/graphic.png"));
			// Open the URL in a new window
			link.setTargetName("_blank");

			return link;
		
			
		}
		if (type.equals("CSV"))
		{
			FileResource resource = new FileResource(new File(basepath+
					"/data/"+path));
			
			
			Link link = new Link(null,
					resource);
			link.setIcon(new ThemeResource("images/csv.png"));
			// Open the URL in a new window
			link.setTargetName("_blank");

			return link;
		
			
		}
		if (type.equals("NGS_QC"))
		{
			// Local .html -> use ThemeResource
			ThemeResource resource = new ThemeResource("html/"+path+"/fastqc_report.html");
					
			Link link = new Link(null,
					resource);
			link.setIcon(new ThemeResource("images/fastqc_icon.png"));
			// Open the URL in a new window
			link.setTargetName("_blank");
			
			return link;
			
			
			//Link link = new Link(null,
			//		new ExternalResource("http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id="+ID+"&lvl=0"));
			//link.setIcon(new ThemeResource("images/ncbi.gif"));
			// Open the URL in a new window
			//link.setTargetName("_blank");
			//return link;
			
			
			
		}

		return null;
	}
	
	public TextField getAnnotation() {
		return annotation;
	}

	public void setAnnotation(TextField annotation) {
		this.annotation = annotation;
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
	public CheckBox getExport() {
		return export;
	}


	public void setExport(CheckBox export) {
		this.export = export;
	}

}
