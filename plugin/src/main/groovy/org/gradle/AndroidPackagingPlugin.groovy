package org.hampus.android.packaging 

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.InvalidUserDataException
import java.io.*

/**
* Merges all files into a specified directory <DIR>. 
* 
* <DIR> MUST be specified in the folder <WORKING_DIRECTORY><packaging.conf>
* 
* Example:
*/
class AndroidPackagingPlugin implements Plugin<Project> {
	Project target;
	FileUtils utils = new FileUtils()
	
	// List of all list of files which have been moved
	def moved = [];
	
	// List of all lists of FileNameWrappers (used from resolved files)
	def wrappedFiles = [];
	void apply(Project target) {
		this.target = target
		
		//Called when all projects for the build have been evaluated. 
		//The project objects are fully configured and are ready to use to 
		//populate the task graph. 
		target.gradle.projectsEvaluated {
			List<String> directories = utils.getLines()

			if(directories.size() == 0){
				throw new InvalidUserDataException("No Directory specified, please specify in " + utils.getConfPath())
			}

			directories.each{dir ->
				println "Evaluating: $dir"
				onProjectsEvaluated(dir)
			}
		}

		// Called when the build is completed. All selected tasks have been executed.
		target.gradle.buildFinished {
			onBuildFinished()
		}
	}

	void onProjectsEvaluated(String targetDirectory){
		def innerMoved = []
		def innerWrappedFiles = []

		File srcDir = target.file(targetDirectory)
		if(!srcDir.isDirectory()){
			println "$targetDirectory does not exist"
			return
		}
		
		srcDir.eachFile {
			utils.collectFilesToMove((File)it, innerMoved, targetDirectory)
		}

		innerMoved.each {
			FileNameWrapper fnw = new FileNameWrapper(targetDirectory)
			fnw.determineAlias(it)
			if(!innerWrappedFiles.contains(fnw)) {
				String fp = fnw.getFilePath()
				String alias = fnw.getAlias()
				if(!fp.equals(alias)){	
					boolean success = utils.moveFile(fnw.getFilePath(), fnw.getAlias())
					innerWrappedFiles << fnw
				}else{
					println "Do not move file in target dir"
				}
			} else{
				throw new InvalidUserDataException("Found Aliased Name ... ")
			}
		}
		moved.addAll(innerMoved)
		wrappedFiles.addAll(innerWrappedFiles)
	}

	void onBuildFinished(){
		println "All that are moved: "
		wrappedFiles.each { 
			String alias = ((FileNameWrapper)it).getAlias()
			String path = ((FileNameWrapper)it).getFilePath()
			boolean success = utils.moveFile(alias, path)
			println "Moved: $path \t Moved Back: $success"
		}
		println "All Done"
	}
}
