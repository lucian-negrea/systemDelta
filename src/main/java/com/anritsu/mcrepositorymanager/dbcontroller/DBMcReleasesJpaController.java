/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.dbcontroller;

import com.anritsu.mcrepositorymanager.dbbeans.DBMcActivities;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcAvailabilities;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcItemsReleased;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcReleases;
import com.anritsu.mcrepositorymanager.dbcontroller.exceptions.IllegalOrphanException;
import com.anritsu.mcrepositorymanager.dbcontroller.exceptions.NonexistentEntityException;
import com.anritsu.mcrepositorymanager.packageinfoparser.PackageInfoParserFactory;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ro100051
 */
public class DBMcReleasesJpaController implements Serializable {
private static final Logger LOGGER = Logger.getLogger(PackageInfoParserFactory.class.getName());
    public DBMcReleasesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DBMcReleases DBMcReleases) {
        if (DBMcReleases.getDBMcActivitiesCollection() == null) {
            DBMcReleases.setDBMcActivitiesCollection(new ArrayList<DBMcActivities>());
        }
        if (DBMcReleases.getDBMcItemsReleasedCollection() == null) {
            DBMcReleases.setDBMcItemsReleasedCollection(new ArrayList<DBMcItemsReleased>());
        }
        if (DBMcReleases.getDBMcAvailabilitiesCollection() == null) {
            DBMcReleases.setDBMcAvailabilitiesCollection(new ArrayList<DBMcAvailabilities>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DBMcActivities> attachedDBMcActivitiesCollection = new ArrayList<DBMcActivities>();
            for (DBMcActivities DBMcActivitiesCollectionDBMcActivitiesToAttach : DBMcReleases.getDBMcActivitiesCollection()) {
                DBMcActivitiesCollectionDBMcActivitiesToAttach = em.getReference(DBMcActivitiesCollectionDBMcActivitiesToAttach.getClass(), DBMcActivitiesCollectionDBMcActivitiesToAttach.getActivityId());
                attachedDBMcActivitiesCollection.add(DBMcActivitiesCollectionDBMcActivitiesToAttach);
            }
            DBMcReleases.setDBMcActivitiesCollection(attachedDBMcActivitiesCollection);
            Collection<DBMcItemsReleased> attachedDBMcItemsReleasedCollection = new ArrayList<DBMcItemsReleased>();
            for (DBMcItemsReleased DBMcItemsReleasedCollectionDBMcItemsReleasedToAttach : DBMcReleases.getDBMcItemsReleasedCollection()) {
                DBMcItemsReleasedCollectionDBMcItemsReleasedToAttach = em.getReference(DBMcItemsReleasedCollectionDBMcItemsReleasedToAttach.getClass(), DBMcItemsReleasedCollectionDBMcItemsReleasedToAttach.getItemId());
                attachedDBMcItemsReleasedCollection.add(DBMcItemsReleasedCollectionDBMcItemsReleasedToAttach);
            }
            DBMcReleases.setDBMcItemsReleasedCollection(attachedDBMcItemsReleasedCollection);
            Collection<DBMcAvailabilities> attachedDBMcAvailabilitiesCollection = new ArrayList<DBMcAvailabilities>();
            for (DBMcAvailabilities DBMcAvailabilitiesCollectionDBMcAvailabilitiesToAttach : DBMcReleases.getDBMcAvailabilitiesCollection()) {
                DBMcAvailabilitiesCollectionDBMcAvailabilitiesToAttach = em.getReference(DBMcAvailabilitiesCollectionDBMcAvailabilitiesToAttach.getClass(), DBMcAvailabilitiesCollectionDBMcAvailabilitiesToAttach.getAvailabilityId());
                attachedDBMcAvailabilitiesCollection.add(DBMcAvailabilitiesCollectionDBMcAvailabilitiesToAttach);
            }
            DBMcReleases.setDBMcAvailabilitiesCollection(attachedDBMcAvailabilitiesCollection);
            em.persist(DBMcReleases);
            for (DBMcActivities DBMcActivitiesCollectionDBMcActivities : DBMcReleases.getDBMcActivitiesCollection()) {
                DBMcReleases oldReleaseIdOfDBMcActivitiesCollectionDBMcActivities = DBMcActivitiesCollectionDBMcActivities.getReleaseId();
                DBMcActivitiesCollectionDBMcActivities.setReleaseId(DBMcReleases);
                DBMcActivitiesCollectionDBMcActivities = em.merge(DBMcActivitiesCollectionDBMcActivities);
                if (oldReleaseIdOfDBMcActivitiesCollectionDBMcActivities != null) {
                    oldReleaseIdOfDBMcActivitiesCollectionDBMcActivities.getDBMcActivitiesCollection().remove(DBMcActivitiesCollectionDBMcActivities);
                    oldReleaseIdOfDBMcActivitiesCollectionDBMcActivities = em.merge(oldReleaseIdOfDBMcActivitiesCollectionDBMcActivities);
                }
            }
            for (DBMcItemsReleased DBMcItemsReleasedCollectionDBMcItemsReleased : DBMcReleases.getDBMcItemsReleasedCollection()) {
                DBMcReleases oldReleaseIdOfDBMcItemsReleasedCollectionDBMcItemsReleased = DBMcItemsReleasedCollectionDBMcItemsReleased.getReleaseId();
                DBMcItemsReleasedCollectionDBMcItemsReleased.setReleaseId(DBMcReleases);
                DBMcItemsReleasedCollectionDBMcItemsReleased = em.merge(DBMcItemsReleasedCollectionDBMcItemsReleased);
                if (oldReleaseIdOfDBMcItemsReleasedCollectionDBMcItemsReleased != null) {
                    oldReleaseIdOfDBMcItemsReleasedCollectionDBMcItemsReleased.getDBMcItemsReleasedCollection().remove(DBMcItemsReleasedCollectionDBMcItemsReleased);
                    oldReleaseIdOfDBMcItemsReleasedCollectionDBMcItemsReleased = em.merge(oldReleaseIdOfDBMcItemsReleasedCollectionDBMcItemsReleased);
                }
            }
            for (DBMcAvailabilities DBMcAvailabilitiesCollectionDBMcAvailabilities : DBMcReleases.getDBMcAvailabilitiesCollection()) {
                DBMcReleases oldReleaseIdOfDBMcAvailabilitiesCollectionDBMcAvailabilities = DBMcAvailabilitiesCollectionDBMcAvailabilities.getReleaseId();
                DBMcAvailabilitiesCollectionDBMcAvailabilities.setReleaseId(DBMcReleases);
                DBMcAvailabilitiesCollectionDBMcAvailabilities = em.merge(DBMcAvailabilitiesCollectionDBMcAvailabilities);
                if (oldReleaseIdOfDBMcAvailabilitiesCollectionDBMcAvailabilities != null) {
                    oldReleaseIdOfDBMcAvailabilitiesCollectionDBMcAvailabilities.getDBMcAvailabilitiesCollection().remove(DBMcAvailabilitiesCollectionDBMcAvailabilities);
                    oldReleaseIdOfDBMcAvailabilitiesCollectionDBMcAvailabilities = em.merge(oldReleaseIdOfDBMcAvailabilitiesCollectionDBMcAvailabilities);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DBMcReleases DBMcReleases) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcReleases persistentDBMcReleases = em.find(DBMcReleases.class, DBMcReleases.getReleaseId());
            Collection<DBMcActivities> DBMcActivitiesCollectionOld = persistentDBMcReleases.getDBMcActivitiesCollection();
            Collection<DBMcActivities> DBMcActivitiesCollectionNew = DBMcReleases.getDBMcActivitiesCollection();
            Collection<DBMcItemsReleased> DBMcItemsReleasedCollectionOld = persistentDBMcReleases.getDBMcItemsReleasedCollection();
            Collection<DBMcItemsReleased> DBMcItemsReleasedCollectionNew = DBMcReleases.getDBMcItemsReleasedCollection();
            Collection<DBMcAvailabilities> DBMcAvailabilitiesCollectionOld = persistentDBMcReleases.getDBMcAvailabilitiesCollection();
            Collection<DBMcAvailabilities> DBMcAvailabilitiesCollectionNew = DBMcReleases.getDBMcAvailabilitiesCollection();
            List<String> illegalOrphanMessages = null;
            for (DBMcActivities DBMcActivitiesCollectionOldDBMcActivities : DBMcActivitiesCollectionOld) {
                if (!DBMcActivitiesCollectionNew.contains(DBMcActivitiesCollectionOldDBMcActivities)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DBMcActivities " + DBMcActivitiesCollectionOldDBMcActivities + " since its releaseId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DBMcActivities> attachedDBMcActivitiesCollectionNew = new ArrayList<DBMcActivities>();
            for (DBMcActivities DBMcActivitiesCollectionNewDBMcActivitiesToAttach : DBMcActivitiesCollectionNew) {
                DBMcActivitiesCollectionNewDBMcActivitiesToAttach = em.getReference(DBMcActivitiesCollectionNewDBMcActivitiesToAttach.getClass(), DBMcActivitiesCollectionNewDBMcActivitiesToAttach.getActivityId());
                attachedDBMcActivitiesCollectionNew.add(DBMcActivitiesCollectionNewDBMcActivitiesToAttach);
            }
            DBMcActivitiesCollectionNew = attachedDBMcActivitiesCollectionNew;
            DBMcReleases.setDBMcActivitiesCollection(DBMcActivitiesCollectionNew);
            Collection<DBMcItemsReleased> attachedDBMcItemsReleasedCollectionNew = new ArrayList<DBMcItemsReleased>();
            for (DBMcItemsReleased DBMcItemsReleasedCollectionNewDBMcItemsReleasedToAttach : DBMcItemsReleasedCollectionNew) {
                DBMcItemsReleasedCollectionNewDBMcItemsReleasedToAttach = em.getReference(DBMcItemsReleasedCollectionNewDBMcItemsReleasedToAttach.getClass(), DBMcItemsReleasedCollectionNewDBMcItemsReleasedToAttach.getItemId());
                attachedDBMcItemsReleasedCollectionNew.add(DBMcItemsReleasedCollectionNewDBMcItemsReleasedToAttach);
            }
            DBMcItemsReleasedCollectionNew = attachedDBMcItemsReleasedCollectionNew;
            DBMcReleases.setDBMcItemsReleasedCollection(DBMcItemsReleasedCollectionNew);
            Collection<DBMcAvailabilities> attachedDBMcAvailabilitiesCollectionNew = new ArrayList<DBMcAvailabilities>();
            for (DBMcAvailabilities DBMcAvailabilitiesCollectionNewDBMcAvailabilitiesToAttach : DBMcAvailabilitiesCollectionNew) {
                DBMcAvailabilitiesCollectionNewDBMcAvailabilitiesToAttach = em.getReference(DBMcAvailabilitiesCollectionNewDBMcAvailabilitiesToAttach.getClass(), DBMcAvailabilitiesCollectionNewDBMcAvailabilitiesToAttach.getAvailabilityId());
                attachedDBMcAvailabilitiesCollectionNew.add(DBMcAvailabilitiesCollectionNewDBMcAvailabilitiesToAttach);
            }
            DBMcAvailabilitiesCollectionNew = attachedDBMcAvailabilitiesCollectionNew;
            DBMcReleases.setDBMcAvailabilitiesCollection(DBMcAvailabilitiesCollectionNew);
            DBMcReleases = em.merge(DBMcReleases);
            for (DBMcActivities DBMcActivitiesCollectionNewDBMcActivities : DBMcActivitiesCollectionNew) {
                if (!DBMcActivitiesCollectionOld.contains(DBMcActivitiesCollectionNewDBMcActivities)) {
                    DBMcReleases oldReleaseIdOfDBMcActivitiesCollectionNewDBMcActivities = DBMcActivitiesCollectionNewDBMcActivities.getReleaseId();
                    DBMcActivitiesCollectionNewDBMcActivities.setReleaseId(DBMcReleases);
                    DBMcActivitiesCollectionNewDBMcActivities = em.merge(DBMcActivitiesCollectionNewDBMcActivities);
                    if (oldReleaseIdOfDBMcActivitiesCollectionNewDBMcActivities != null && !oldReleaseIdOfDBMcActivitiesCollectionNewDBMcActivities.equals(DBMcReleases)) {
                        oldReleaseIdOfDBMcActivitiesCollectionNewDBMcActivities.getDBMcActivitiesCollection().remove(DBMcActivitiesCollectionNewDBMcActivities);
                        oldReleaseIdOfDBMcActivitiesCollectionNewDBMcActivities = em.merge(oldReleaseIdOfDBMcActivitiesCollectionNewDBMcActivities);
                    }
                }
            }
            for (DBMcItemsReleased DBMcItemsReleasedCollectionOldDBMcItemsReleased : DBMcItemsReleasedCollectionOld) {
                if (!DBMcItemsReleasedCollectionNew.contains(DBMcItemsReleasedCollectionOldDBMcItemsReleased)) {
                    DBMcItemsReleasedCollectionOldDBMcItemsReleased.setReleaseId(null);
                    DBMcItemsReleasedCollectionOldDBMcItemsReleased = em.merge(DBMcItemsReleasedCollectionOldDBMcItemsReleased);
                }
            }
            for (DBMcItemsReleased DBMcItemsReleasedCollectionNewDBMcItemsReleased : DBMcItemsReleasedCollectionNew) {
                if (!DBMcItemsReleasedCollectionOld.contains(DBMcItemsReleasedCollectionNewDBMcItemsReleased)) {
                    DBMcReleases oldReleaseIdOfDBMcItemsReleasedCollectionNewDBMcItemsReleased = DBMcItemsReleasedCollectionNewDBMcItemsReleased.getReleaseId();
                    DBMcItemsReleasedCollectionNewDBMcItemsReleased.setReleaseId(DBMcReleases);
                    DBMcItemsReleasedCollectionNewDBMcItemsReleased = em.merge(DBMcItemsReleasedCollectionNewDBMcItemsReleased);
                    if (oldReleaseIdOfDBMcItemsReleasedCollectionNewDBMcItemsReleased != null && !oldReleaseIdOfDBMcItemsReleasedCollectionNewDBMcItemsReleased.equals(DBMcReleases)) {
                        oldReleaseIdOfDBMcItemsReleasedCollectionNewDBMcItemsReleased.getDBMcItemsReleasedCollection().remove(DBMcItemsReleasedCollectionNewDBMcItemsReleased);
                        oldReleaseIdOfDBMcItemsReleasedCollectionNewDBMcItemsReleased = em.merge(oldReleaseIdOfDBMcItemsReleasedCollectionNewDBMcItemsReleased);
                    }
                }
            }
            for (DBMcAvailabilities DBMcAvailabilitiesCollectionOldDBMcAvailabilities : DBMcAvailabilitiesCollectionOld) {
                if (!DBMcAvailabilitiesCollectionNew.contains(DBMcAvailabilitiesCollectionOldDBMcAvailabilities)) {
                    DBMcAvailabilitiesCollectionOldDBMcAvailabilities.setReleaseId(null);
                    DBMcAvailabilitiesCollectionOldDBMcAvailabilities = em.merge(DBMcAvailabilitiesCollectionOldDBMcAvailabilities);
                }
            }
            for (DBMcAvailabilities DBMcAvailabilitiesCollectionNewDBMcAvailabilities : DBMcAvailabilitiesCollectionNew) {
                if (!DBMcAvailabilitiesCollectionOld.contains(DBMcAvailabilitiesCollectionNewDBMcAvailabilities)) {
                    DBMcReleases oldReleaseIdOfDBMcAvailabilitiesCollectionNewDBMcAvailabilities = DBMcAvailabilitiesCollectionNewDBMcAvailabilities.getReleaseId();
                    DBMcAvailabilitiesCollectionNewDBMcAvailabilities.setReleaseId(DBMcReleases);
                    DBMcAvailabilitiesCollectionNewDBMcAvailabilities = em.merge(DBMcAvailabilitiesCollectionNewDBMcAvailabilities);
                    if (oldReleaseIdOfDBMcAvailabilitiesCollectionNewDBMcAvailabilities != null && !oldReleaseIdOfDBMcAvailabilitiesCollectionNewDBMcAvailabilities.equals(DBMcReleases)) {
                        oldReleaseIdOfDBMcAvailabilitiesCollectionNewDBMcAvailabilities.getDBMcAvailabilitiesCollection().remove(DBMcAvailabilitiesCollectionNewDBMcAvailabilities);
                        oldReleaseIdOfDBMcAvailabilitiesCollectionNewDBMcAvailabilities = em.merge(oldReleaseIdOfDBMcAvailabilitiesCollectionNewDBMcAvailabilities);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = DBMcReleases.getReleaseId();
                if (findDBMcReleases(id) == null) {
                    throw new NonexistentEntityException("The dBMcReleases with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DBMcReleases DBMcReleases;
            try {
                DBMcReleases = em.getReference(DBMcReleases.class, id);
                DBMcReleases.getReleaseId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The DBMcReleases with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DBMcActivities> DBMcActivitiesCollectionOrphanCheck = DBMcReleases.getDBMcActivitiesCollection();
            for (DBMcActivities DBMcActivitiesCollectionOrphanCheckDBMcActivities : DBMcActivitiesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DBMcReleases (" + DBMcReleases + ") cannot be destroyed since the DBMcActivities " + DBMcActivitiesCollectionOrphanCheckDBMcActivities + " in its DBMcActivitiesCollection field has a non-nullable releaseId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DBMcItemsReleased> DBMcItemsReleasedCollection = DBMcReleases.getDBMcItemsReleasedCollection();
            for (DBMcItemsReleased DBMcItemsReleasedCollectionDBMcItemsReleased : DBMcItemsReleasedCollection) {
                DBMcItemsReleasedCollectionDBMcItemsReleased.setReleaseId(null);
                DBMcItemsReleasedCollectionDBMcItemsReleased = em.merge(DBMcItemsReleasedCollectionDBMcItemsReleased);
            }
            Collection<DBMcAvailabilities> DBMcAvailabilitiesCollection = DBMcReleases.getDBMcAvailabilitiesCollection();
            for (DBMcAvailabilities DBMcAvailabilitiesCollectionDBMcAvailabilities : DBMcAvailabilitiesCollection) {
                DBMcAvailabilitiesCollectionDBMcAvailabilities.setReleaseId(null);
                DBMcAvailabilitiesCollectionDBMcAvailabilities = em.merge(DBMcAvailabilitiesCollectionDBMcAvailabilities);
            }
            em.remove(DBMcReleases);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DBMcReleases> findDBMcReleasesEntities() {
        return findDBMcReleasesEntities(true, -1, -1);
    }

    public List<DBMcReleases> findDBMcReleasesEntities(int maxResults, int firstResult) {
        return findDBMcReleasesEntities(false, maxResults, firstResult);
    }

    private List<DBMcReleases> findDBMcReleasesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DBMcReleases.class));
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
    
    public List<DBMcReleases> findDBMcReleasesByName(String name){        
        Query query = getEntityManager().createNamedQuery("DBMcReleases.findByComponentName");
        query.setParameter("componentName", name);
        List<DBMcReleases> dbMcReleases = (List<DBMcReleases>) query.getResultList();
        return dbMcReleases;        
    }
    
    public DBMcReleases findDBMcReleasesByNameVersion(String name, String version){
        Query query = getEntityManager().createNamedQuery("DBMcReleases.findByComponentNameComponentVersion");
        query.setParameter("componentName", name);
        query.setParameter("componentVersion", version);
        LOGGER.log(Level.INFO, "Executing: " + query.toString());
        return (DBMcReleases) query.getSingleResult();
        
    }

    public DBMcReleases findDBMcReleases(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DBMcReleases.class, id);
        } finally {
            em.close();
        }
    }

    public int getDBMcReleasesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DBMcReleases> rt = cq.from(DBMcReleases.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
