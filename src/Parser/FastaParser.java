package Parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import FileType.FastaSequence;

/*
 * Parser - reads in .fasta files and writes .fasta files.
 */
public class FastaParser {
	//Attributes
	private String filePath;
	private LinkedList<FastaSequence> FastaSequences  = new LinkedList<FastaSequence>();

	//Constucts
	public FastaParser(String path){
		this.filePath=path;
		readFile(path);
	}
	
	/**
		Used parts of the code from exercise!
	 */
	public void readFile(String file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currLine = "";
			int x= 1;
			FastaSequence e= new FastaSequence(0, "", "");
			while((currLine =br.readLine()) != null){
				if(Pattern.matches(">.*", currLine)){
					e = new FastaSequence(x, currLine, "");
					FastaSequences.add(e);
					x++;
				}
				else if((e !=null) && Pattern.matches(".+",currLine) ){
					e.setFastaSequence(e.getFastaSequence()+currLine);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void readOut(){
		FastaSequence e;
		int i =0;
		while(i < FastaSequences.size() && (e = FastaSequences.get(i)) != null ){
			System.out.println("FastaSequence-Number: "+e.getFastaSequenceNumber());
			System.out.println("Header: "+e.getFastaSequenceName());
			System.out.println("FastaSequence: "+e.getFastaSequence());
			System.out.println();
			i++;
		}
	}

	public LinkedList<FastaSequence> getFastaSequences(){
		return FastaSequences;
	}
}