package Export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import FileType.FastaSequence;
import FileType.FastaSequence;

public class FastaExport {

private LinkedList<FastaSequence> FastaSequences  = new LinkedList<FastaSequence>();

	
	public FastaExport(LinkedList<FastaSequence> FastaSequences){
		this.FastaSequences = FastaSequences;
	}
	
	public void createFastaFile(String file){
		try {
			FastaSequence e;
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
			for(int i=0; i<FastaSequences.size() && (e=FastaSequences.get(i))!=null; i++){
				out.println(e.getFastaSequenceName());
				out.println(e.getFastaSequence());
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
