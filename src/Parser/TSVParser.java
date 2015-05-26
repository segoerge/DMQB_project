package Parser;


import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

import FileType.TSVFile;

public class TSVParser {

	//Attributes
	TSVFile tSVFile;
	private String filePath;
	private String[] header;
	private String[][] content;

	//Constucts
	public TSVParser(String path){
		this.filePath=path;
		readFile(path);
	}

	//Methodes
	public void readFile(String file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currLine = "";
			if((currLine =br.readLine()) != null){
				header = currLine.split("\t");
			}
			LinkedList<String[]> tempList = new LinkedList<String[]>();
			while((currLine =br.readLine()) != null){
				String[] temp = currLine.split("\t");
				tempList.add(temp);
			}
			content = new String[tempList.toArray().length][header.length];
			for(int i=0; i< tempList.toArray().length; i++){
				for(int j=0; j<tempList.get(i).length; j++){
						content[i][j]= tempList.get(i)[j];
				}
				if(tempList.get(i).length<header.length){
					for(int j=tempList.get(i).length; j<header.length; j++){
						content[i][j]= " ";
					}
				}
			}
			tSVFile = new TSVFile(content, header, filePath);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void readOut(){
		for(int i=0; i < header.length; i++){
			System.out.print(header[i]+" ");
		}
		System.out.println();
		for(int i=0; i < content.length; i++){
			for(int j=0; j< content[i].length; j++){
				System.out.print(content[i][j]+" ");
			}
			System.out.println();
		}
	}
	public TSVFile getTSVFile(){
		return tSVFile;
	}
}
