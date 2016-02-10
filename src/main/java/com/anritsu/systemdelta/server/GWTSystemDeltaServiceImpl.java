/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.anritsu.systemdelta.shared.McPackage;
import com.anritsu.systemdelta.client.GWTSystemDeltaService;
import com.anritsu.systemdelta.utils.Q7admParser;
import com.anritsu.systemdelta.utils.RSSParser;
import com.anritsu.systemdelta.shared.Filter;
import com.anritsu.systemdelta.shared.PackingStatus;
import com.anritsu.systemdelta.utils.Packing;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author ro100051
 */
public class GWTSystemDeltaServiceImpl extends RemoteServiceServlet implements GWTSystemDeltaService {
    private Packing packing;
    private RSSParser parser;
    //private Filter f;
       
    public ArrayList<String> getAvailabilities(Filter f/*Parameter not used*/){
        ArrayList<String> availabilities = new ArrayList<>();
        HashSet<String> av = parser.getAvailability();
        for(String s: av){
            availabilities.add(s);
        }
        return availabilities;
    }
    
    public ArrayList<String> getCustomers(Filter f /*Parameter not used*/) {
        ArrayList<String> customers = new ArrayList<>();
        HashSet<String> cus = parser.getCustomers();
        for(String s: cus){
            customers.add(s);
        }
        return customers;
    }

    @Override
    public String generateRepository(ArrayList<McPackage> packages) {
        packing = new Packing(packages);
        return packing.buildArchive();
    }

    @Override
    public ArrayList<McPackage> getPackageList(Filter filter) {
        ArrayList<McPackage> packageList = new ArrayList<>();
        HashSet<McPackage> pack = parser.getPackageList();
        for(McPackage p: pack){
            Q7admParser q7admParser = new Q7admParser();
            if(q7admParser.isMcPackageMatchAvailabilityFilter(p, filter) && q7admParser.isMcPackageMatchCustomerFilter(p, filter)){
                packageList.add(p);
            }
        }
        Collections.sort(packageList);
        return packageList;
    }

    @Override
    public List<String> getMcVersions() {
        List<String> mcVersions = new ArrayList<>();
        File folder = new File(RSSParser.FOLDER_PATH);
        for(File f: folder.listFiles()){
            if(f.isFile() && f.getName().matches("MC-(.*)-ReleaseStatus.xlsx")){
                mcVersions.add(f.getName().replace("MC-", "").replace("-ReleaseStatus.xlsx", ""));
            }
        }
        System.out.println(mcVersions.size() + " versions detected!");
        return mcVersions;
    }

    @Override
    public String setMcVersion(Filter f) {
        parser = new RSSParser(f);
        return parser.getMcVersion();
    }    

    @Override
    public PackingStatus getPackingStatus() {
        return packing.getStatus();
        
    }

    
}
