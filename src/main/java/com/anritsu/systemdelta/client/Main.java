/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.client;

import com.google.gwt.core.client.EntryPoint;
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
        FilterForm filterForm = new FilterForm();
        RootPanel.get().add(filterForm);
    }
}
