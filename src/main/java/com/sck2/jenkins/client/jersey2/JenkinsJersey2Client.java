package com.sck2.jenkins.client.jersey2;

import com.sck2.jenkins.client.JenkinsClient;
import com.sck2.jenkins.client.JenkinsEnv;
import com.sck2.jenkins.util.xml.XmlUtil;
import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author newlight77@gmail.com
 */
@Log4j2 public class JenkinsJersey2Client implements JenkinsClient {

    private static JenkinsJersey2Client instance;
    private JenkinsEnv env;

    private Client client;

    private JenkinsJersey2Client(JenkinsEnv env) {
        client = ClientBuilder.newClient();
    }

    public static JenkinsJersey2Client get(JenkinsEnv env) {
        if (instance == null) {
            instance = new JenkinsJersey2Client(env);
            instance.env = env;
            instance.useBasicAuthentication();
        }
        return instance;
    }

    public JenkinsJersey2Client useBasicAuthentication() {
        HttpAuthenticationFeature feature =
            HttpAuthenticationFeature.basic(env.getUser(), env.getPassword());
        client.register(feature);
        return instance;
    }

    private String getApiXml() {
        log.info("listJobs : ");

        String path = env.getHost() + "/api/xml";
        Response response =
            client.target(path).request().post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.readEntity(String.class);
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
        Response response = client.target(path).queryParam("name", name).request()
            .post(Entity.entity(configXML, MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int renameJob(String name, String newName) {
        log.info("renameJob : name=" + name + " newName=" + newName);
        String path = env.getHost() + "/job/" + name + "/doRename";
        Response response =
            client.target(path).queryParam("newName", newName).request()
                .post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int deleteJob(String name) {
        log.info("deleteJob : name=" + name);
        String path = env.getHost() + "/job/" + name + "/doDelete";
        Response response = client.target(path).request()
            .post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int launchBuild(String name) {
        log.info("launchBuild : name " + name);
        String path = env.getHost() + "/job/" + name + "/build";
        Response response =
            client.target(path).queryParam("token", env.getBuildToken()).request()
                .post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int deleteBuild(String name, int buildNumber) {
        log.info("launchBuild : name " + name + " build " + buildNumber);
        String path = env.getHost() + "/job/" + name + "/" + buildNumber + "/confirmDelete";
        Response response = client.target(path).request()
            .post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int createUser(String name, String configXML) {
        log.info("createUser : name " + name);
        //String path = env.getHost() + "securityRealm/createAccountByAdmin";
        String path = env.getHost() + "/securityRealm/createAccount";
        Response response = client.target(path).queryParam("username", name)
            .queryParam("password1", name).queryParam("password2", name)
            .queryParam("fullname", name).queryParam("email", name + "@email.com").request()
            .post(Entity.entity(configXML, MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int deleteUser(String name) {
        log.info("deleteUser : name " + name);
        String path = env.getHost() + "/user/" + name + "/doDelete";
        Response response = client.target(path).request()
            .post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int createView(String name, String configXml) {
        log.info("createView : name " + name);
        String path = env.getHost() + "/createView";
        Response response = client.target(path).queryParam("name", name).request()
            .post(Entity.entity(configXml, MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }

    public int deleteView(String name) {
        log.info("deleteView : name " + name);
        String path = env.getHost() + "/view/" + name + "/doDelete";
        Response response = client.target(path).request()
            .post(Entity.entity("", MediaType.APPLICATION_XML));
        log.info("Response :" + response.getStatus());
        return response.getStatus();
    }
}
