package com.tathvatech.common.dao;

import com.tathvatech.common.entity.AbstractEntity;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao {
 //   void setClazz(Class< T > clazzToSet);

    AbstractEntity findOne(Class clazz, final long id);

    List<AbstractEntity> findAll(Class clazz);

    AbstractEntity create(final AbstractEntity entity);

    AbstractEntity update(final AbstractEntity entity);

    void delete(final AbstractEntity entity);

    void deleteById(Class clazz, final long entityId);
}