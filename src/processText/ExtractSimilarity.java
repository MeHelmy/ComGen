package processText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author medhat
 *
 */
public class ExtractSimilarity {
	
	
 

	
	/**
	 * @param uniqueMapsReadsFile
	 * @return pacBioIllumina list of string
	 */
	public List<String> createList(String uniqueMapsReadsFile ) {
		List<String> pacBioIllumina = new ArrayList<String>();
		if (!uniqueMapsReadsFile.isEmpty()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(uniqueMapsReadsFile));
				String sCurrentLine;
				String[] lineSplitted; 
				String pacBioRead;
				String[] illuminaRead;
				while ((sCurrentLine = br.readLine()) != null) {
					// concate pacbio to illumina read and add it to the arraylist;
					lineSplitted = sCurrentLine.split("\\s+");
					pacBioRead = lineSplitted[0];
					int sizeOfArray = lineSplitted.length;
					illuminaRead = Arrays.copyOfRange(lineSplitted, 1, sizeOfArray);
					for (String string : illuminaRead) {
						// add to the list Illumina read tab pacbio read
						pacBioIllumina.add(string+pacBioRead);
						
					}
					
					
				}
				
			} catch (IOException e) {
				System.err.println("sorry problem in opening the file "+ uniqueMapsReadsFile + " ERROR: "+e.getMessage());
			}finally{
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					
				}
			}
			
		}
		return pacBioIllumina;
	}

/**
 * @param pacBioIllumina
 * @param alignmentFile
 * @param coordinateFile
 */
public void writeCoordinate(List<String> pacBioIllumina , String alignmentFile , String coordinateFile) {
	if (!pacBioIllumina.isEmpty() && !alignmentFile.isEmpty() && ! coordinateFile.isEmpty()) {
		// open both buffer reader and writer 
		BufferedReader br = null;
		BufferedWriter bw =null;
		File file = new File(coordinateFile);
		String sCurrentLine;
		try {
			//read all he alignment file to compare each line in with what i have in the arraylist;			
			br = new BufferedReader(new FileReader(alignmentFile));
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			String[] splitedLine;
			String alignmentRead;
			int arrayLength=0;
			String alignmentBeginEnd;
			while ((sCurrentLine = br.readLine()) != null){
				if (!sCurrentLine.startsWith("#")) {
				// for each line concate pacbio to illumina read and find it in the list if it exist get the data from the line;
				splitedLine = sCurrentLine.split("\\s+");
				// form the id to search with
				alignmentRead = (splitedLine[0].substring(0, splitedLine[0].length() - 2))+splitedLine[1];

				if(pacBioIllumina.contains(alignmentRead)){
					// get your data to file;
					arrayLength = splitedLine.length;
					// data to be wrriten from 
					// 0:21:10:1858:1882:3:58:1	m141217_081754_42149_c100744762550000001823165807071546_s1_p0/87777/0_8817	100.00	58	0	0	1	58	7990	8047	7e-22	  108
					alignmentBeginEnd = splitedLine[0]+"\t"+splitedLine[1]+"\t"+splitedLine[arrayLength-4]+"\t"+splitedLine[arrayLength-3]+"\t"+(Integer.valueOf(splitedLine[arrayLength-3])-Integer.valueOf(splitedLine[arrayLength-4]));	
					// time to write result in file 
					bw.write(alignmentBeginEnd+"\n");
				}
				
			}	
			}
		} catch (IOException e) {
			System.err.println("sorry problem in opening the file " + " ERROR: "+e.getMessage());
		}finally{
			
				try {
					if(br != null)br.close();
					if(bw != null)bw.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
			
		}
		
	}
	
}
public static void main(String[] args) {
	String uniqueMaps = args[0];
	String alignmentPath =args[1];
	String writeFile = args[2];
	ExtractSimilarity testExtractSimilarity = new ExtractSimilarity();
	List<String> uniqueReads = testExtractSimilarity.createList(uniqueMaps);

	testExtractSimilarity.writeCoordinate(uniqueReads, alignmentPath, writeFile);
}
}

