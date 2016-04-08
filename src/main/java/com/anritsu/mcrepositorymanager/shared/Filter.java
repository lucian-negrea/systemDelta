/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.shared;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author RO100051
 */
public class Filter implements Serializable{
    private String q7admOutput;
    private String mcVersion;
    private List<String> availability;
    private String customer;
    private List<String> mcComponent;

    public Filter() {
    }

    public List<String> getMcComponent() {
        return mcComponent;
    }

    public void setMcComponent(List<String> mcComponent) {
        this.mcComponent = mcComponent;
    }

    public String getQ7admOutput() {
        return q7admOutput;
    }

    public void setQ7admOutput(String q7admOutput) {
        this.q7admOutput = q7admOutput;
    }

    public String getMcVersion() {
        return mcVersion;
    }

    public void setMcVersion(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    public List<String> getAvailability() {
        return availability;
    }

    public void setAvailability(List<String> availability) {
        this.availability = availability;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    
}
