package com.marinabay.cruise.dao;

import com.marinabay.cruise.model.PagingModel;

import java.util.List;

public interface GenericDao<T> {
	
	public int deleteByID(Long id);
	
	public void insert(T record);
	
	public List<T> select(PagingModel model);

	public Long count(PagingModel model);

	public T selectByID(long id);
	
	public int update(T record);
	
	
}
