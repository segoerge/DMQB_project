package Parser;

public class FastaSequence {
	private int sequenceNumber;
	private String sequenceName;
	private String sequence;

	public FastaSequence(int sequenceNumber, String sequenceName, String sequence){
		this.setFastaSequenceNumber(sequenceNumber);
		this.setFastaSequenceName(sequenceName);
		this.setFastaSequence(sequence);
	}

	public int getFastaSequenceNumber() {
		return sequenceNumber;
	}

	public void setFastaSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getFastaSequenceName() {
		return sequenceName;
	}

	public void setFastaSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public String getFastaSequence() {
		return sequence;
	}

	public void setFastaSequence(String sequence) {
		this.sequence = sequence;
	}
}
