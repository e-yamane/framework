package jp.rough_diamond.ant.taskdefs;

import java.io.File;

import jp.rough_diamond.tools.makewsdl.GetAndMakeWSDL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

public class GetAndMakeWSDLTask extends Task {
	@Override
	public void execute() throws BuildException {
		Java java = new Java();
		java.setProject(getProject());
		java.setTaskName("java");
		java.setClassname(GetAndMakeWSDL.class.getName());
		java.setFork(true);
		java.createArg().setFile(getWsdlStorageDir());
		java.setClasspath((Path)getProject().getReference(getClassPathRef()));
		java.execute();
	}

	private File wsdlStorageDir;
	private String classPathRef;
	
	public File getWsdlStorageDir() {
		return wsdlStorageDir;
	}

	public void setWsdlStorageDir(File wsdlStorageDir) {
		this.wsdlStorageDir = wsdlStorageDir;
	}

	public String getClassPathRef() {
		return classPathRef;
	}

	public void setClassPathRef(String classPathRef) {
		this.classPathRef = classPathRef;
	}

}
