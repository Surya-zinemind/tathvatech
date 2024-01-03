package com.tathvatech.common.wrapper;

import java.util.List;

public interface PersistWrapper {

	<T> List<T> readList(final Class<T> objectClass, final String sql, final Object...parameters);

	long createEntity(Object inObject) throws Exception;

	void update(Object inObject);
}
