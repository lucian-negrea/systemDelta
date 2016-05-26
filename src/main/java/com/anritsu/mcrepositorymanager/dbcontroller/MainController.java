/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.dbcontroller;

import com.anritsu.mcrepositorymanager.dbbeans.DBMcAvailabilities;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcCustomers;
import com.anritsu.mcrepositorymanager.dbbeans.DBMcReleases;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ro100051
 */
public class MainController {
    
    
    private static MainController singleton;
    EntityManagerFactory emf;

    private DBMcReleasesJpaController dbMcReleasesJpaController;
    private DBMcAvailabilitiesJpaController dbMcAvailabilitiesJpaController;
    private DBMcActivitiesJpaController dbMcActivitiesJpaController; 
    private DBMcCustomersJpaController dbMcCustomersJpaController;
    private DBMcDependenciesJpaController dbMcDependenciesJpaController;
    private DBMcItemsReleasedJpaController dBMcItemsReleasedJpaController;

    private MainController() {
        try {
            emf = Persistence.createEntityManagerFactory("MCRG_PU");
            dbMcReleasesJpaController = new DBMcReleasesJpaController(emf);
            dbMcAvailabilitiesJpaController = new DBMcAvailabilitiesJpaController(emf);
            dbMcActivitiesJpaController = new DBMcActivitiesJpaController(emf);
            dbMcCustomersJpaController = new DBMcCustomersJpaController(emf);
            dbMcDependenciesJpaController = new DBMcDependenciesJpaController(emf);
            dBMcItemsReleasedJpaController = new DBMcItemsReleasedJpaController(emf);
            
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static MainController getInstance() {
        if (singleton == null) {
            singleton = new MainController();
        }
        return singleton;
    }

    public HashSet<DBMcReleases> getPackages(){
       HashSet<DBMcReleases> mcReleases = new HashSet<>();
        for(DBMcReleases dbmcr: dbMcReleasesJpaController.findDBMcReleasesEntities()){
            mcReleases.add(dbmcr);
        }
        return mcReleases;
    }
    public HashSet<String> getAvailabilities() {
        HashSet<String> availabilities = new HashSet<>();
        for(DBMcAvailabilities dbmca: dbMcAvailabilitiesJpaController.findDBMcAvailabilitiesEntities()){
            availabilities.add(dbmca.getAvailability());
        }
        return availabilities;
    }
    
    public List<DBMcReleases> getDBMcReleasesByPackageName(String name){
        return dbMcReleasesJpaController.findDBMcReleasesByName(name);
        
    }

    public TreeSet<String> getCustomers() {
        TreeSet<String> customers = new TreeSet();
        for (DBMcCustomers dbmcc : dbMcCustomersJpaController.findDBMcCustomersEntities()) {
            customers.add(dbmcc.getCustomerName());
        }
        return customers;
    }
    
    public TreeSet<String> getPackagesName(){
        TreeSet<String> packagesName = new TreeSet<>();
        for(DBMcReleases dbmcr : dbMcReleasesJpaController.findDBMcReleasesEntities()){
            packagesName.add(dbmcr.getComponentName());
        }
        return packagesName;
    }

    public ArrayList<String> getMCVersions() {
        ArrayList<String> mcAvailabilities = new ArrayList<>();
        List<DBMcAvailabilities> dbMcAvailabilities = dbMcAvailabilitiesJpaController.findDBMcAvailabilitiesEntities();
        for(DBMcAvailabilities dbmca: dbMcAvailabilities){
            mcAvailabilities.add(dbmca.getMcVersion());
        }
        return mcAvailabilities;
    }
    
    public DBMcReleases getDBMcReleasesByNameVersion(String name, String version){
        return dbMcReleasesJpaController.findDBMcReleasesByNameVersion(name, version);
    }
}
