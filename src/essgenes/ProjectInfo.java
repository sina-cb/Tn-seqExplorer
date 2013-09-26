package essgenes;

import java.io.File;

public class ProjectInfo {

	private String name;
	private String path;
	private int sequenceLen;
	private File geneFile;
	private File projectFile;
	
	public void createFile(String path) {
		projectFile = new File(path);
	}
	
	public File getFile(){
		return projectFile;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getSequenceLen() {
		return sequenceLen;
	}
	public void setSequenceLen(int sequenceLen) {
		this.sequenceLen = sequenceLen;
	}

	public File getGeneFile() {
		return geneFile;
	}

	public void setGeneFile(File geneFile) {
		this.geneFile = geneFile;
	}
}
