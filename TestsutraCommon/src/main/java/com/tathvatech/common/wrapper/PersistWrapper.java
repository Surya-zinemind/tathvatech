package com.tathvatech.common.wrapper;

import com.tathvatech.common.entity.AbstractEntity;

import java.util.List;

public interface PersistWrapper {

	AbstractEntity readByPrimaryKey(final Class <? extends AbstractEntity> objectClass, long id);

	static <T> List<T> readList(final Class<T> objectClass, final String sql, final Object... parameters);

	static <T> T read(final Class<T> objectClass, final String sql, final Object... parameters);

	List<? extends AbstractEntity> readAll(final Class<? extends AbstractEntity> objectClass);

	long createEntity(AbstractEntity inObject) throws Exception;

	AbstractEntity update(AbstractEntity inObject) throws Exception;

	int executeUpdate(String sql, Object... parameters) throws Exception;

	void deleteEntity(AbstractEntity inObject) throws Exception;

	void deleteEntity(Class objClass, long pk) throws Exception;

	static int delete(String whereClause, Object... parameters) throws Exception;


}
