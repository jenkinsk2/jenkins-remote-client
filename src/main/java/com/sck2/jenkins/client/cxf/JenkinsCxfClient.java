package com.sck2.jenkins.client.cxf;

import com.sck2.jenkins.client.JenkinsClient;
import com.sck2.jenkins.client.JenkinsEnv;
import com.sck2.jenkins.util.xml.XmlUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author newlight77@gmail.com
 */
@Log4j2 public class JenkinsCxfClient implements JenkinsClient {

    private static JenkinsCxfClient instance;
    private JenkinsEnv env;

    private WebClient client;

    private JenkinsCxfClient(JenkinsEnv env) {
        client = WebClient.create(env.getHost());
    }

    public static JenkinsCxfClient get(JenkinsEnv env) {
        if (instance == null) {
            instance = new JenkinsCxfClient(env);
            instance.env = env;
            //instance.useBasicAuthentication();
        }
        return instance;
    }

    // not working
    public JenkinsCxfClient useBasicAuthentication() {
        log.info("With Security :" + env.getUser() + "/" + env.getPassword());
        client = WebClient.create(env.getHost(), env.getUser(), env.getPassword(), "");
        return instance;
    }

    private String getApiXml() {
        log.info("listJobs : ");
        String path = env.getHost() + "/api/xml";
        client.reset();
        Response response = client.path(path).post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.readEntity(String.class);
    }

    @Override public List<String> listJobs() {
        log.info("listJobs : ");
        return XmlUtil.parseJobs(getApiXml());
    }

    @Override public List<String> listUsers() {
        log.info("listUsers : ");
        return XmlUtil.parseUsers(getApiXml());
    }

    @Override public List<String> listViews() {
        log.info("listViews : ");
        return XmlUtil.parseViews(getApiXml());
    }

    @Override public int createJob(String name, String configXML) {
        log.info("createJob : " + name);
        String path = env.getHost() + "/createItem";
        Response response = client.path(path).query("name", name)
            .post(Entity.entity(configXML, MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    @Override public int renameJob(String name, String newName) {
        log.info("renameJob : name=" + name + " newName=" + newName);
        String path = env.getHost() + "/job/" + name + "/doRename";
        Response response = client.path(path).query("newName", newName)
            .post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    @Override public int deleteJob(String name) {
        log.info("deleteJob : name=" + name);
        String path = env.getHost() + "/job/" + name + "/doDelete";
        Response response =
            client.path(path).post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    @Override public int launchBuild(String name) {
        log.info("launchBuild : name " + name);
        String path = env.getHost() + "/job/" + name + "/build";
        Response response = client.path(path).query("token", env.getBuildToken())
            .post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    @Override public int deleteBuild(String name, int buildNumber) {
        log.info("launchBuild : name " + name + " build " + buildNumber);
        String path = env.getHost() + "/job/" + name + "/" + buildNumber + "/confirmDelete";
        Response response =
            client.path(path).post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    @Override public int createUser(String name, String configXML) {
        log.info("createUser : name " + name);
        //String path = "securityRealm/createAccountByAdmin";
        String path = env.getHost() + "/securityRealm/createAccount";
        Response response =
            client.path(path).query("username", name).query("password1", name)
                .query("password2", name).query("fullname", name)
                .query("email", name + "@email.com")
                .post(Entity.entity(configXML, MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    @Override public int deleteUser(String name) {
        log.info("deleteUser : name " + name);
        String path = env.getHost() + "/user/" + name + "/doDelete";
        Response response =
            client.path(path).post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    @Override public int createView(String name, String configXml) {
        log.info("createView : name " + name);
        String path = env.getHost() + "/createView";
        Response response = client.path(path).query("name", name)
            .post(Entity.entity(configXml, MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    @Override public int deleteView(String name) {
        log.info("deleteView : name " + name);
        String path = env.getHost() + "/view/" + name + "/doDelete";
        Response response =
            client.path(path).post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }
}
