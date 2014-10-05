package com.marinabay.cruise.service;

import com.marinabay.cruise.dao.GenericDao;
import com.marinabay.cruise.model.GenericModel;
import com.marinabay.cruise.model.PagingModel;

import java.util.List;


public abstract class GenericService<T extends GenericModel>{

	public abstract GenericDao<T> getDao();

	public int deleteByID(long id) {
		return getDao().deleteByID(id);
	}

	public void insert(T record) {
		getDao().insert(record);
	}

    public Long count(PagingModel model) {
        return getDao().count(model);
    }

	public List<T> select(PagingModel model) {
		return getDao().select(model);
	}

	public T selectByID(long id) {
		return getDao().selectByID(id);
	}
	
	public void update(T record) {
		getDao().update(record);
	}

}
