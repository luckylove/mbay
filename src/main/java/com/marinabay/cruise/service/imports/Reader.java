package com.marinabay.cruise.service.imports;

import java.io.InputStream;
import java.util.List;

public interface Reader<T> {

	public boolean isFileValid(String fileName) throws Exception;

	public List<T> getResults(InputStream is, String fileName) throws Exception;
}
