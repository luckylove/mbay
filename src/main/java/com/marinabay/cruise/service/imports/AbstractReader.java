package com.marinabay.cruise.service.imports;

import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.List;

public abstract class AbstractReader<T> implements Reader<T> {

    protected String extension;

	public final boolean isFileValid(String fileName) throws Exception {
		if(StringUtils.isNotEmpty(fileName)) {
			int indexOf = fileName.lastIndexOf(".");
			String extension = null;
			if(indexOf > 1) {
				extension = fileName.substring(indexOf + 1);
			}
			List<String> list = getExtension();
			if(StringUtils.isEmpty(extension)) {
				throw new Exception("This file is not allow");
			}
            this.extension = extension;
			for (String ex : list) {
				if(ex.trim().equals(extension)) {
					return true;
				}
			}

		}
		throw new Exception("This file is not allow");
	}

	public final List<T> getResults(InputStream is, String fileName) throws Exception {
		isFileValid(fileName);
		List<T> results=  getData(is);
		return results;
	}

	public abstract List<String> getExtension();

	public abstract List<T> getData(InputStream is) throws Exception;


}
