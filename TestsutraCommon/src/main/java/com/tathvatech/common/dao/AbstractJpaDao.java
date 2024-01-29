package com.tathvatech.common.dao;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractJpaDao<T extends Serializable> {
//    private Class<T> clazz;

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

//    public final void setClazz(final Class<T> clazzToSet) {
//        this.clazz = clazzToSet;
//    }

    public AbstractEntity findOne(Class clazz, final long id)
    {
        return (AbstractEntity) entityManager.find(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<AbstractEntity> findAll(Class clazz) {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    public AbstractEntity create(final AbstractEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    public AbstractEntity update(final AbstractEntity entity) {
        return entityManager.merge(entity);
    }

    public void delete(final AbstractEntity entity) {
        entityManager.remove(entity);
    }

    public void deleteById(Class clazz, final long entityId) {
        final AbstractEntity entity = findOne(clazz, entityId);
        delete(entity);
    }
}