package com.sck2.jenkins;

import com.sck2.jenkins.client.JenkinsClient;
import org.junit.Assert;

import java.util.List;

/**
 * @author newlight77@gmail.com
 */
public class JenkinsTestHelper {

    private JenkinsClient client;
    public JenkinsTestHelper(JenkinsClient client) {
        this.client = client;
    }

    public boolean isPresent(String name, List<String> list) {
        for (String item : list) {
            if (item.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void clean() {
        deleteAllJobs(client.listJobs());
        deleteAllUsers(client.listUsers());
        deleteAllViews(client.listViews());
    }

    private void deleteAllJobs(List<String> list) {
        for (String item : list) {
            deleteJob(item);
        }
    }

    private void deleteAllUsers(List<String> list) {
        for (String item : list) {
            deleteUser(item);
        }
    }

    private void deleteAllViews(List<String> list) {
        for (String item : list) {
            deleteView(item);
        }
    }

    public void createJob(String name, String configXml) {
        int response = client.createJob(name, configXml);
        Assert.assertEquals(response, 200);
        Assert.assertTrue(isPresent(name, client.listJobs()));
    }

    public void renameJob(String name, String newName) {
        int response = client.renameJob(name, newName);
        Assert.assertEquals(response, 302);
    }

    public void deleteJob(String name) {
        int response = client.deleteJob(name);
        Assert.assertEquals(response, 302);
    }

    public void launchBuild(String name) {
        int response = client.launchBuild(name);
        Assert.assertEquals(response, 201);
    }

    public void deleteBuild(String name, int number) {
        int response = client.deleteBuild(name, number);
        Assert.assertEquals(response, 200);
    }

    public void createUser(String name, String configXml) {
        int response = client.createUser(name, configXml);
        Assert.assertEquals(response, 200);
        Assert.assertTrue(isPresent(name, client.listUsers()));
    }

    public void deleteUser(String name) {
        int response = client.deleteUser(name);
        Assert.assertEquals(response, 302);
    }

    public void createView(String name, String configXml) {
        int response = client.createView(name, configXml);
        Assert.assertEquals(response, 200);
        Assert.assertTrue(isPresent(name, client.listViews()));
    }

    public void deleteView(String name) {
        int response = client.deleteView(name);
        Assert.assertEquals(response, 302);
    }
}
