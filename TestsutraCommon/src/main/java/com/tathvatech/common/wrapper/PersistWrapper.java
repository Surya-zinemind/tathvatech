package com.tathvatech.common.wrapper;

import com.tathvatech.common.entity.AbstractEntity;

import java.util.List;
import java.util.Map;

public interface PersistWrapper {

	AbstractEntity readByPrimaryKey(final Class <? extends AbstractEntity> objectClass, long id);

	<T> List<T> readList(final Class<T> objectClass, final String sql, final Object...parameters);

	<T> T read(final Class<T> objectClass, final String sql, final Object...parameters);

	List<? extends AbstractEntity> readAll(final Class<? extends AbstractEntity> objectClass);

	long createEntity(AbstractEntity inObject) throws Exception;

	AbstractEntity update(AbstractEntity inObject) throws Exception;

	int executeUpdate(String sql, Object... parameters) throws Exception;

	void deleteEntity(AbstractEntity inObject) throws Exception;

	void deleteEntity(Class objClass, long pk) throws Exception;

	int delete(String whereClause, Object... parameters) throws Exception;

	Map<String, Object> readAsMap(final String sql, final Object...parameters) throws Exception;

	List<Map<String,Object>> readListAsMap(final String sql, final Object...parameters) throws Exception;

}
