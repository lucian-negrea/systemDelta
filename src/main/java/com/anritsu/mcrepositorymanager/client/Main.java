/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author ro100051
 */
public class Main implements EntryPoint{

    public Main() {
    }
    
    @Override
    public void onModuleLoad() {
        
        
        SelectFilterForm selectFilterForm = new SelectFilterForm();
        RootPanel.get().add(selectFilterForm);
    }
    
}
