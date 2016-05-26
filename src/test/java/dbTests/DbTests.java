/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbTests;

import com.anritsu.mcrepositorymanager.dbcontroller.MainController;
import com.anritsu.mcrepositorymanager.packageinfoparser.PackageInfoParser;
import com.anritsu.mcrepositorymanager.packageinfoparser.PackageInfoParserFactory;
import com.anritsu.mcrepositorymanager.shared.Filter;
import com.anritsu.mcrepositorymanager.shared.McPackage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author RO100051
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/appContextTest.xml")
public class DbTests {
     
    @Autowired
    private MainController dbControllerTest;
    
    private Filter filter = new Filter();
    
    @Test
    public void connectionIsOK(){
        dbControllerTest.getPackages();
        Assert.assertTrue(dbControllerTest.getPackages().size() > 1);
    }
    
    
    @Test
    public void dbMCsupportedBaselines(){
        ArrayList<String> mcVersion = dbControllerTest.getMCVersions();
        Assert.assertTrue(mcVersion.get(0).equalsIgnoreCase("8.0"));
    }
    
    @Test
    public void generatePackageList(){
        filter.setMcVersion("8.0");
        List<String> packageNames = new ArrayList<>();
        packageNames.add("cdb-model");
        filter.setMcComponent(packageNames);
        PackageInfoParser parser = PackageInfoParserFactory.getPackageInfoParser(filter);
        HashSet<McPackage> mcPackages = parser.getPackageList(filter) ;
        mcPackages.forEach((McPackage p) -> System.out.println(p.getFileName())) ;
        Assert.assertTrue(!mcPackages.isEmpty());
    }
}
