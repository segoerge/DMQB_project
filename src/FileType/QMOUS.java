package FileType;

// QMOUS class type needed to create a BeanContainer from QMOUS.tsv
public class QMOUS implements Sample
{
	String Identifier,
	SAMPLE_TYPE,
	Q_SECONDARY_NAME,
	EXPERIMENT,
	PARENT,
	Q_PRIMARY_TISSUE,
	Q_TISSUE_DETAILED,
	Q_ADDITIONAL_INFO,
	Q_NCBI_ORGANISM,
	Q_EXTERNALDB_ID,
	Condition_genotype,	
	Condition_tissue;

	public QMOUS(String identifier, String sAMPLE, String tYPEQ_SECONDARY_NAME,
			String eXPERIMENT, String pARENT, String q_PRIMARY_TISSUE,
			String q_TISSUE_DETAILED, String q_ADDITIONAL_INFO,
			String q_NCBI_ORGANISM, String q_EXTERNALDB_ID,
			String condition_genotype, String condition_tissue) {
		Identifier = identifier;
		SAMPLE_TYPE = sAMPLE;
		Q_SECONDARY_NAME = tYPEQ_SECONDARY_NAME;
		EXPERIMENT = eXPERIMENT;
		PARENT = pARENT;
		Q_PRIMARY_TISSUE = q_PRIMARY_TISSUE;
		Q_TISSUE_DETAILED = q_TISSUE_DETAILED;
		Q_ADDITIONAL_INFO = q_ADDITIONAL_INFO;
		Q_NCBI_ORGANISM = q_NCBI_ORGANISM;
		Q_EXTERNALDB_ID = q_EXTERNALDB_ID;
		Condition_genotype = condition_genotype;
		Condition_tissue = condition_tissue;
	}

	public String getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}

	public String getSAMPLE_TYPE() {
		return SAMPLE_TYPE;
	}

	public void setSAMPLE_TYPE(String sAMPLE) {
		SAMPLE_TYPE = sAMPLE;
	}

	public String getQ_SECONDARY_NAME() {
		return Q_SECONDARY_NAME;
	}

	public void setQ_SECONDARY_NAME(String q_SECONDARY_NAME) {
		Q_SECONDARY_NAME = q_SECONDARY_NAME;
	}

	public String getEXPERIMENT() {
		return EXPERIMENT;
	}

	public void setEXPERIMENT(String eXPERIMENT) {
		EXPERIMENT = eXPERIMENT;
	}

	public String getPARENT() {
		return PARENT;
	}

	public void setPARENT(String pARENT) {
		PARENT = pARENT;
	}

	public String getQ_PRIMARY_TISSUE() {
		return Q_PRIMARY_TISSUE;
	}

	public void setQ_PRIMARY_TISSUE(String q_PRIMARY_TISSUE) {
		Q_PRIMARY_TISSUE = q_PRIMARY_TISSUE;
	}

	public String getQ_TISSUE_DETAILED() {
		return Q_TISSUE_DETAILED;
	}

	public void setQ_TISSUE_DETAILED(String q_TISSUE_DETAILED) {
		Q_TISSUE_DETAILED = q_TISSUE_DETAILED;
	}

	public String getQ_ADDITIONAL_INFO() {
		return Q_ADDITIONAL_INFO;
	}

	public void setQ_ADDITIONAL_INFO(String q_ADDITIONAL_INFO) {
		Q_ADDITIONAL_INFO = q_ADDITIONAL_INFO;
	}

	public String getQ_NCBI_ORGANISM() {
		return Q_NCBI_ORGANISM;
	}

	public void setQ_NCBI_ORGANISM(String q_NCBI_ORGANISM) {
		Q_NCBI_ORGANISM = q_NCBI_ORGANISM;
	}

	public String getQ_EXTERNALDB_ID() {
		return Q_EXTERNALDB_ID;
	}

	public void setQ_EXTERNALDB_ID(String q_EXTERNALDB_ID) {
		Q_EXTERNALDB_ID = q_EXTERNALDB_ID;
	}

	public String getCondition_genotype() {
		return Condition_genotype;
	}

	public void setCondition_genotype(String condition_genotype) {
		Condition_genotype = condition_genotype;
	}

	public String getCondition_tissue() {
		return Condition_tissue;
	}

	public void setCondition_tissue(String condition_tissue) {
		Condition_tissue = condition_tissue;
	}
	


}
