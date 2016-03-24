import java.util.regex.Matcher
import java.io.File

class FileNameWrapper {
	String baseDir;
	String offsetDir;
	String fileName;
	String filePath;
	String alias;
	String aliasDirectory;

	FileNameWrapper(String aliasDirectory){
		this.aliasDirectory = aliasDirectory
	}

	def determineAlias(File path) {
		if (aliasDirectory != null) {
			// Note that it is not necessary for an offset dir to exist ...
			Matcher matcher = path =~ /(.*?$aliasDirectory)(.*\/)?([^\/]*$)/
			if (matcher.find()) {
				this.filePath = matcher.group()
				this.baseDir = matcher.group(1)
				this.offsetDir = matcher.group(2)
				this.fileName = matcher.group(3)
				this.alias = baseDir + fileName
			}
		}
	}

	@Override
	boolean equals(Object o) {
		if(!(o instanceof FileNameWrapper))
			return false
		else
			return ((FileNameWrapper) o).getAlias().equals(alias);
	}

	String getAlias(){
		return alias 
	}

	String getFilePath() {
		return filePath
	}

	String getAliasDirectory() {
		return aliasDirectory
	}
}
