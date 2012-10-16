package org.jboss.test.workshop.arquillian.cluster;

import junit.framework.Assert;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;


/**
 * @author Ales Justin
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ClusteredWebTestCase {
    private static final String STRATOS = "Project_Stratos";

    private static HttpClient client;

    @BeforeClass
    public static void setUp() {
        client = new DefaultHttpClient();
    }

    @AfterClass
    public static void tearDown() {
        if (client != null)
            client.getConnectionManager().shutdown();
    }

    @Deployment(name = "dep1")
    @TargetsContainer("container-1")
    public static WebArchive getDeploymentA() {
        return getDeployment();
    }

    @Deployment(name = "dep2")
    @TargetsContainer("container-2")
    public static WebArchive getDeploymentB() {
        return getDeployment();
    }

    protected static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class, "cluster-tests.war")
                .addClasses(Holder.class, HolderServlet.class)
                .setWebXML("cluster-web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @InSequence(10)
    @OperateOnDeployment("dep1")
    public void testClusterOnDepA(@ArquillianResource URL url) throws Exception {
        String uri = url.toExternalForm() + "holder?state=" + STRATOS;
        System.out.println("Node1 - uri = " + uri);
        HttpPost post = new HttpPost(uri);
        HttpResponse response = client.execute(post);
        String actual = EntityUtils.toString(response.getEntity());
        Assert.assertEquals("OK", actual);
        // sync
        Thread.sleep(3000L);
    }

    @Test
    @InSequence(20)
    @OperateOnDeployment("dep2")
    public void testClusterOnDepB(@ArquillianResource URL url) throws Exception {
        String uri = url.toExternalForm() + "holder";
        System.out.println("Node2 - uri = " + uri);
        HttpPost post = new HttpPost(uri);
        HttpResponse response = client.execute(post);
        String actual = EntityUtils.toString(response.getEntity());
        Assert.assertEquals(STRATOS, actual);
    }
}
