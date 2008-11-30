package jp.rough_diamond.ant.taskdefs;

import java.io.File;

import jp.rough_diamond.tools.servicegen.ServiceGenerator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Delete;

public class ServiceGeneratorTask extends Task {
	@Override
	public void execute() throws BuildException {
		try {
			//ディレクトリの場合はmuleConfigが省略されたと判断する
			ServiceGenerator gen = new ServiceGenerator(input, srcDir, (muleConfigFile.isDirectory()) ? null : muleConfigFile, version);
			gen.doIt();
			deletebackupFile();
		} catch(Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}
	
	void deletebackupFile() {
		if(!muleConfigFile.isDirectory()) {
			Delete task = new Delete();
			task.setProject(getProject());
			task.setDir(muleConfigFile.getParentFile());
			task.setIncludes(muleConfigFile.getName() + ".*");
			task.execute();
		}
	}

	public File getInput() {
		return input;
	}
	public void setInput(File input) {
		this.input = input;
	}
	public File getSrcDir() {
		return srcDir;
	}
	public void setSrcDir(File srcDir) {
		this.srcDir = srcDir;
	}
	public File getMuleConfigFile() {
		return muleConfigFile;
	}
	public void setMuleConfigFile(File muleConfigFile) {
		this.muleConfigFile = muleConfigFile;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	private File input;
	private File srcDir;
	private File muleConfigFile;
	private String version;
}
