/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.utils;

import com.anritsu.systemdelta.shared.McPackage;
import com.anritsu.systemdelta.shared.PackingStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ro100051
 */
public class Packing{

    private final String ARCHIVE_NAME = String.valueOf(System.currentTimeMillis()) + ".zip";
    private ArrayList<McPackage> packageListToBePacked;
    private ZipOutputStream out;
    private File archiveNameFile;
    private PackingStatus status;
    
    public Packing(ArrayList<McPackage> packageListToBePacked) {
        this.packageListToBePacked = packageListToBePacked;
        status = new PackingStatus();
        status.setPackageList(packageListToBePacked);
        archiveNameFile = new File(RSSParser.FOLDER_PATH + ARCHIVE_NAME);
        try {
            out = new ZipOutputStream(new FileOutputStream(archiveNameFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
        }
              
        // Update status.packageDownloadedSize
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                String fileName = status.getProcessingPackage().getFileName();
                File f = new File(RSSParser.FOLDER_PATH + fileName);
                status.setPackageDownloadedSize(f.length());
            }
        }, 0, 5000);
    }

    public String buildArchive() {
        for (McPackage p : packageListToBePacked) {
            String fileName = p.getFileName();
            status.setProcessingPackage(p);            
            if (p.isAddToRepository()) {
                downloadPackage(p);
                status.getDownloadedPackages().add(fileName);
                addPackageToArchive(p);
                status.getArchivedPackages().add(fileName);
            }
        }
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "http://localhost/" + ARCHIVE_NAME;
    }

    public boolean downloadPackage(McPackage p) {
        try {
            String fileName = p.getFileName();
            if (!new File(RSSParser.FOLDER_PATH + fileName).exists()) {
                FileUtils.copyURLToFile(new URL(p.getDownloadLink()), new File(RSSParser.FOLDER_PATH + fileName));
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean addPackageToArchive(McPackage p) {
        try {
            String fileName = p.getFileName();

            byte[] buf = new byte[1024];
            int len;
            FileInputStream in = new FileInputStream(RSSParser.FOLDER_PATH + fileName);
            ZipEntry e = new ZipEntry(fileName);
            out.putNextEntry(e);
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch( ZipException exp){
            System.out.println("Cought " + exp.getMessage());
        }
        catch (IOException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public PackingStatus getStatus() {
        return status;
    }


}
