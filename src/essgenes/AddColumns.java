package essgenes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

public class AddColumns {

	private static Logger logger = Logger.getLogger(AddColumns.class.getName());
	
	@SuppressWarnings("resource")
	public static String countInsertions(String libName, String tableName, String adjustStartString, String adjustEndString, boolean ifUniqueInsertions, ProjectInfo info) throws IOException{
		
		int adjustStart;
		double adjustStartPercent;
		int adjustEnd;
		double adjustEndPercent;
		
		if (adjustStartString.contains("%")){
			adjustStart = 0;
			adjustStartPercent = Integer.parseInt(adjustStartString.substring(0, adjustStartString.length() - 1)) / 100.0;
		}else{
			adjustStart = Integer.parseInt(adjustStartString);
			adjustStartPercent = 0.0;
		}
		
		if (adjustEndString.contains("%")){
			adjustEnd = 0;
			adjustEndPercent = Integer.parseInt(adjustEndString.substring(0, adjustEndString.length() - 1)) / 100.0;
		}else{
			adjustEnd = Integer.parseInt(adjustEndString);
			adjustEndPercent = 0.0;
		}
		
		int LSeq = info.getSequenceLen();
		
		File libFile = new File(info.getPath() + libName + ".inspou");
		BufferedReader br = new BufferedReader(new FileReader(libFile));
		
		ArrayList<Integer> insertions = new ArrayList<>();
		for (int i = 0; i < LSeq; i++){
			insertions.add(0);
		}
		
		String line = br.readLine();
		while(line != null){
			int tempPos = Integer.parseInt(line.substring(0, line.indexOf("\t")));
			line = line.substring(line.indexOf("\t") + 1);
			line = line.substring(line.indexOf("\t") + 1);
			int tempNum = Integer.parseInt(line);
			if (tempPos > LSeq){
				JOptionPane.showMessageDialog(null, "ERROR: insertion at position " + tempPos + " whereas chromosome length is " + LSeq + ".", "Error", JOptionPane.ERROR_MESSAGE);
				return Messages.failMsg;
			}
			
			if (ifUniqueInsertions){
				insertions.set(tempPos, insertions.get(tempPos) + 1);
			}else{
				insertions.set(tempPos, insertions.get(tempPos) + tempNum);
			}
			line = br.readLine();
		}
		
		br.close();
		File tableFile = new File(info.getPath() + tableName + ".table");
		br = new BufferedReader(new FileReader(tableFile));
		
		File newTableFile = new File(info.getPath() + tableName + ".new");
		BufferedWriter bw = new BufferedWriter(new FileWriter(newTableFile));

		//Writing File Header at First
		line = br.readLine();
		bw.write(line + "\t" + "" + "\n");

		line = br.readLine();
		bw.write(line + "\t" + "" + "\n");

		line = br.readLine();
		if (ifUniqueInsertions){
			bw.write(line + "\tunique_insertion_counts: " + libName + "\n");
		}else{
			bw.write(line + "\tall_reads_counts: " + libName + "\n");
		}

		line = br.readLine();
		bw.write(line + "\t" + adjustStartString + "\n");

		line = br.readLine();
		bw.write(line + "\t" + adjustEndString + "\n");

		//Reading Main Data and Process it
		line = br.readLine();
		while(line != null){
			String tempLine = new String(line);
			Tabs gene = new Tabs(tempLine);

			int GeneL = gene.getStart_coord();
			int GeneR = gene.getEnd_coord();
			
			int start = gene.getStart_coord();
			int end = gene.getEnd_coord();
			int geneLen = end - start + 1;
			if (gene.getStrand().compareTo("+") == 0){
				start = GeneL - adjustStart;
				end = GeneR + adjustEnd;
				geneLen = end - start + 1;
				start = (int) (GeneL - adjustStartPercent * geneLen);
				end = (int) (GeneR + adjustEndPercent * geneLen);
			}else if (gene.getStrand().compareTo("-") == 0){
				start = GeneL - adjustEnd;
				end = GeneR + adjustStart;
				geneLen = end - start + 1;
				start = (int) (GeneL - adjustEndPercent * geneLen);
				end = (int) (GeneR + adjustStartPercent * geneLen);
			}else{
				logger.error("Gene strand is not + or -  ...  skipping end adjustments");
			}
			
			int count = 0;
			for (int i = start; i <= end; i++){
				count += insertions.get(i);
			}
			
			bw.write(line + "\t" + count + "\n");
			bw.flush();
			
			line = br.readLine();
		}
		
		br.close();
		bw.close();
		
		if(tableFile.delete()){
			if(newTableFile.renameTo(tableFile)){
				return Messages.successMsg;
			}
		}
		
		return Messages.failMsg;
	}
	
	public static String add(String libName, String tableName, int windowLen, int step, String adjustStartString, String adjustEndString, int seqLen, boolean ifUniqueInsertions, ProjectInfo info) throws IOException {
	
		int adjustStart;
		double adjustStartPercent;
		int adjustEnd;
		double adjustEndPercent;
		
		if (adjustStartString.contains("%")){
			adjustStart = 0;
			adjustStartPercent = Integer.parseInt(adjustStartString.substring(0, adjustStartString.length() - 1)) / 100.0;
		}else{
			adjustStart = Integer.parseInt(adjustStartString);
			adjustStartPercent = 0.0;
		}
		
		if (adjustEndString.contains("%")){
			adjustEnd = 0;
			adjustEndPercent = Integer.parseInt(adjustEndString.substring(0, adjustEndString.length() - 1)) / 100.0;
		}else{
			adjustEnd = Integer.parseInt(adjustEndString);
			adjustEndPercent = 0.0;
		}
		
		File libFile = new File(info.getPath() + libName + ".inspou");
		BufferedReader br = new BufferedReader(new FileReader(libFile));
		
		ArrayList<Integer> insertions = new ArrayList<>();
		ArrayList<Integer> numberOfReads = new ArrayList<>();
		String line = br.readLine();
		while(line != null){
			insertions.add(Integer.parseInt(line.substring(0, line.indexOf("\t"))));
			line = line.substring(line.indexOf("\t") + 1);
			line = line.substring(line.indexOf("\t") + 1);
			numberOfReads.add(Integer.parseInt(line));
			line = br.readLine();
		}
		
		ArrayList<Window> windows = new ArrayList<>();
		for (int i = 1; i <= seqLen; i += step){
			
			Window temp = new Window();
			temp.start = i;
			
			int LL = i + windowLen - 1;
			int N = 0;
			
			if(LL < seqLen){
				for (int j = 0; j < insertions.size(); j++){
					if(insertions.get(j) > i && insertions.get(j) <= LL){
						if (ifUniqueInsertions){
							N++;	
						}else{
							N += numberOfReads.get(j);
						}
					}
				}
			}else{
				for (int j = 0; j < insertions.size(); ++j){
					if(insertions.get(j) > i && insertions.get(j) <= seqLen){
						if (ifUniqueInsertions){
							N++;	
						}else{
							N += numberOfReads.get(j);
						}
					}
					if(insertions.get(j) <= LL - seqLen){
						if (ifUniqueInsertions){
							N++;	
						}else{
							N += numberOfReads.get(j);
						}
					}
				}
			}
			temp.N = N;
			windows.add(temp);
		}
		
		br.close();
		File tableFile = new File(info.getPath() + tableName + ".table");
		br = new BufferedReader(new FileReader(tableFile));
		
		File newTableFile = new File(info.getPath() + tableName + ".new");
		BufferedWriter bw = new BufferedWriter(new FileWriter(newTableFile));
		
		//Writing File Header at First
		line = br.readLine();
		if (ifUniqueInsertions){
			bw.write(line + "\t" + libName + "(Counting Unique Insertions)\n");
		}else{
			bw.write(line + "\t" + libName + "(Counting All Reads)\n");
		}
		
		line = br.readLine();
		bw.write(line + "\t" + windowLen + "\n");
		
		line = br.readLine();
		bw.write(line + "\t" + step + "\n");
		
		line = br.readLine();
		bw.write(line + "\t" + adjustStartString + "\n");
		
		line = br.readLine();
		bw.write(line + "\t" + adjustEndString + "\n");
		
		//Reading Main Data and Process it
		line = br.readLine();
		while(line != null){
			String tempLine = new String(line);
			Tabs gene = new Tabs(tempLine);
			
			int start = gene.getStart_coord();
			int end = gene.getEnd_coord();
			int geneLen = end - start + 1;
			
			if(gene.getStrand().compareTo("+") == 0){
				start = gene.getStart_coord() - adjustStart;
				end = gene.getEnd_coord() + adjustEnd;
				geneLen = end - start + 1;
				start = (int) (gene.getStart_coord() - adjustStartPercent * geneLen);
				end = (int) (gene.getEnd_coord() + adjustEndPercent * geneLen);
			}else if(gene.getStrand().compareTo("-") == 0){
				start = gene.getStart_coord() - adjustEnd;
				end = gene.getEnd_coord() + adjustStart;
				geneLen = end - start + 1;
				start = (int) (gene.getStart_coord() - adjustEndPercent * geneLen);
				end = (int) (gene.getEnd_coord() + adjustStartPercent * geneLen);
			}else{
				logger.info("Gene strand is not + or - ... skipping end adjustment");
			}
			
			int LI = ((int) ((double) (end - windowLen + 1) / step)) + 1;
			int LJ = ((int) ((double) (start) / step));
			int N = 0;
			if (LJ < LI){		//Gene is larger than the window
				geneLen = LJ;
				LJ = LI;
				LI = geneLen;
				
				if(LJ > windows.size()){
					break;
				}
				N = windows.get(LJ).N;
				for(geneLen = LI; geneLen <= LJ; ++geneLen){
					if(geneLen > 0){
						if(windows.get(geneLen).N > N){
							N = windows.get(geneLen).N;
						}
					}else{
						int LL = windows.size() + geneLen;
						if(windows.get(LL).N > N){
							N = windows.get(LL).N;
						}
					}
				}
			}else{				//Gene is smaller than the window
				if(LJ >= windows.size()){
					JOptionPane.showMessageDialog(null, "error");
				}
				N = windows.get(LJ).N;
				for (geneLen = LI; geneLen <= LJ; ++geneLen){
					if (geneLen >= 0){
						if(windows.get(geneLen).N < N){
							N = windows.get(geneLen).N;
						}
					}else{
						int LL = windows.size() + geneLen;
						if(windows.get(LL).N < N){
							N = windows.get(LL).N;
						}
					}
				}
			}
			
			bw.write(line + "\t" + N + "\n");
			bw.flush();
			
			line = br.readLine();
			
		}
		
		br.close();
		bw.close();
		
		if(tableFile.delete()){
			if(newTableFile.renameTo(tableFile)){
				return Messages.successMsg;
			}
		}
		
		return Messages.failMsg;
	}
	
	private static class Window{
		@SuppressWarnings("unused")
		public int start;
		public int N;
	}
	
	@SuppressWarnings("unused")
	private static class Tabs{
		private int start_coord;
		private int end_coord;
		private String strand;
		private int length;
		private String locus_type;
		private String locus_tag;
		private String gene_symbol;
		private String description;
		
		public Tabs(String line){
			String temp = line.substring(0, line.indexOf("\t"));
			line = line.substring(line.indexOf("\t") + 1);
			setStart_coord(Integer.parseInt(temp));
			
			temp = line.substring(0, line.indexOf("\t"));
			line = line.substring(line.indexOf("\t") + 1);
			setEnd_coord(Integer.parseInt(temp));
			
			temp = line.substring(0, line.indexOf("\t"));
			line = line.substring(line.indexOf("\t") + 1);
			setStrand(temp);
			
			temp = line.substring(0, line.indexOf("\t"));
			line = line.substring(line.indexOf("\t") + 1);
			setLength(Integer.parseInt(temp));
			
			temp = line.substring(0, line.indexOf("\t"));
			line = line.substring(line.indexOf("\t") + 1);
			setLocus_type(temp);
			
			temp = line.substring(0, line.indexOf("\t"));
			line = line.substring(line.indexOf("\t") + 1);
			setLocus_tag(temp);
			
			temp = line.substring(0, line.indexOf("\t"));
			line = line.substring(line.indexOf("\t") + 1);
			setGene_symbol(temp);
			
			setDescription(line);			
		}
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getGene_symbol() {
			return gene_symbol;
		}
		public void setGene_symbol(String gene_symbol) {
			this.gene_symbol = gene_symbol;
		}
		public String getLocus_tag() {
			return locus_tag;
		}
		public void setLocus_tag(String locus_tag) {
			this.locus_tag = locus_tag;
		}
		public String getLocus_type() {
			return locus_type;
		}
		public void setLocus_type(String locus_type) {
			this.locus_type = locus_type;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public String getStrand() {
			return strand;
		}
		public void setStrand(String strand) {
			this.strand = strand;
		}
		public int getEnd_coord() {
			return end_coord;
		}
		public void setEnd_coord(int end_coord) {
			this.end_coord = end_coord;
		}
		public int getStart_coord() {
			return start_coord;
		}
		public void setStart_coord(int start_coord) {
			this.start_coord = start_coord;
		}
	}

	
	public static DefaultTableModel getHeaderData(String tableName, ProjectInfo info) throws IOException {
		File tableFile = new File(info.getPath() + tableName + ".table");
		BufferedReader br = new BufferedReader(new FileReader(tableFile));
		
		ArrayList<ArrayList<String>> data = new ArrayList<>();
		
		String line = br.readLine();
		int lineCount = 0;
		while(line != null){
			line = line.substring(line.indexOf("\t") + 1);
			line = line.substring(line.indexOf("\t") + 1);
			line = line.substring(line.indexOf("\t") + 1);
			line = line.substring(line.indexOf("\t") + 1);
			line = line.substring(line.indexOf("\t") + 1);
			line = line.substring(line.indexOf("\t") + 1);
			line = line.substring(line.indexOf("\t") + 1);
			if (line.contains("\t"))
				line = line.substring(line.indexOf("\t") + 1);
			else{
				JOptionPane.showMessageDialog(null, "The table is empty and there is no data to compare");
				br.close();
				return null;
			}
			 
			ArrayList<String> row = new ArrayList<>();
			
			while(line.contains("\t")){
				String temp = line.substring(0, line.indexOf("\t"));
				line = line.substring(line.indexOf("\t") + 1);
				
				row.add(temp);
			}
			row.add(line);
			data.add(row);
			
			line = br.readLine();
			
			lineCount++;
			if(lineCount > 4){
				break;
			}
		}
		
		br.close();
		
		int columnCount = data.get(0).size();
		int rowCount = data.size();
		
		String[][] result = new String[rowCount][columnCount + 1];
		for (int i = 0; i < rowCount; i++){
			result[i][0] = getInfo(i);
			for (int j = 0; j < columnCount; j++){
				result[i][j + 1] = data.get(i).get(j);
			}
		}
		
		String[] columnNames = new String[columnCount + 1];
		columnNames[0] = "Info";
		for (int i = 1; i < columnCount + 1; i++){
			columnNames[i] = (i) + "";
		}
		
		
		return new DefaultTableModel(result, columnNames);
	}

	private static String getInfo(int i) {
		switch (i){
		case 0:
			return "Library Name";
		case 1:
			return "Window Size";
		case 2:
			return "Window Step";
		case 3:
			return "Start Adj.";
		case 4:
			return "End Adj.";
		default:
			return "ERROR";
		}
	}

	public static String compareColumns(String tableName, int firstColumn, int secondColumn, int maxIns, ProjectInfo info) throws IOException {

		File tableFile = new File(info.getPath() + tableName + ".table");
		BufferedReader br = new BufferedReader(new FileReader(tableFile));

		File newTableFile = new File(info.getPath() + tableName + ".new");
		BufferedWriter bw = new BufferedWriter(new FileWriter(newTableFile));
		
		//Writing File Header at First
		String line = br.readLine();
		bw.write(line + "\t" + "\n");

		line = br.readLine();
		bw.write(line + "\t" + "\n");

		line = br.readLine();
		bw.write(line + "\t" + "\n");

		line = br.readLine();
		bw.write(line + "\t" + "Compare" + "\n");

		line = br.readLine();
		bw.write(line + "\t" + firstColumn + " - " + secondColumn + "\n");

		//Reading Main Data and Process it
		line = br.readLine();
		while(line != null){
			String tempLine = new String(line);
			ArrayList<String> tabs = tabsForCompare(line);
			
			int one = Integer.parseInt(tabs.get(firstColumn + 7));
			int two = Integer.parseInt(tabs.get(secondColumn + 7));
			
			if (one > maxIns){
				one = maxIns;
			}
			
			if (two > maxIns){
				two = maxIns;
			}
			
			bw.write(tempLine + "\t" + (one - two) + "\n");
			
			line = br.readLine();
		}
		
		bw.close();
		br.close();
		
		if (tableFile.delete()){
			if(newTableFile.renameTo(tableFile)){
				return Messages.successMsg;
			}
		}
		
		return Messages.failMsg;
	}
	
	public static ArrayList<String> tabsForCompare(String line){
		ArrayList<String> results = new ArrayList<>();
		
		while (line.contains("\t")){
			String temp = line.substring(0, line.indexOf("\t"));
			line = line.substring(line.indexOf("\t") + 1);
			results.add(temp);
		}
		results.add(line);
		
		return results;
	}
}
