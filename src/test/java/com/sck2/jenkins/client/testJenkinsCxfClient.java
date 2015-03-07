package com.sck2.jenkins.client;

import com.sck2.jenkins.JenkinsTestHelper;
import com.sck2.jenkins.client.cxf.JenkinsCxfClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author newlight77@gmail.com
 */
public class testJenkinsCxfClient {

    private JenkinsEnv env = new JenkinsEnv();
    private JenkinsCxfClient client = JenkinsCxfClient.get(env);
    private JenkinsTestHelper helper = new JenkinsTestHelper(client);

    @After public void afterTest() {
        helper.clean();
    }

    @Test public void listJobs() {
        String jobName = "listJobs";
        String configXml = ConfigFile.getJobConfigTemplate();
        helper.createJob(jobName, configXml);
        Assert.assertTrue(helper.isPresent(jobName, client.listJobs()));
    }

    @Test public void createJob() {
        String jobName = "createJob";
        String configXml = ConfigFile.getJobConfigTemplate();
        helper.createJob(jobName, configXml);
    }

    @Test public void renameJob() {
        String jobName = "renameJob";
        String newJobName = jobName + ".1";
        String configXml = ConfigFile.getJobConfigTemplate();
        helper.createJob(jobName, configXml);
        helper.renameJob(jobName, newJobName);
    }

    @Test public void deleteJob() {
        String jobName = "deleteJob";
        String configXml = ConfigFile.getJobConfigTemplate();
        helper.createJob(jobName, configXml);
        helper.deleteJob(jobName);
    }

    @Test public void launchBuild() {
        String jobName = "launchBuild";
        String configXml = ConfigFile.getJobConfigTemplate();
        helper.createJob(jobName, configXml);
        helper.launchBuild(jobName);
    }

    @Test public void deleteBuild() throws InterruptedException {
        String jobName = "deleteBuild";
        String configXml = ConfigFile.getJobConfigTemplate();
        helper.createJob(jobName, configXml);
        helper.launchBuild(jobName);
        Thread.sleep(10000);
        helper.deleteBuild(jobName, 1);
    }

    @Ignore @Test public void createUser() {
        String user = "createUser";
        String configXml = ConfigFile.getUserConfigTemplate();
        helper.createUser(user, configXml);
    }

    @Ignore @Test public void deleteUser() {
        String user = "deleteUser";
        String configXml = ConfigFile.getUserConfigTemplate();
        helper.createUser(user, configXml);
        helper.deleteUser(user);
    }

    @Test public void createView() {
        String user = "createView";
        String configXml = ConfigFile.getViewConfigTemplate();
        helper.createView(user, configXml);
    }

    @Test public void deleteView() {
        String view = "deleteView";
        String configXml = ConfigFile.getViewConfigTemplate();
        helper.createView(view, configXml);
        helper.deleteView(view);
    }
}
