/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RO100051
 */
public class Configuration {
    // Default values to be used if no config file is found
    private static Configuration configuration;
    private static String dbUser = "mclawpkginfo_test";
    private static String dbPassword = "mclawpkginfo_test";
    private static String dbIP = "localhost";
    private static String dbName = "mclawpkginfo_test";
    private static String rssFilesPath = "rss";
    private static String mcPackagesPath = "mcPackages";
    private static String generatedRepositoriesPath = "generatedRepositories";
    private static String releaseManagementPassword = "admin";
    private static String supportedVersions = "7.0.2,7.1,8.0";
    private static String sourceXLS = "7.0.2";
    private static String sourceDB = "7.1,8.0";
    private static String rssTemplatePath = "rss/";
    private Properties prop = new Properties();
    

    private Configuration() {
        InputStream input = null;
        String fileName = "mcRepositoryManager.properties";
        File configFile = new File(fileName);
        if (configFile.exists() && !configFile.isDirectory()) {
            try {
                input = new FileInputStream(configFile);
                if (input == null) {
                    System.out.println("Sorry, unable to find " + configFile.getAbsolutePath());
                }
                prop.load(input);
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            System.out.println("Sorry, unable to find " + configFile.getAbsolutePath());
        }

    }

    public static Configuration getInstance() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }
    
    public String getDbUser() {
        try {
            if(prop.getProperty("dbUser")!=null){
                dbUser = prop.getProperty("dbUser");
            }
        } catch (Exception exp) {
            System.out.println("Returning default value for dbUser: " + dbUser);
        }
        System.out.println("Returning: " + dbUser);
        return dbUser;
    }

    public String getDbPassword() {
        try {
             if(prop.getProperty("dbPassword")!=null){
                 dbPassword = prop.getProperty("dbPassword");
            }
           
        } catch (Exception exp) {
            System.out.println("Returning default value for dbPassword: " + dbPassword);
        }
        System.out.println("Returning: " + dbPassword);
        return dbPassword;
    }

    public String getDbIP() {
        try {
            if(prop.getProperty("dbIP")!=null){
                 dbIP = prop.getProperty("dbIP");
            }
        } catch (Exception exp) {
            System.out.println("Returning default value for dbIP: " + dbIP);
        }
        System.out.println("Returning: " + dbIP);
        return dbIP;
    }

    public String getDbName() {
        try {
            if(prop.getProperty("dbName")!=null){
                 dbName = prop.getProperty("dbName");
            }
        } catch (Exception exp) {
            System.out.println("Returning default value for dbName:" + dbName);
        }
        System.out.println("Returning: " + dbName);
        return dbName;
    }

    public String getRssFilesPath() {
        try {
             if(prop.getProperty("savePath")!=null){
                 rssFilesPath = prop.getProperty("savePath");
            }
        } catch (Exception exp) {
            System.out.println("Returning default value for savePath: " + rssFilesPath);
        }
        System.out.println("Returning: " + rssFilesPath);
        return rssFilesPath;
    }
    
    public String getReleaseManagementPassword(){
        try{
            if(prop.getProperty("releaseManagementPassword")!=null){
                releaseManagementPassword = prop.getProperty("releaseManagementPassword");
            }
        }catch(Exception exp){
            System.out.println("Returning default value for releaseManagementPassword: " + releaseManagementPassword);
        }
        System.out.println("Returning: " + releaseManagementPassword);
        return releaseManagementPassword;
        
    }
    
    public String getSupportedVersions(){
        try{
            if(prop.getProperty("supportedVersions")!=null){
                supportedVersions = prop.getProperty("supportedVersions");
            }
        }catch(Exception exp){
            System.out.println("Returning default value for supportedVersions: " + supportedVersions);
        }
        System.out.println("Returning: " + supportedVersions);
        return supportedVersions;
        
    }
    
     public String getMcPackagesPath(){
        try{
            if(prop.getProperty("mcPackagesPath")!=null){
                mcPackagesPath = prop.getProperty("mcPackagesPath");
            }
        }catch(Exception exp){
            System.out.println("Returning default value for mcPackagesPath: " + mcPackagesPath);
        }
        System.out.println("Returning: " + mcPackagesPath);
        return mcPackagesPath;
        
    }
     
     public String getGeneratedRepositoriesPath(){
        try{
            if(prop.getProperty("generatedRepositoriesPath")!=null){
                generatedRepositoriesPath = prop.getProperty("generatedRepositoriesPath");
            }
        }catch(Exception exp){
            System.out.println("Returning default value for mcPackagesPath: " + generatedRepositoriesPath);
        }
        System.out.println("Returning: " + generatedRepositoriesPath);
        return generatedRepositoriesPath;
        
    }
     
      public String getRssTemplatePath(){
        try{
            if(prop.getProperty("rssTemplatePath")!=null){
                rssTemplatePath = prop.getProperty("rssTemplatePath");
            }
        }catch(Exception exp){
            System.out.println("Returning default value for rssTemplatePath: " + rssTemplatePath);
        }
        System.out.println("Returning: " + rssTemplatePath);
        return rssTemplatePath;
        
    }
      
    public String getSourceXLS(){
        try{
            if(prop.getProperty("sourceXLS")!=null){
                sourceXLS = prop.getProperty("sourceXLS");
            }
        }catch(Exception exp){
            System.out.println("Returning default value for rssTemplatePath: " + sourceXLS);
        }
        System.out.println("Returning: " + sourceXLS);
        return sourceXLS;
        
    }
    
    public String getSourceDB(){
        try{
            if(prop.getProperty("sourceDB")!=null){
                sourceDB = prop.getProperty("sourceDB");
            }
        }catch(Exception exp){
            System.out.println("Returning default value for rssTemplatePath: " + sourceDB);
        }
        System.out.println("Returning: " + sourceDB);
        return sourceDB;
        
    }
}
