package processText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextProcess {
	
	
	public static void main(String[] args) {
		String input="";
		String output="";
		// in.nextLine(); reading from the line input
		if (args.length > 0){
			input = args[0];
			output = args[1];
		}
		TextProcess obj = new TextProcess();
		obj.run(input,output);
	}

	private void run(String input,String output) {
		String csvFile="";
		if(input.length()>0){
		 csvFile = input;
		}
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<Integer> myList = new ArrayList<Integer>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			String headerLine = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] freq = line.split(cvsSplitBy);
				int readLength = Integer.parseInt(freq[0]);
				int readfreq = Integer.parseInt(freq[1]);
				//System.out.println(readfreq+"  "+readLength);
				for (int i = 0; i < readfreq; i++) {
					myList.add(readLength);
					
				}
				
				
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
		try {
			writeResult(myList , output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeResult(List<Integer> myList,String output) throws IOException {
		BufferedWriter out = null;
		String outFile="";
		if(output.length()>0){
		 outFile = output;
		}
		try{
			 FileWriter fstream = new FileWriter(outFile, true); //true tells to append data.
			    out = new BufferedWriter(fstream);
			System.out.println("length of the list id  "+myList.size());
		if (!myList.isEmpty()) {
			Iterator<Integer> iterator = myList.iterator();
			while (iterator.hasNext()) {
				//System.out.println(iterator.next());
				out.write(iterator.next()+"\n");
			}
			
			
		}
		}catch (IOException e)
		{
		    System.err.println("Error: " + e.getMessage());
		}
		finally
		{
		    if(out != null) {
		        out.close();
		        System.out.println("opeation done");
		    }
		}
	}

}
