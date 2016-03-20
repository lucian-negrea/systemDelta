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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        archiveNameFile = new File(RSSParser.FOLDER_PATH + ARCHIVE_NAME);
        try {
            out = new ZipOutputStream(new FileOutputStream(archiveNameFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(McPackage p: packageListToBePacked){
            p.setPackageSize(getPackageSize(p));
        }
              
        // Update status.packageDownloadedSize
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                
                try{
                String fileName = status.getProcessingPackage().getFileName();
                File f = new File(RSSParser.FOLDER_PATH + fileName);
                status.getProcessingPackage().setPackageDownloadedSize(f.length());
                }catch(Exception exp){
                    status.getProcessingPackage().setPackageDownloadedSize(0);
                }
            }
        }, 0, 1000);
    }
    
    public void logDeadLink(McPackage p, Exception ex){
        try (PrintWriter out = new PrintWriter(new FileWriter("systemDelta.log", true))){
            
            out.append(p.getMcVersion() + " " + p.getName() + " " + ex.getMessage() + "\n");
        } catch (FileNotFoundException exp) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, exp);
        } catch (IOException exp) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, exp);
        }
    }
    
    public long getPackageSize(McPackage p) {
        HttpURLConnection conn = null;
        try {
            //System.out.println("Checking download link: " + p.getDownloadLink());
            URL url = new URL(p.getDownloadLink());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            return conn.getContentLengthLong();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
            logDeadLink(p, ex);
        } catch (IOException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
            logDeadLink(p, ex);
        }finally{
            try{
                conn.disconnect();
            }catch(Exception exp){
                System.out.println("Error while disconnecting!");
            }
        }
        return 0;
    }

    public String buildArchive() {
        status = new PackingStatus();
        status.setPackageList(packageListToBePacked);
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
        return ARCHIVE_NAME;
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
