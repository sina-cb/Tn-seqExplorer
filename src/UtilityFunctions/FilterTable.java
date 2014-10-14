package UtilityFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FilterTable {

	public static void main(String[] args) throws IOException {
		
		File tableCompare = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\Compare\\SupplementaryFile2-00.xls");
		File knownEssentials = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\known-essentials.xls");
		File knownNonEssentials = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\known-non-essentials.xls");
		File output = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\Compare\\result.xls");
		
		String line = "";

		/////////////////////////////////////////////////////////////////////////////////
		
		BufferedReader br = new BufferedReader(new FileReader(knownEssentials));
		for (int i = 0; i < 3; i++){
			line = br.readLine();
		}
		
		line = br.readLine();
		ArrayList<String> knownEssentialsList = new ArrayList<>();
		while(line != null){
			for (int i = 0; i < 6; i++){
				line = line.substring(line.indexOf("\t") + 1);
			}

			String mmpNum = line.substring(0, line.indexOf("\t"));
			
			knownEssentialsList.add(mmpNum);
			
			line = br.readLine();
		}
		br.close();
		
		/////////////////////////////////////////////////////////////////////////////////
		
		br = new BufferedReader(new FileReader(knownNonEssentials));
		for (int i = 0; i < 3; i++){
			line = br.readLine();
		}

		line = br.readLine();
		ArrayList<String> knownNonEssentialsList = new ArrayList<>();
		while(line != null){
			for (int i = 0; i < 6; i++){
				line = line.substring(line.indexOf("\t") + 1);
			}

			String mmpNum = line.substring(0, line.indexOf("\t"));
			knownNonEssentialsList.add(mmpNum);
			
			line = br.readLine();
		}
		br.close();
		
		/////////////////////////////////////////////////////////////////////////////////

		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		StringBuilder header = new StringBuilder();
		br = new BufferedReader(new FileReader(tableCompare));
		for (int i = 0; i < 4; i++){
			line = br.readLine();
			header.append(line + "\n");
		}
		
		bw.write(header.toString());
		
		line = br.readLine();
		while(line != null){
			GeneHolder gene = new GeneHolder();
			gene.setCompleteLine(new String(line));
		
			for (int i = 0; i < 3; i++){
				line = line.substring(line.indexOf("\t") + 1);
			}
			gene.setMmpNum(line.substring(0, line.indexOf("\t")));
			
			if (knownEssentialsList.contains(gene.getMmpNum())){
				bw.write(gene.getCompleteLine() + "+\n");
			}else if (knownNonEssentialsList.contains(gene.getMmpNum())){
				bw.write(gene.getCompleteLine() + "-\n");
			}else{
				bw.write(gene.getCompleteLine() + "\n");
			}
			
			line = br.readLine();
		}
		br.close();
		bw.close();
		
	}
	
	private static class GeneHolder{
		private String completeLine;
		private String mmpNum;
		
		public GeneHolder(){
			
		}
		
		public String getCompleteLine() {
			return completeLine;
		}
		public void setCompleteLine(String completeLine) {
			this.completeLine = completeLine;
		}
		public String getMmpNum() {
			return mmpNum;
		}
		public void setMmpNum(String mmpNum) {
			this.mmpNum = mmpNum;
		}
	}
	
}
