/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.packageinfoparser;

import com.anritsu.mcrepositorymanager.shared.Filter;
import com.anritsu.mcrepositorymanager.utils.Configuration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RO100051
 */
public class PackageInfoParserFactory {
    
    private static final Logger LOGGER = Logger.getLogger(PackageInfoParserFactory.class.getName());
    
    public static PackageInfoParser getPackageInfoParser(Filter filter){
        if(Configuration.getInstance().getSourceXLS().contains(filter.getMcVersion())){
            LOGGER.log(Level.INFO, filter.getMcVersion() + " selectd => using local RSS as MC package info source.");
            return new RSSParser(filter);
        }else if(Configuration.getInstance().getSourceDB().contains(filter.getMcVersion())){
            LOGGER.log(Level.INFO, filter.getMcVersion() + " selectd => using changes.xml DB as MC package info source.");
            return new DBParser(filter);
            
        }else{
            LOGGER.log(Level.INFO, filter.getMcVersion() + " selectd => using changes.xml DB as MC package info source.");
            return new DBParser(filter);
        }
    }
}
