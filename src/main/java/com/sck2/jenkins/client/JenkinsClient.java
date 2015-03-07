package com.sck2.jenkins.client;

import java.util.List;

/**
 * @author newlight77@gmail.com
 */
public interface JenkinsClient {

    public List<String> listJobs();

    public List<String> listUsers();

    public List<String> listViews();

    public int createJob(String name, String configXML);

    public int renameJob(String name, String newName);

    public int deleteJob(String name);

    public int launchBuild(String name);

    public int deleteBuild(String name, int buildNumber);

    public int createUser(String name, String configXML);

    public int deleteUser(String name);

    public int createView(String name, String configXml);

    public int deleteView(String name);
}
