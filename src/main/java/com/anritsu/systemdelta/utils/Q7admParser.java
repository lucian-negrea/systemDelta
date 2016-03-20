/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.utils;

import com.anritsu.systemdelta.shared.Filter;
import com.anritsu.systemdelta.shared.McPackage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RO100051
 */
public class Q7admParser {

    private Filter filter;
    private HashMap<String, ArrayList<String>> q7admPackageNameVersion = new HashMap<>();

    public Q7admParser(Filter filter) {
        this.filter = filter;
        parseFilter();
    }

    public Q7admParser() {

    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean isMcPackageMatchCustomerFilter(McPackage p) {
        return p.getCustomerList().contains(filter.getCustomer()) || p.getCustomerList().contains("");
    }
    
    public boolean isMcPackageMatchMcComponentFilter(McPackage p) {
        return filter.getMcComponent().contains(p.getName()) || filter.getMcComponent().isEmpty();
    }
    
    public boolean isMcPackageMatchAvailabilityFilter(McPackage p) {
        return filter.getAvailability().contains(p.getAvailability()) || filter.getAvailability().isEmpty();
    }

    public boolean isMcPackageMatchQ7admOutput(McPackage p) {
        //System.out.println("Filter: " +filter.getQ7admOutput());
        if(filter.getQ7admOutput().equals("")) {
            p.setQ7admOutputVersion("");
            return true;
        }
        //To be implemented!
        for (Map.Entry<String, ArrayList<String>> entry : q7admPackageNameVersion.entrySet()) {
            if(entry.getKey().startsWith("protocol-") && entry.getKey().matches(".*-" + p.getName() + "-.*")){
                p.setQ7admOutputVersion(getQ7admLastVersion(entry.getValue()));
                //System.out.println("Name: " + entry.getKey() + " Version: " + Arrays.toString(entry.getValue().toArray()));
                return true;
            }else if(entry.getKey().replace("-Linux64", "").replace("-Linux", "").equals(p.getName()) ){
                p.setQ7admOutputVersion(getQ7admLastVersion(entry.getValue()));
                //System.out.println("Name: " + entry.getKey() + " Version: " + Arrays.toString(entry.getValue().toArray()));
                return true;
            } else{
                //System.out.println("Name: " + entry.getKey()  + "NOMATCH: " + p.getName() + " Version: " + Arrays.toString(entry.getValue().toArray()));
            }
        }
        return false;
    }
    
    public String getQ7admLastVersion(ArrayList<String> versions){
        HashMap<Integer,String> newVersionsMap = new HashMap<>();
        ArrayList<Integer> newVersionList = new ArrayList<>();
        for(String s: versions){
            Integer newS = Integer.parseInt(s.split("-")[0].replace(".", ""));
            newVersionsMap.put(newS, s);
            newVersionList.add(newS);
        }
        Collections.sort(newVersionList);
        //System.out.println(Arrays.toString(newVersionList.toArray()));
        return newVersionsMap.get(newVersionList.get(newVersionList.size()-1));
    }

    public void parseFilter() {
        ArrayList<String> parsedPackageNameList = new ArrayList<>();
        String lines[] = filter.getQ7admOutput().split("\\r?\\n");
        String packageVersion;
        String packageName;
        for (String line1 : lines) {
            if (line1.contains("installed as")) {
                String[] line = line1.replaceAll("^\\s+", "").split(" ");
                String packageNameAndVersion[] = line[0].split("-");
                int arrLength = packageNameAndVersion.length;
                if (line[0].contains("protocol-") && !(line[0].contains("protocol-servers"))) {
                    // Protocol package
                    packageVersion = packageNameAndVersion[arrLength - 2] + "-" + packageNameAndVersion[arrLength - 1];
                    packageName = line[0].replace("-" + packageVersion, "");
                } else {
                    // Normal package
                    packageVersion = packageNameAndVersion[arrLength - 1];
                    packageName = line[0].replace("-" + packageVersion, "");
                }
                if (parsedPackageNameList.contains(packageName)) {
                    for (Map.Entry<String, ArrayList<String>> entry : q7admPackageNameVersion.entrySet()) {
                        if (entry.getKey().equals(packageName)) {
                            entry.getValue().add(packageVersion);
                        }
                    }
                } else {
                    ArrayList<String> packageVersionArray = new ArrayList<>();
                    packageVersionArray.add(packageVersion);
                    q7admPackageNameVersion.put(packageName,packageVersionArray);
                    parsedPackageNameList.add(packageName);
                }

            }
        }
       
    }

}

