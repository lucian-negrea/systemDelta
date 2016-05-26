/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.dbcontroller;

import com.anritsu.mcrepositorymanager.dbbeans.DBMcAvailabilities;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcCustomers;
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
public class DBMcCustomersJpaController implements Serializable {

    public DBMcCustomersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DBMcCustomers DBMcCustomers) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcAvailabilities availabilityId = DBMcCustomers.getAvailabilityId();
            if (availabilityId != null) {
                availabilityId = em.getReference(availabilityId.getClass(), availabilityId.getAvailabilityId());
                DBMcCustomers.setAvailabilityId(availabilityId);
            }
            em.persist(DBMcCustomers);
            if (availabilityId != null) {
                availabilityId.getDBMcCustomersCollection().add(DBMcCustomers);
                availabilityId = em.merge(availabilityId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DBMcCustomers DBMcCustomers) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcCustomers persistentDBMcCustomers = em.find(DBMcCustomers.class, DBMcCustomers.getCustomerId());
            DBMcAvailabilities availabilityIdOld = persistentDBMcCustomers.getAvailabilityId();
            DBMcAvailabilities availabilityIdNew = DBMcCustomers.getAvailabilityId();
            if (availabilityIdNew != null) {
                availabilityIdNew = em.getReference(availabilityIdNew.getClass(), availabilityIdNew.getAvailabilityId());
                DBMcCustomers.setAvailabilityId(availabilityIdNew);
            }
            DBMcCustomers = em.merge(DBMcCustomers);
            if (availabilityIdOld != null && !availabilityIdOld.equals(availabilityIdNew)) {
                availabilityIdOld.getDBMcCustomersCollection().remove(DBMcCustomers);
                availabilityIdOld = em.merge(availabilityIdOld);
            }
            if (availabilityIdNew != null && !availabilityIdNew.equals(availabilityIdOld)) {
                availabilityIdNew.getDBMcCustomersCollection().add(DBMcCustomers);
                availabilityIdNew = em.merge(availabilityIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = DBMcCustomers.getCustomerId();
                if (findDBMcCustomers(id) == null) {
                    throw new NonexistentEntityException("The dBMcCustomers with id " + id + " no longer exists.");
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
            DBMcCustomers DBMcCustomers;
            try {
                DBMcCustomers = em.getReference(DBMcCustomers.class, id);
                DBMcCustomers.getCustomerId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The DBMcCustomers with id " + id + " no longer exists.", enfe);
            }
            DBMcAvailabilities availabilityId = DBMcCustomers.getAvailabilityId();
            if (availabilityId != null) {
                availabilityId.getDBMcCustomersCollection().remove(DBMcCustomers);
                availabilityId = em.merge(availabilityId);
            }
            em.remove(DBMcCustomers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DBMcCustomers> findDBMcCustomersEntities() {
        return findDBMcCustomersEntities(true, -1, -1);
    }

    public List<DBMcCustomers> findDBMcCustomersEntities(int maxResults, int firstResult) {
        return findDBMcCustomersEntities(false, maxResults, firstResult);
    }

    private List<DBMcCustomers> findDBMcCustomersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DBMcCustomers.class));
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

    public DBMcCustomers findDBMcCustomers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DBMcCustomers.class, id);
        } finally {
            em.close();
        }
    }

    public int getDBMcCustomersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DBMcCustomers> rt = cq.from(DBMcCustomers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
