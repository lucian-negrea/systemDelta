/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbTests;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ro100051
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/appContextTest.xml")
public class RegexTest {
    
    @Test
    public void testRegex() {
        String yumLine = "qbase.x86_64                                                                       7.0.0.932-1                                             @mc_repo";
        String q7admLine = "        qbase-6.2.2 (installed as 'qbase')";
        ArrayList<String> output = new ArrayList<>();
        output.add(yumLine);
        output.add(q7admLine);
        String packageName = "";
        String packageVersion = "";

        for (String line1 : output) {
            System.out.println("Parsing: " + line1);

            if (line1.contains("installed as")) {
                String[] line = line1.replaceAll("^\\s+", "").split(" ");
                System.out.println(line1 + " contains 'installed as'!");
                // q7adm list output                
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

            } else if (!line1.contains("installed") && line1.matches(".*\\.*\\.*\\.*\\.*")) {
                System.out.println("line1: yum " + line1);
                String[] line = line1.trim().replaceAll(" +", " ").split(" ");
                System.out.println(Arrays.toString(line));
                // yum list output
                String packageNameArr[] = line[0].split("\\.");
                packageName = packageNameArr[0];
                String packageVersionArr[] = line[1].split("\\-");
                packageVersion = packageVersionArr[0];
            }
            System.out.println("line1\n" + packageName + " " + packageVersion);
        }
    }
}
