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
    private static String dbUser = "mclawpkginfo";
    private static String dbPassword = "mclawpkginfo";
    private static String dbIP = "172.28.35.31";
    private static String dbName = "mclawpkginfo";
    private static String rssFilesPath = "rss";
    private static String mcPackagesPath = "mcPackage";
    private static String generatedRepositories = "generatedRepositories";
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
}
