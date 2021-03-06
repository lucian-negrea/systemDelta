/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.dbcontroller;

import com.anritsu.mcrepositorymanager.dbbeans.DBMcAvailabilities;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcDependencies;
import com.anritsu.mcrepositorymanager.dbcontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ro100051
 */
public class DBMcDependenciesJpaController implements Serializable {

    public DBMcDependenciesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DBMcDependencies DBMcDependencies) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcAvailabilities availabilityId = DBMcDependencies.getAvailabilityId();
            if (availabilityId != null) {
                availabilityId = em.getReference(availabilityId.getClass(), availabilityId.getAvailabilityId());
                DBMcDependencies.setAvailabilityId(availabilityId);
            }
            em.persist(DBMcDependencies);
            if (availabilityId != null) {
                availabilityId.getDBMcDependenciesCollection().add(DBMcDependencies);
                availabilityId = em.merge(availabilityId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DBMcDependencies DBMcDependencies) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcDependencies persistentDBMcDependencies = em.find(DBMcDependencies.class, DBMcDependencies.getDependencyId());
            DBMcAvailabilities availabilityIdOld = persistentDBMcDependencies.getAvailabilityId();
            DBMcAvailabilities availabilityIdNew = DBMcDependencies.getAvailabilityId();
            if (availabilityIdNew != null) {
                availabilityIdNew = em.getReference(availabilityIdNew.getClass(), availabilityIdNew.getAvailabilityId());
                DBMcDependencies.setAvailabilityId(availabilityIdNew);
            }
            DBMcDependencies = em.merge(DBMcDependencies);
            if (availabilityIdOld != null && !availabilityIdOld.equals(availabilityIdNew)) {
                availabilityIdOld.getDBMcDependenciesCollection().remove(DBMcDependencies);
                availabilityIdOld = em.merge(availabilityIdOld);
            }
            if (availabilityIdNew != null && !availabilityIdNew.equals(availabilityIdOld)) {
                availabilityIdNew.getDBMcDependenciesCollection().add(DBMcDependencies);
                availabilityIdNew = em.merge(availabilityIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = DBMcDependencies.getDependencyId();
                if (findDBMcDependencies(id) == null) {
                    throw new NonexistentEntityException("The dBMcDependencies with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcDependencies DBMcDependencies;
            try {
                DBMcDependencies = em.getReference(DBMcDependencies.class, id);
                DBMcDependencies.getDependencyId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The DBMcDependencies with id " + id + " no longer exists.", enfe);
            }
            DBMcAvailabilities availabilityId = DBMcDependencies.getAvailabilityId();
            if (availabilityId != null) {
                availabilityId.getDBMcDependenciesCollection().remove(DBMcDependencies);
                availabilityId = em.merge(availabilityId);
            }
            em.remove(DBMcDependencies);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DBMcDependencies> findDBMcDependenciesEntities() {
        return findDBMcDependenciesEntities(true, -1, -1);
    }

    public List<DBMcDependencies> findDBMcDependenciesEntities(int maxResults, int firstResult) {
        return findDBMcDependenciesEntities(false, maxResults, firstResult);
    }

    private List<DBMcDependencies> findDBMcDependenciesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DBMcDependencies.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DBMcDependencies findDBMcDependencies(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DBMcDependencies.class, id);
        } finally {
            em.close();
        }
    }

    public int getDBMcDependenciesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DBMcDependencies> rt = cq.from(DBMcDependencies.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
