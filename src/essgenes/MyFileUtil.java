package essgenes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MyFileUtil {

	public static String tail( File file ) {
		RandomAccessFile fileHandler = null;
		try {
			fileHandler = new RandomAccessFile( file, "r" );
			long fileLength = fileHandler.length() - 1;
			StringBuilder sb = new StringBuilder();

			for(long filePointer = fileLength; filePointer != -1; filePointer--){
				fileHandler.seek( filePointer );
				int readByte = fileHandler.readByte();

				if( readByte == 0xA ) {
					if( filePointer == fileLength ) {
						continue;
					} else {
						break;
					}
				} else if( readByte == 0xD ) {
					if( filePointer == fileLength - 1 ) {
						continue;
					} else {
						break;
					}
				}

				sb.append( ( char ) readByte );
			}

			String lastLine = sb.reverse().toString();
			return lastLine;
		} catch( java.io.FileNotFoundException e ) {
			e.printStackTrace();
			return null;
		} catch( java.io.IOException e ) {
			e.printStackTrace();
			return null;
		} finally {
			if (fileHandler != null )
				try {
					fileHandler.close();
				} catch (IOException e) {
					/* ignore */
				}
		}
	}

	public static ArrayList<String> tailNLines(File file, int linesCount) throws FileNotFoundException, IOException{
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

		ArrayList<String> result = new ArrayList<String>();

		int lines = 0;
		StringBuilder builder = new StringBuilder();
		long length = file.length();
		length--;        
		randomAccessFile.seek(length);
		for(long seek = length; seek >= 0; --seek){
			randomAccessFile.seek(seek);
			char c = (char)randomAccessFile.read();
			builder.append(c);
			if(c == '\n'){
				builder = builder.reverse();
				result.add(builder.toString());
				lines++;
				builder = null;
				builder = new StringBuilder();
				if (lines == linesCount){
					break;
				}
			}
		}
		randomAccessFile.close();
		return result;
	}

}