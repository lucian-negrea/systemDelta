/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.utils;

import com.anritsu.mcrepositorymanager.shared.Filter;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.anritsu.mcrepositorymanager.shared.McPackage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author RO100051
 */
public class RSSParser {

    public final static String FOLDER_PATH = "./";
    private String filePath;
    private String mcVersion;
    private HashSet<McPackage> packageList = new HashSet<>();
    private HashSet<String> customers = new HashSet<>();
    private HashSet<String> availability = new HashSet<>();

    public HashSet<McPackage> getPackageList() {
        return packageList;
    }

    public void setPackageList(HashSet<McPackage> packageList) {
        this.packageList = packageList;
    }

    public RSSParser(Filter filter) {
        mcVersion = filter.getMcVersion();
        this.filePath = FOLDER_PATH + "MC-" + mcVersion + "-ReleaseStatus.xlsx";
        parseRSS();
    }

    public String getMcVersion() {
        return mcVersion;
    }

    public HashSet<String> getCustomers() {
        return customers;
    }

    public HashSet<String> getAvailability() {
        return availability;
    }
    
    public void parseRSS() {
        try {
            FileInputStream file = new FileInputStream(new File(this.filePath));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get package list sheet from the workbook
            String sheetName = "MC " + mcVersion;
            XSSFSheet sheet = workbook.getSheet(sheetName);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() < 3) {
                    continue;
                }
                McPackage p = new McPackage();
                p.setMcVersion(mcVersion);
                p.setName(row.getCell(2).getStringCellValue());
                try {
                    p.setDownloadLink(row.getCell(7).getHyperlink().getAddress());
                    int urlIndex = p.getDownloadLink().split("/").length;
                    String fileName = p.getDownloadLink().split("/")[urlIndex - 1];
                    p.setFileName(fileName);
                } catch (NullPointerException exp) {
                    p.setDownloadLink("");
                }
                
                p.setPackageVersion(row.getCell(3).getStringCellValue());
                p.setAvailability(row.getCell(4).getStringCellValue());
                availability.add(row.getCell(4).getStringCellValue());

                // Set customers list
                ArrayList<String> cusList = new ArrayList<>();
                String[] customerCell = row.getCell(5).getStringCellValue().split(",");
                for (int i = 0; i < customerCell.length; i++) {
                    customers.add(customerCell[i]);
                    cusList = new ArrayList<>(Arrays.asList(customerCell));  
                }
                //System.out.println("Parsing line " + row.getRowNum());

                p.setCustomerList(new HashSet<>(cusList));
                packageList.add(p);
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
