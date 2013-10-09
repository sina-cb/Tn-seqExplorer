package essgenes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class AddColumns {

	private static Logger logger = Logger.getLogger(AddColumns.class.getName());
	
	public static String add(String libName, String tableName, int windowLen, int step, String adjustStartString, String adjustEndString, int seqLen, ProjectInfo info) throws IOException {
	
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
		String line = br.readLine();
		while(line != null){
			insertions.add(Integer.parseInt(line.substring(0, line.indexOf("\t"))));
			line = br.readLine();
		}
		
		ArrayList<Integer> numberOfInsertions = new ArrayList<>();
		for(int i = 0; i < insertions.size(); i++){
			numberOfInsertions.add(0);
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
						N++;
					}
				}
			}else{
				for (int j = 0; j < insertions.size(); ++j){
					if(insertions.get(j) > i && insertions.get(j) <= seqLen){
						++N;
					}
					if(insertions.get(j) <= LL - seqLen){
						++N;
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
		bw.write(line + "\t" + libName + "\n");
		
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
				//TODO: Needs more debug
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
	
}
