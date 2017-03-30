package processText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * pseudo code for getting info from the blast file
for read in illumina_read:
    with open(input_file_blast,'r') as input_data_blast:
        for blast_line in input_data_blast:
           
                blast_line_split=blast_line.split()
output_file_read.write(blast_line_split[0]+'\t'+blast_line_split[1]+'\t'+str(blast_line_split[-4])+"\t"+str(blast_line_split[-3])+"\t"+str(int(blast_line_split[-3])-int(blast_line_split[-4]))+"\n")

*/

public class AlignSearch {
	
public void readFile(String pathToUniqueMapFile , String pathToAlignment , String writeFile) {
	if(!pathToUniqueMapFile.isEmpty()){
		
		BufferedReader br = null;
		BufferedWriter bw =null;
		File file = new File(writeFile);
		BufferedReader readAlignment = null;
		 
		

		 
		try {
 
			String sCurrentLine;
			String[] lineSplitted;
			String pacBioRead;
			String[] illuminaRead;
 
			br = new BufferedReader(new FileReader(pathToUniqueMapFile));
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			while ((sCurrentLine = br.readLine()) != null) {
				lineSplitted = sCurrentLine.split("\\s+");
				pacBioRead = lineSplitted[0];
				int sizeOfArray = lineSplitted.length;
				illuminaRead = Arrays.copyOfRange(lineSplitted, 1, sizeOfArray);
				String alignmentLine;
				String[] splitedAlignmeny;
				String alignmentBeginEnd;
				int arrayLength=0;
				for (String string : illuminaRead) {
					try {
						// I think it should be opened out of the for loop
						
						//here opened the file to read from it and search the string 
						readAlignment = new BufferedReader(new FileReader(pathToAlignment));
						int numberOfHits = 0;
						while ((alignmentLine = readAlignment.readLine()) != null) {
							if (!alignmentLine.startsWith("#")) {
								// now alignmentRead have line of alignment
								// we need to  if re.findall((read+":"+'[1-2]\t'+pacbio_read),blast_line):
								// pattern to be used Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();
								Pattern MY_PATTERN = Pattern.compile(string + "(:[1-2]\\s+)"+ pacBioRead);
								Matcher m = MY_PATTERN.matcher(alignmentLine);
								if(m.find() == true){
									System.out.println("found");
									splitedAlignmeny = alignmentLine.split("\\s+");
									arrayLength = splitedAlignmeny.length;
									alignmentBeginEnd = splitedAlignmeny[0]+"\t"+splitedAlignmeny[1]+"\t"+splitedAlignmeny[arrayLength-4]+"\t"+splitedAlignmeny[arrayLength-3]+"\t"+(Integer.valueOf(splitedAlignmeny[arrayLength-3])-Integer.valueOf(splitedAlignmeny[arrayLength-4]));	
									// time to write result in file 
									bw.write(alignmentBeginEnd+"\n");
									numberOfHits++;
								}
							}
							if(numberOfHits == 2){
								System.out.println("file closed");
								readAlignment.close();
								break;
							}
						}
						
					} catch (IOException e) {
						System.err.println(e.getMessage());					}
					
					
					
				}
			}
			
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				bw.close();
				System.out.println("Done");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
	public static void main(String[] args) {
		String filePath = args[0];
		String alignmentPath =args[1];
		String writeFile = args[2];
		AlignSearch testAlignSearch = new AlignSearch();
		testAlignSearch.readFile(filePath,alignmentPath,writeFile);
				
	}

}
