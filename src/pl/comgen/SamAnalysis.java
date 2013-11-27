/**
 * 
 */
package pl.comgen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author medhat
 *
 */
public class SamAnalysis {

	public BufferedReader readSam(File samFile) {
		if (samFile == null || !samFile.exists())
			return null;
		BufferedReader samReader = null;
		try {
			samReader = new BufferedReader(new FileReader(samFile));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return samReader;
	}

	public void calculateMismatches(BufferedReader samBuffer) {
		try {
			if(samBuffer != null && samBuffer.ready()){
				String currentLine;
				while ( (currentLine = samBuffer.readLine()) != null) {
					if(!currentLine.startsWith("@")){
					String[] column= currentLine.split("\t");
					System.out.println(column[16]);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

public static void main(String[] args) {
 SamAnalysis analysis = new SamAnalysis();
 BufferedReader samBuffer = analysis.readSam(new File("/home/medhat/Downloads/test.sam"));
 analysis.calculateMismatches(samBuffer);
 
}
}
