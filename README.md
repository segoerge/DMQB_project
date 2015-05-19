# DMQB_project
TODO:
- Explore given data
- Parser
- VAADIN interface
- Data export

Setup:
https://vaadin.com/eclipse - Option 1 geht recht fix

Eclipse hat auch Github integriert:
Im Eclipse rechts oben neben der Java Perspektive "Open Perspective". Da kann man unser Git repository einbinden (über Clone -> URL eingeben und github login)


Programm Struktur
Package: Parser - Generiert internen Datentyp

     - .tsv - dateien. (Kann Vaadin)
	- .mzml
	- .fastq


Package: Visuell-Oberfläche - Zeigt Internal Datentypen an 

	- Projekte
		○ Experimente
			§ Samples
				□ Data-sets

	-  Zeig die Rohdaten: (extern oder im Framework?)
	- Anotieren . Notizen

Package: Export - Gibt Internal File Format aus als text file

	- Projekt-Ebene , Experiment-Ebene, Sample-Ebene (sind tsv Files- mit bilder? Zusammenfassung=
	- FastaExport
	- mzmL Export
	- FastQ Export

Package: FileTypen - Internal File Format

	- FastaSequence
	- MzMLSequence
	- FastQSequence
