/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.dbcontroller;

import com.anritsu.mcrepositorymanager.dbbeans.DBMcActivities;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcReleases;
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
public class DBMcActivitiesJpaController implements Serializable {

    public DBMcActivitiesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DBMcActivities DBMcActivities) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcReleases releaseId = DBMcActivities.getReleaseId();
            if (releaseId != null) {
                releaseId = em.getReference(releaseId.getClass(), releaseId.getReleaseId());
                DBMcActivities.setReleaseId(releaseId);
            }
            em.persist(DBMcActivities);
            if (releaseId != null) {
                releaseId.getDBMcActivitiesCollection().add(DBMcActivities);
                releaseId = em.merge(releaseId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DBMcActivities DBMcActivities) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcActivities persistentDBMcActivities = em.find(DBMcActivities.class, DBMcActivities.getActivityId());
            DBMcReleases releaseIdOld = persistentDBMcActivities.getReleaseId();
            DBMcReleases releaseIdNew = DBMcActivities.getReleaseId();
            if (releaseIdNew != null) {
                releaseIdNew = em.getReference(releaseIdNew.getClass(), releaseIdNew.getReleaseId());
                DBMcActivities.setReleaseId(releaseIdNew);
            }
            DBMcActivities = em.merge(DBMcActivities);
            if (releaseIdOld != null && !releaseIdOld.equals(releaseIdNew)) {
                releaseIdOld.getDBMcActivitiesCollection().remove(DBMcActivities);
                releaseIdOld = em.merge(releaseIdOld);
            }
            if (releaseIdNew != null && !releaseIdNew.equals(releaseIdOld)) {
                releaseIdNew.getDBMcActivitiesCollection().add(DBMcActivities);
                releaseIdNew = em.merge(releaseIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = DBMcActivities.getActivityId();
                if (findDBMcActivities(id) == null) {
                    throw new NonexistentEntityException("The dBMcActivities with id " + id + " no longer exists.");
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
            DBMcActivities DBMcActivities;
            try {
                DBMcActivities = em.getReference(DBMcActivities.class, id);
                DBMcActivities.getActivityId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The DBMcActivities with id " + id + " no longer exists.", enfe);
            }
            DBMcReleases releaseId = DBMcActivities.getReleaseId();
            if (releaseId != null) {
                releaseId.getDBMcActivitiesCollection().remove(DBMcActivities);
                releaseId = em.merge(releaseId);
            }
            em.remove(DBMcActivities);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DBMcActivities> findDBMcActivitiesEntities() {
        return findDBMcActivitiesEntities(true, -1, -1);
    }

    public List<DBMcActivities> findDBMcActivitiesEntities(int maxResults, int firstResult) {
        return findDBMcActivitiesEntities(false, maxResults, firstResult);
    }

    private List<DBMcActivities> findDBMcActivitiesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DBMcActivities.class));
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

    public DBMcActivities findDBMcActivities(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DBMcActivities.class, id);
        } finally {
            em.close();
        }
    }

    public int getDBMcActivitiesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DBMcActivities> rt = cq.from(DBMcActivities.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
