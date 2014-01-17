package edu.umass.ciir.strepsi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Reads properties from a file in src/main/resources that is called with the following patterns (in that order)
 * <p/>
 * <project>-<user>@<hostname>.properties
 * <p/>
 * <project>-<hostname>.properties
 * <p/>
 * <project>.properties
 */
public final class ConfigLoader {
    static final String resourcePath = "src/main/resources/";
    static final String username = System.getProperty("user.name", "user");

    private static List<File> possibleFilenames(String project) {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "host";
        }
        // drop domain from hostname and lowercase
        int dotIdx = hostname.indexOf('.');
        if (dotIdx != -1) {
            hostname = hostname.substring(0, dotIdx).toLowerCase();
        }


        List<File> filenames = new ArrayList<File>();
        filenames.add(new File(resourcePath + project + ".properties"));

        String[] gridservers = new String[]{"compute", "sydney"};

        for (String gridserver : gridservers) {
            if (hostname.startsWith(gridserver)) {
                filenames.add(new File(resourcePath + project + "-" + gridserver + ".properties"));
            }
            filenames.add(new File(resourcePath + project + "-" + hostname + ".properties"));
            if (hostname.startsWith(gridserver)) {
                filenames.add(new File(resourcePath + project + "-" + username + "@" + gridserver + ".properties"));
            }
        }
        filenames.add(new File(resourcePath + project + "-" + username + "@" + hostname + ".properties"));


        String manualProps = System.getProperty(project + ".props", "");
        if (!manualProps.isEmpty()) {
            System.out.println("Found property " + project + ".props = " + manualProps);
            filenames.add(new File(resourcePath + manualProps));
        }


        return (filenames);

    }

    private static void localReadProps(Properties properties, List<File> possibleFiles, String project) {
        Collections.reverse(possibleFiles);
        for (File propFile : possibleFiles) {
            System.out.println("try loading " + project + " loading properties: " + propFile);
            try {
                FileInputStream propStream = new FileInputStream(propFile);
                properties.load(propStream);
                System.out.println("...loaded from " + propFile);
                return;
            } catch (IOException e) {
                System.out.println("Unable to load file : " + propFile.getAbsolutePath() + " " + e.getMessage());
            }
        }

        throw new RuntimeException("Could not find property file for project " + project + ". " +
                "Options (last preferred): " + possibleFilenames(project) +
                "Local directory is " + (new File("").getAbsolutePath()));

    }

    public static Properties readProps(String project) {
        Properties properties = System.getProperties();
        localReadProps(properties, possibleFilenames(project), project);
        return (properties);
    }

}


