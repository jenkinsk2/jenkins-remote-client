package com.sck2.jenkins.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author newlight77@gmail.com
 */
public class ConfigFile {

    enum ConfigTemplate {
        JOB("job_template.xml"),
        USER("user_template.xml"),
        VIEW("view_template.xml");

        private String filename;

        ConfigTemplate(String filename) {
            this.filename = filename;
        }
    }

    public static String getJobConfigTemplate() {
        return getConfigTemplate(ConfigTemplate.JOB);
    }

    public static String getUserConfigTemplate() {
        return getConfigTemplate(ConfigTemplate.USER);
    }

    public static String getViewConfigTemplate() {
        return getConfigTemplate(ConfigTemplate.VIEW);
    }

    private static String getConfigTemplate(ConfigTemplate template) {
        try {
            return fileToString(template.filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Java 7
     *
     * @param filename text file
     * @return file content as string
     * @throws IOException
     */
    public static String fileToString(String filename) throws IOException {
        Path path = Paths.get("./src/main/resources", filename);
        return new String(Files.readAllBytes(path));
    }

}
