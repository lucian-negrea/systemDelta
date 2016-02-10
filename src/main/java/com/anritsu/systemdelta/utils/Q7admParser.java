/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.utils;

import com.anritsu.systemdelta.shared.Filter;
import com.anritsu.systemdelta.shared.McPackage;
import java.util.ArrayList;

/**
 *
 * @author RO100051
 */
public class Q7admParser {
    private Filter filter;
    private McPackage mcPackage;
    
    private ArrayList<String> q7admOutputPackageList = new ArrayList<String>();

    public Q7admParser(Filter filter, McPackage mcPackage) {
        this.filter = filter;
        this.mcPackage = mcPackage;
    }
    
    public Q7admParser() {
        
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public McPackage getMcPackage() {
        return mcPackage;
    }

    public void setMcPackage(McPackage mcPackage) {
        this.mcPackage = mcPackage;
    }
    
    public boolean isMcPackageMatchCustomerFilter(McPackage p, Filter f){
        return p.getCustomerList().contains(f.getCustomer()) || p.getCustomerList().contains("");
    }
    
    public boolean isMcPackageMatchAvailabilityFilter(McPackage p, Filter f){
        return f.getAvailability().contains(p.getAvailability()) || f.getAvailability().contains("");
    }
    
    public boolean isMcPackageMatchQ7admOutput(McPackage p, Filter f){
        //To be implemented!
        return true;
    }
    
}
