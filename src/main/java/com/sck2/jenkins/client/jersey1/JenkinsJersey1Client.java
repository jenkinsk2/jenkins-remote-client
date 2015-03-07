package com.sck2.jenkins.client.jersey1;

import com.sck2.jenkins.client.JenkinsClient;
import com.sck2.jenkins.client.JenkinsEnv;
import com.sck2.jenkins.util.xml.XmlUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author newlight77@gmail.com
 */
@Log4j2 public class JenkinsJersey1Client implements JenkinsClient {

    private static JenkinsJersey1Client instance;
    private JenkinsEnv env;

    private Client client;

    private JenkinsJersey1Client(JenkinsEnv env) {
        client = Client.create();
    }

    public static JenkinsJersey1Client get(JenkinsEnv env) {
        if (instance == null) {
            instance = new JenkinsJersey1Client(env);
            instance.env = env;
            instance.useBasicAuthentication();
        }
        return instance;
    }

    public JenkinsJersey1Client useBasicAuthentication() {
        log.info("With Security :" + env.getUser() + "/" + env.getPassword());
        ClientFilter authFilter = new HTTPBasicAuthFilter(env.getUser(), env.getPassword());
        client.addFilter(authFilter);
        return instance;
    }

    private String getApiXml() {
        log.info("listJobs : ");

        String path = env.getHost() + "/api/xml";
        ClientResponse response =
            client.resource(path).type("application/xml").post(ClientResponse.class);
        log.info("Response :" + response.getStatus());
        return response.getEntity(String.class);
    }

    public List<String> listJobs() {
        log.info("listJobs : ");
        return XmlUtil.parseJobs(getApiXml());
    }

    public List<String> listUsers() {
        log.info("listUsers : ");
        return XmlUtil.parseUsers(getApiXml());
    }

    public List<String> listViews() {
        log.info("listViews : ");
        return XmlUtil.parseViews(getApiXml());
    }

    public int createJob(String name, String configXML) {
        log.info("createJob : " + name);
        String path = env.getHost() + "/createItem";
        ClientResponse response = client.resource(path).queryParam("name", name)
            .type(MediaType.APPLICATION_XML).post(ClientResponse.class, configXML);
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int renameJob(String name, String newName) {
        log.info("renameJob : name=" + name + " newName=" + newName);
        String path = env.getHost() + "/job/" + name + "/doRename";
        ClientResponse response =
            client.resource(path).queryParam("newName", newName)
                .type("application/xml").post(ClientResponse.class);
        log.info("Response :" + response.getStatus());
        if (response.getStatus() == 200) {
            return 302;
        }
        return response.getStatus();
    }

    public int deleteJob(String name) {
        log.info("deleteJob : name=" + name);
        String path = env.getHost() + "/job/" + name + "/doDelete";
        ClientResponse response = client.resource(path).post(ClientResponse.class);
        log.info("Response :" + response.getStatus());
        if (response.getStatus() == 200) {
            return 302;
        }
        return response.getStatus();
    }

    public int launchBuild(String name) {
        log.info("launchBuild : name " + name);
        String path = env.getHost() + "/job/" + name + "/build";
        ClientResponse response =
            client.resource(path).queryParam("token", env.getBuildToken())
                .post(ClientResponse.class);
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int deleteBuild(String name, int buildNumber) {
        log.info("launchBuild : name " + name + " build " + buildNumber);
        String path = env.getHost() + "/job/" + name + "/" + buildNumber + "/confirmDelete";
        ClientResponse response = client.resource(path).post(ClientResponse.class);
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int createUser(String name, String configXML) {
        log.info("createUser : name " + name);
        //String path = "securityRealm/createAccountByAdmin";
        String path = env.getHost() + "/securityRealm/createAccount";
        ClientResponse response = client.resource(path).queryParam("username", name)
            .queryParam("password1", name).queryParam("password2", name)
            .queryParam("fullname", name).queryParam("email", name + "@email.com")
            .type(MediaType.APPLICATION_XML).post(ClientResponse.class);
        log.info("Response :" + response.getStatus());
        if (response.getStatus() == 200) {
            return 302;
        }
        return response.getStatus();
    }

    public int deleteUser(String name) {
        log.info("deleteUser : name " + name);
        String path = env.getHost() + "/user/" + name + "/doDelete";
        ClientResponse response = client.resource(path).post(ClientResponse.class);
        log.info("Response :" + response.getStatus());
        if (response.getStatus() == 200) {
            return 302;
        }
        return response.getStatus();
    }

    public int createView(String name, String configXml) {
        log.info("createView : name " + name);
        String path = env.getHost() + "/createView";
        ClientResponse response = client.resource(path).queryParam("name", name)
            .type(MediaType.APPLICATION_XML).post(ClientResponse.class, configXml);
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int deleteView(String name) {
        log.info("deleteView : name " + name);
        String path = env.getHost() + "/view/" + name + "/doDelete";
        ClientResponse response = client.resource(path).post(ClientResponse.class);
        log.info("Response :" + response.getStatus());
        if (response.getStatus() == 200) {
            return 302;
        }
        return response.getStatus();
    }
}
