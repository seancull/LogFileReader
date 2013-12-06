package dk.majkilde.logreader.source;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.majkilde.logreader.files.Directory;
import dk.majkilde.logreader.files.IFile;
import dk.majkilde.logreader.files.XMLFile;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class XMLFileList implements Serializable, IFileList {

	private final Logger log = LogManager.getLogger();
	private IFile current = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pattern = "";
	private final String title = "";

	private final List<IFile> files = new ArrayList<IFile>();

	public XMLFileList(XML config) {
		this.pattern = config.child("filename").content();
		String xlsFilename = config.child("transform").content();

		List<String> filenames = Directory.getFileNames(pattern);
		for (String filename : filenames) {

			files.add(new XMLFile(filename, xlsFilename));
		}

		Collections.sort(files);

	}

	public String getPattern() {
		return Directory.getCleanPattern(pattern);
	}

	public String getPath() {
		return NotesStrings.strLeftBack(getPattern(), File.separator);
	}

	public IFile getNewest() {
		if (files.size() > 0) {
			return files.get(0);
		} else {
			return null;
		}
	}

	public List<IFile> getFiles() {
		log.trace("Trace: getFiles({0})", pattern);
		if (files.size() == 0)
			return null;
		return files;
	}

	public int getCount() {
		return files.size();
	}

	public void setCurrent(IFile current) {
		this.current = current;
		XSPUtils.getViewScope().put("currentFile", current);
	}

	public IFile getCurrent() {
		if (current == null) {
			current = getNewest();
		}
		return current;
	}

}
