/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author FreakingBarbarians
 */
public class ManifestBuilder {

    private List<String[]> manifestArray;
    private String filename;

    public ManifestBuilder(String filename) {
        manifestArray = new LinkedList();
        this.filename = filename;
    }

    public HashMap<String, String> build() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        HashMap<String, String> manifest = new HashMap();
        while (line != null) {
            manifestArray.add(line.split("\t"));
            // debug
            line = reader.readLine();
        }
        for (String[] resource : manifestArray) {
            manifest.put(resource[0], resource[1]);
        }
        return manifest;
    }
}
