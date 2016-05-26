/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.dbcontroller;

import com.anritsu.mcrepositorymanager.dbbeans.DBMcAvailabilities;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcCustomers;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcDependencies;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcReleases;
import com.anritsu.mcrepositorymanager.dbcontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ro100051
 */
public class DBMcAvailabilitiesJpaController implements Serializable {

    public DBMcAvailabilitiesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DBMcAvailabilities DBMcAvailabilities) {
        if (DBMcAvailabilities.getDBMcDependenciesCollection() == null) {
            DBMcAvailabilities.setDBMcDependenciesCollection(new ArrayList<DBMcDependencies>());
        }
        if (DBMcAvailabilities.getDBMcCustomersCollection() == null) {
            DBMcAvailabilities.setDBMcCustomersCollection(new ArrayList<DBMcCustomers>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcReleases releaseId = DBMcAvailabilities.getReleaseId();
            if (releaseId != null) {
                releaseId = em.getReference(releaseId.getClass(), releaseId.getReleaseId());
                DBMcAvailabilities.setReleaseId(releaseId);
            }
            Collection<DBMcDependencies> attachedDBMcDependenciesCollection = new ArrayList<DBMcDependencies>();
            for (DBMcDependencies DBMcDependenciesCollectionDBMcDependenciesToAttach : DBMcAvailabilities.getDBMcDependenciesCollection()) {
                DBMcDependenciesCollectionDBMcDependenciesToAttach = em.getReference(DBMcDependenciesCollectionDBMcDependenciesToAttach.getClass(), DBMcDependenciesCollectionDBMcDependenciesToAttach.getDependencyId());
                attachedDBMcDependenciesCollection.add(DBMcDependenciesCollectionDBMcDependenciesToAttach);
            }
            DBMcAvailabilities.setDBMcDependenciesCollection(attachedDBMcDependenciesCollection);
            Collection<DBMcCustomers> attachedDBMcCustomersCollection = new ArrayList<DBMcCustomers>();
            for (DBMcCustomers DBMcCustomersCollectionDBMcCustomersToAttach : DBMcAvailabilities.getDBMcCustomersCollection()) {
                DBMcCustomersCollectionDBMcCustomersToAttach = em.getReference(DBMcCustomersCollectionDBMcCustomersToAttach.getClass(), DBMcCustomersCollectionDBMcCustomersToAttach.getCustomerId());
                attachedDBMcCustomersCollection.add(DBMcCustomersCollectionDBMcCustomersToAttach);
            }
            DBMcAvailabilities.setDBMcCustomersCollection(attachedDBMcCustomersCollection);
            em.persist(DBMcAvailabilities);
            if (releaseId != null) {
                releaseId.getDBMcAvailabilitiesCollection().add(DBMcAvailabilities);
                releaseId = em.merge(releaseId);
            }
            for (DBMcDependencies DBMcDependenciesCollectionDBMcDependencies : DBMcAvailabilities.getDBMcDependenciesCollection()) {
                DBMcAvailabilities oldAvailabilityIdOfDBMcDependenciesCollectionDBMcDependencies = DBMcDependenciesCollectionDBMcDependencies.getAvailabilityId();
                DBMcDependenciesCollectionDBMcDependencies.setAvailabilityId(DBMcAvailabilities);
                DBMcDependenciesCollectionDBMcDependencies = em.merge(DBMcDependenciesCollectionDBMcDependencies);
                if (oldAvailabilityIdOfDBMcDependenciesCollectionDBMcDependencies != null) {
                    oldAvailabilityIdOfDBMcDependenciesCollectionDBMcDependencies.getDBMcDependenciesCollection().remove(DBMcDependenciesCollectionDBMcDependencies);
                    oldAvailabilityIdOfDBMcDependenciesCollectionDBMcDependencies = em.merge(oldAvailabilityIdOfDBMcDependenciesCollectionDBMcDependencies);
                }
            }
            for (DBMcCustomers DBMcCustomersCollectionDBMcCustomers : DBMcAvailabilities.getDBMcCustomersCollection()) {
                DBMcAvailabilities oldAvailabilityIdOfDBMcCustomersCollectionDBMcCustomers = DBMcCustomersCollectionDBMcCustomers.getAvailabilityId();
                DBMcCustomersCollectionDBMcCustomers.setAvailabilityId(DBMcAvailabilities);
                DBMcCustomersCollectionDBMcCustomers = em.merge(DBMcCustomersCollectionDBMcCustomers);
                if (oldAvailabilityIdOfDBMcCustomersCollectionDBMcCustomers != null) {
                    oldAvailabilityIdOfDBMcCustomersCollectionDBMcCustomers.getDBMcCustomersCollection().remove(DBMcCustomersCollectionDBMcCustomers);
                    oldAvailabilityIdOfDBMcCustomersCollectionDBMcCustomers = em.merge(oldAvailabilityIdOfDBMcCustomersCollectionDBMcCustomers);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DBMcAvailabilities DBMcAvailabilities) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcAvailabilities persistentDBMcAvailabilities = em.find(DBMcAvailabilities.class, DBMcAvailabilities.getAvailabilityId());
            DBMcReleases releaseIdOld = persistentDBMcAvailabilities.getReleaseId();
            DBMcReleases releaseIdNew = DBMcAvailabilities.getReleaseId();
            Collection<DBMcDependencies> DBMcDependenciesCollectionOld = persistentDBMcAvailabilities.getDBMcDependenciesCollection();
            Collection<DBMcDependencies> DBMcDependenciesCollectionNew = DBMcAvailabilities.getDBMcDependenciesCollection();
            Collection<DBMcCustomers> DBMcCustomersCollectionOld = persistentDBMcAvailabilities.getDBMcCustomersCollection();
            Collection<DBMcCustomers> DBMcCustomersCollectionNew = DBMcAvailabilities.getDBMcCustomersCollection();
            if (releaseIdNew != null) {
                releaseIdNew = em.getReference(releaseIdNew.getClass(), releaseIdNew.getReleaseId());
                DBMcAvailabilities.setReleaseId(releaseIdNew);
            }
            Collection<DBMcDependencies> attachedDBMcDependenciesCollectionNew = new ArrayList<DBMcDependencies>();
            for (DBMcDependencies DBMcDependenciesCollectionNewDBMcDependenciesToAttach : DBMcDependenciesCollectionNew) {
                DBMcDependenciesCollectionNewDBMcDependenciesToAttach = em.getReference(DBMcDependenciesCollectionNewDBMcDependenciesToAttach.getClass(), DBMcDependenciesCollectionNewDBMcDependenciesToAttach.getDependencyId());
                attachedDBMcDependenciesCollectionNew.add(DBMcDependenciesCollectionNewDBMcDependenciesToAttach);
            }
            DBMcDependenciesCollectionNew = attachedDBMcDependenciesCollectionNew;
            DBMcAvailabilities.setDBMcDependenciesCollection(DBMcDependenciesCollectionNew);
            Collection<DBMcCustomers> attachedDBMcCustomersCollectionNew = new ArrayList<DBMcCustomers>();
            for (DBMcCustomers DBMcCustomersCollectionNewDBMcCustomersToAttach : DBMcCustomersCollectionNew) {
                DBMcCustomersCollectionNewDBMcCustomersToAttach = em.getReference(DBMcCustomersCollectionNewDBMcCustomersToAttach.getClass(), DBMcCustomersCollectionNewDBMcCustomersToAttach.getCustomerId());
                attachedDBMcCustomersCollectionNew.add(DBMcCustomersCollectionNewDBMcCustomersToAttach);
            }
            DBMcCustomersCollectionNew = attachedDBMcCustomersCollectionNew;
            DBMcAvailabilities.setDBMcCustomersCollection(DBMcCustomersCollectionNew);
            DBMcAvailabilities = em.merge(DBMcAvailabilities);
            if (releaseIdOld != null && !releaseIdOld.equals(releaseIdNew)) {
                releaseIdOld.getDBMcAvailabilitiesCollection().remove(DBMcAvailabilities);
                releaseIdOld = em.merge(releaseIdOld);
            }
            if (releaseIdNew != null && !releaseIdNew.equals(releaseIdOld)) {
                releaseIdNew.getDBMcAvailabilitiesCollection().add(DBMcAvailabilities);
                releaseIdNew = em.merge(releaseIdNew);
            }
            for (DBMcDependencies DBMcDependenciesCollectionOldDBMcDependencies : DBMcDependenciesCollectionOld) {
                if (!DBMcDependenciesCollectionNew.contains(DBMcDependenciesCollectionOldDBMcDependencies)) {
                    DBMcDependenciesCollectionOldDBMcDependencies.setAvailabilityId(null);
                    DBMcDependenciesCollectionOldDBMcDependencies = em.merge(DBMcDependenciesCollectionOldDBMcDependencies);
                }
            }
            for (DBMcDependencies DBMcDependenciesCollectionNewDBMcDependencies : DBMcDependenciesCollectionNew) {
                if (!DBMcDependenciesCollectionOld.contains(DBMcDependenciesCollectionNewDBMcDependencies)) {
                    DBMcAvailabilities oldAvailabilityIdOfDBMcDependenciesCollectionNewDBMcDependencies = DBMcDependenciesCollectionNewDBMcDependencies.getAvailabilityId();
                    DBMcDependenciesCollectionNewDBMcDependencies.setAvailabilityId(DBMcAvailabilities);
                    DBMcDependenciesCollectionNewDBMcDependencies = em.merge(DBMcDependenciesCollectionNewDBMcDependencies);
                    if (oldAvailabilityIdOfDBMcDependenciesCollectionNewDBMcDependencies != null && !oldAvailabilityIdOfDBMcDependenciesCollectionNewDBMcDependencies.equals(DBMcAvailabilities)) {
                        oldAvailabilityIdOfDBMcDependenciesCollectionNewDBMcDependencies.getDBMcDependenciesCollection().remove(DBMcDependenciesCollectionNewDBMcDependencies);
                        oldAvailabilityIdOfDBMcDependenciesCollectionNewDBMcDependencies = em.merge(oldAvailabilityIdOfDBMcDependenciesCollectionNewDBMcDependencies);
                    }
                }
            }
            for (DBMcCustomers DBMcCustomersCollectionOldDBMcCustomers : DBMcCustomersCollectionOld) {
                if (!DBMcCustomersCollectionNew.contains(DBMcCustomersCollectionOldDBMcCustomers)) {
                    DBMcCustomersCollectionOldDBMcCustomers.setAvailabilityId(null);
                    DBMcCustomersCollectionOldDBMcCustomers = em.merge(DBMcCustomersCollectionOldDBMcCustomers);
                }
            }
            for (DBMcCustomers DBMcCustomersCollectionNewDBMcCustomers : DBMcCustomersCollectionNew) {
                if (!DBMcCustomersCollectionOld.contains(DBMcCustomersCollectionNewDBMcCustomers)) {
                    DBMcAvailabilities oldAvailabilityIdOfDBMcCustomersCollectionNewDBMcCustomers = DBMcCustomersCollectionNewDBMcCustomers.getAvailabilityId();
                    DBMcCustomersCollectionNewDBMcCustomers.setAvailabilityId(DBMcAvailabilities);
                    DBMcCustomersCollectionNewDBMcCustomers = em.merge(DBMcCustomersCollectionNewDBMcCustomers);
                    if (oldAvailabilityIdOfDBMcCustomersCollectionNewDBMcCustomers != null && !oldAvailabilityIdOfDBMcCustomersCollectionNewDBMcCustomers.equals(DBMcAvailabilities)) {
                        oldAvailabilityIdOfDBMcCustomersCollectionNewDBMcCustomers.getDBMcCustomersCollection().remove(DBMcCustomersCollectionNewDBMcCustomers);
                        oldAvailabilityIdOfDBMcCustomersCollectionNewDBMcCustomers = em.merge(oldAvailabilityIdOfDBMcCustomersCollectionNewDBMcCustomers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = DBMcAvailabilities.getAvailabilityId();
                if (findDBMcAvailabilities(id) == null) {
                    throw new NonexistentEntityException("The dBMcAvailabilities with id " + id + " no longer exists.");
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
            DBMcAvailabilities DBMcAvailabilities;
            try {
                DBMcAvailabilities = em.getReference(DBMcAvailabilities.class, id);
                DBMcAvailabilities.getAvailabilityId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The DBMcAvailabilities with id " + id + " no longer exists.", enfe);
            }
            DBMcReleases releaseId = DBMcAvailabilities.getReleaseId();
            if (releaseId != null) {
                releaseId.getDBMcAvailabilitiesCollection().remove(DBMcAvailabilities);
                releaseId = em.merge(releaseId);
            }
            Collection<DBMcDependencies> DBMcDependenciesCollection = DBMcAvailabilities.getDBMcDependenciesCollection();
            for (DBMcDependencies DBMcDependenciesCollectionDBMcDependencies : DBMcDependenciesCollection) {
                DBMcDependenciesCollectionDBMcDependencies.setAvailabilityId(null);
                DBMcDependenciesCollectionDBMcDependencies = em.merge(DBMcDependenciesCollectionDBMcDependencies);
            }
            Collection<DBMcCustomers> DBMcCustomersCollection = DBMcAvailabilities.getDBMcCustomersCollection();
            for (DBMcCustomers DBMcCustomersCollectionDBMcCustomers : DBMcCustomersCollection) {
                DBMcCustomersCollectionDBMcCustomers.setAvailabilityId(null);
                DBMcCustomersCollectionDBMcCustomers = em.merge(DBMcCustomersCollectionDBMcCustomers);
            }
            em.remove(DBMcAvailabilities);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DBMcAvailabilities> findDBMcAvailabilitiesEntities() {
        return findDBMcAvailabilitiesEntities(true, -1, -1);
    }

    public List<DBMcAvailabilities> findDBMcAvailabilitiesEntities(int maxResults, int firstResult) {
        return findDBMcAvailabilitiesEntities(false, maxResults, firstResult);
    }

    private List<DBMcAvailabilities> findDBMcAvailabilitiesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DBMcAvailabilities.class));
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

    public DBMcAvailabilities findDBMcAvailabilities(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DBMcAvailabilities.class, id);
        } finally {
            em.close();
        }
    }

    public int getDBMcAvailabilitiesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DBMcAvailabilities> rt = cq.from(DBMcAvailabilities.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
