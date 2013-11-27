/**
 * 
 */
package pl.comgen.ARDEN;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author medhat
 * 
 */

public class ArdenMultiChromosome {
	

	public boolean isSam(String samFile) {
		if (samFile == null || samFile.isEmpty()) {
			System.err.println("Enter file please");
			return false;
		}
		boolean properFile = false;
		try {
			if (samFile.contains("/")) {
				String[] pathDevided = samFile.split("/");
				String lastWord = pathDevided[pathDevided.length - 1];
				String[] samWords = lastWord.split("\\.");
				String samLastWord = samWords[samWords.length - 1];
				if (samLastWord.equalsIgnoreCase("sam"))
					properFile = true;
			} else {
				String[] samWords = samFile.split("\\.");
				String samLastWord = samWords[samWords.length - 1];
				if (samLastWord.equalsIgnoreCase("sam"))
					properFile = true;
			}

		} catch (Throwable e) {
			System.err.println("Not sam file");
			e.printStackTrace();
		}
		return properFile;
	}

	public BufferedReader readSam(String samFileLocation) {
		if (samFileLocation == null || samFileLocation.isEmpty()
				|| !isSam(samFileLocation))
			return null;

		BufferedReader samBuffer = null;
		try {
			samBuffer = new BufferedReader(new FileReader(samFileLocation));
		} catch (Throwable e) {
			System.out.println("Enter file ");
			e.printStackTrace();
		}
		return samBuffer;

	}

	public HashSet<String> chromosomesSegregation(BufferedReader samFileOrignal,
			BufferedReader samFileArtificial, String samLocation) {
		HashSet<String> chromosomesInSam = new HashSet<String>();
		try {
			if (samFileOrignal != null && samFileArtificial != null) {
				chromosomesInSam =	creatGenomsamDirectories(samFileOrignal, samLocation, true);
				creatGenomsamDirectories(samFileArtificial, samLocation, false);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return chromosomesInSam;
	}

	public void createSamFiles(String file, String line, String header) {
		try {
			File samFile = new File(file);
			if (samFile.exists()) {
				// Append
				FileWriter fw = new FileWriter(samFile, true);
				BufferedWriter bufferWritter = new BufferedWriter(fw);
				bufferWritter.write(line + "\n");
				bufferWritter.close();
			} else {
				// Create sam file
				samFile.createNewFile();
				FileWriter fw = new FileWriter(samFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(header + line + "\n");
				bw.close();

				// Create FileInfo.conf for the same file
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public void createARDENFile(HashSet<String>  orignalChromosomes,String locationOfMappedCheromosomes, 
			String locationOfReferanceChr, String locationOfArReferanceChr,
			String tool ,String fastq) {
		try {
			if(!locationOfMappedCheromosomes.endsWith("/"))
				locationOfMappedCheromosomes += "/";
			if(!locationOfReferanceChr.endsWith("/"))
				locationOfReferanceChr += "/";
			if(!locationOfArReferanceChr.endsWith("/"))
				locationOfArReferanceChr += "/";

			Iterator<String> itr = orignalChromosomes.iterator();
			while (itr.hasNext()) {
				String chrmosomeNumber = itr.next();
				File confFile =  new File(locationOfMappedCheromosomes+chrmosomeNumber+".ini");
				if(!confFile.exists())
					confFile.createNewFile();
				//Create Configuration file for Arden.
				FileWriter  fw = new FileWriter(confFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("$:"+locationOfReferanceChr+chrmosomeNumber+".fa\n");
				bw.write("#:"+locationOfArReferanceChr+chrmosomeNumber+".fasta\n");
				bw.write("&:"+fastq+"\n");
				bw.write("@"+tool+"\n");
				bw.write("ref:"+locationOfMappedCheromosomes+chrmosomeNumber+".sam\n");
				bw.write("art:"+locationOfMappedCheromosomes+chrmosomeNumber+"_AR.sam\n");
				bw.write("+");
				bw.close();
				
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		

	}

	public HashSet<String> creatGenomsamDirectories(BufferedReader samFile,
			String samLocation, boolean isOriginal) {
		HashSet<String> setOfChromosomesNames = new HashSet<String>();
		try {

			StringBuilder header = new StringBuilder();
			String currentLine;
			long ftime = System.currentTimeMillis();
			while ((currentLine = samFile.readLine()) != null) {

				if (currentLine.startsWith("@")) {
					header.append(currentLine + "\n");
				} else {
					String[] arrayOfColumns = currentLine.split("\t");
					String chromosomeName = arrayOfColumns[2];

					// create directory for each new chromosome.
					if (!chromosomeName.equalsIgnoreCase("*")
							&& !setOfChromosomesNames.contains(chromosomeName
									.toString())) {
						setOfChromosomesNames.add(chromosomeName);
						if (samLocation.endsWith("/")) {
							samLocation = samLocation.substring(0,samLocation.length()-1);
							File file = new File(
									samLocation);//.concat(chromosomeName));
							if (!file.exists())
								file.mkdir();
						} else {
							File file = new File(samLocation);//.concat("/".concat(chromosomeName)));
							if (!file.exists())
								file.mkdir();
						}
					}

					// create sam file for each chromosome in it current
					// directory
					if (samLocation.endsWith("/")) {
						String fileName;
						if (isOriginal)
							fileName = samLocation +// chromosomeName + "/" + 
						chromosomeName + ".sam";
						else
							fileName = samLocation +// chromosomeName + "/" + 
						chromosomeName + "_AR.sam";

						createSamFiles(fileName, currentLine, header.toString());
					} else {

						String fileName;
						if (isOriginal)
							fileName = samLocation +// chromosomeName +
							 "/" + 
						chromosomeName + ".sam";
						else
							fileName = samLocation +// chromosomeName + 
							"/" + 
						chromosomeName + "_AR.sam";

						createSamFiles(fileName, currentLine, header.toString());
					
					}

				}
			}
			long lTime = System.currentTimeMillis();
			System.out.println(lTime - ftime);
		} catch (Throwable e) {
			e.printStackTrace();
		}
return setOfChromosomesNames;
	}

	public static void main(String[] args) {
		String arSam = "/home/medhat/Downloads/sortedArNonDuplicatRightName.sam";
		String originalSam = "/home/medhat/Downloads/ReUnMapped_nonDuplicated_sam_shRe.sam";
		String location = "/home/medhat/Downloads/test/";
		ArdenMultiChromosome t = new ArdenMultiChromosome();
	/*	HashSet<String> chr = t.chromosomesSegregation(t.readSam(originalSam),
				t.readSam(arSam), location);*/
		HashSet<String> chr = new HashSet<String>();
		for (int i = 1; i < 11; i++) {
			chr.add(String.valueOf(i));
			
		}
		chr.add("chloroplast");
		chr.add("MT");
		t.createARDENFile(chr, location, "/data/qdata/public/illumina-genomes/Zea_mays/Ensembl/AGPv2/Sequence/Chromosomes/",
				"/home/medhat/source/ARDEN/genomeTest/",
				"bowtie2",
				"/home/medhat/NGS/Data/Sample_AT1/shRe_filtered_good_reads.fastq");
/*		for (Iterator iterator = chr.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);

		}*/


	}

}
