/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.utils;

import com.anritsu.mcrepositorymanager.packageinfoparser.RSSParser;
import com.anritsu.mcrepositorymanager.server.GWTMCRepositoryManagerServiceImpl;
import com.anritsu.mcrepositorymanager.shared.McPackage;
import com.anritsu.mcrepositorymanager.shared.PackingStatus;
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
public class Packing {

    private final String FOLDER_PATH = Configuration.getInstance().getMcPackagesPath() + "/";
    private final String REPOSITORY_LOCATION = Configuration.getInstance().getGeneratedRepositoriesPath() + "/";
    private static final Logger LOGGER = Logger.getLogger(Packing.class.getName());
    private final String ARCHIVE_NAME = String.valueOf(System.currentTimeMillis()) + ".zip";
    private ArrayList<McPackage> packageListToBePacked;
    private ZipOutputStream out;
    private File archiveNameFile;
    private PackingStatus status = new PackingStatus();

    public Packing(ArrayList<McPackage> packageListToBePacked) {
        this.packageListToBePacked = packageListToBePacked;
        archiveNameFile = new File(REPOSITORY_LOCATION + ARCHIVE_NAME);
        try {
            out = new ZipOutputStream(new FileOutputStream(archiveNameFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (McPackage p : packageListToBePacked) {
            p.setPackageSize(getPackageSize(p));
        }

        // Update status.packageDownloadedSize
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    String fileName = status.getProcessingPackage().getFileName();
                    File f = new File(FOLDER_PATH + fileName);
                    status.getProcessingPackage().setPackageDownloadedSize(f.length());
                } catch (Exception exp) {
                    status.getProcessingPackage().setPackageDownloadedSize(0);
                }
            }
        }, 0, 1000);
    }

    public void logDeadLink(McPackage p, Exception ex) {
        try (PrintWriter out = new PrintWriter(new FileWriter("MCRepositoryManager.log", true))) {

            out.append(p.getMcVersion() + " " + p.getName() + " " + ex.getMessage() + "\n");
        } catch (FileNotFoundException exp) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, exp);
        } catch (IOException exp) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, exp);
        }
    }

    public long getPackageSize(McPackage p) {
        HttpURLConnection conn = null;
        long result = 0;
        try {
            for (String link : p.getDownloadLinks()) {
                System.out.println("Checking package size for: " + link);
                URL url = new URL(link);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                conn.getInputStream();
                result += conn.getContentLengthLong();
            }
            return result;

        } catch (MalformedURLException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
            logDeadLink(p, ex);
        } catch (IOException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
            logDeadLink(p, ex);
        } finally {
            try {
                conn.disconnect();
            } catch (Exception exp) {
                System.out.println("Error while disconnecting!");
            }
        }
        return 0;
    }

    public String buildArchive() {
        status.setPackageList(packageListToBePacked);
        for (McPackage p : packageListToBePacked) {
            String fileName = p.getFileName();
            status.setProcessingPackage(p);
            if (p.isAddToRepository()) {
                downloadPackage(p);

                addPackageToArchive(p);

            }
        }
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return REPOSITORY_LOCATION + ARCHIVE_NAME;
    }

    public boolean downloadPackage(McPackage p) {
        boolean result = false;
        for (String link : p.getDownloadLinks()) {
            try {
                String dowloadLink[] = link.split("/");
                String fileName = dowloadLink[dowloadLink.length - 1];
                status.getDownloadedPackages().add(fileName);
                System.out.println("Downloading package " + fileName + ": " + link);
                if (!new File(FOLDER_PATH + fileName).exists()) {
                    FileUtils.copyURLToFile(new URL(link), new File(FOLDER_PATH + fileName));
                }
                LOGGER.log(Level.INFO, link + " succesfully downloaded!");
                result = true;
            } catch (IOException ex) {
                Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
                result = false;
            }
        }
        return result;
    }

    public boolean addPackageToArchive(McPackage p) {
        boolean result = false;
        for (String link : p.getDownloadLinks()) {
            try {
                String dowloadLink[] = link.split("/");
                String fileName = dowloadLink[dowloadLink.length - 1];
                System.out.println("Archiving " + fileName + ": " + link);
                status.getArchivedPackages().add(fileName);

                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(FOLDER_PATH + fileName);
                ZipEntry e = new ZipEntry(fileName);
                out.putNextEntry(e);
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
                LOGGER.log(Level.INFO, link + " succesfully archived!");
                result=true;
            } catch (Exception ex) {
                Logger.getLogger(Packing.class.getName()).log(Level.SEVERE, null, ex);
                result=false;
            } 
        }
        return result;
    }

    public PackingStatus getStatus() {
        return status;
    }

}
