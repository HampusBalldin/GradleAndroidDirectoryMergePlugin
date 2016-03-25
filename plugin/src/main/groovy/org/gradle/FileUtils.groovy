package org.hampus.android.packaging
import java.io.File
import java.util.regex.Matcher

class FileUtils{
	private static final CONFIG_FILE = "packaging.conf"

	void collectFilesToMove(File f, moved, String targetDir){
		if(f.isDirectory()){
			f.eachFile {
				collectFilesToMove(it, moved, targetDir)
			}
		}else{

			String parentFilePath = f.getParentFile().absolutePath + "/"
			if(!parentFilePath.equals(targetDir)) {
				println parentFilePath 
				println targetDir
				moved << f
			}
		}
	}

	boolean moveFile(String from, String to){
		if(from != null && to != null){
			File file = new File(from)
			File dest = new File(to)
			return file.renameTo(dest)
		}
		return false
	}
	
	// Allowed input sequences:
	// </DIR/>, </DIR>, </DIR/>, <DIR>
	// All will give <WORKING DIRECTORY>/<DIR>/

	String ILLEGAL_DIR_CHARACTERS = "\\*\\.\"\\/\\\\\\[\\]:;\\|=,"
	List<String> getLines(){
		String path = getConfPath()
		List<String> lines = []

		new File(path).text.split('\n').each { line ->
			Matcher matcher = line =~ /\/?([^$ILLEGAL_DIR_CHARACTERS]+)\/?/
			def sb = new StringBuffer()
			while(matcher.find()) {
				sb.append(matcher.group(1)).append("/")
			}
			lines << System.getProperty("user.dir") + "/" + sb.toString()
		}
		return lines
	}

	String getConfPath() {
		return System.getProperty("user.dir") + "/" + CONFIG_FILE
	}
}
