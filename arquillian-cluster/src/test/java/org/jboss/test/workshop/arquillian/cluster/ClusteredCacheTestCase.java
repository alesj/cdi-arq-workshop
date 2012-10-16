package org.jboss.test.workshop.arquillian.cluster;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import java.io.IOException;

import static org.junit.Assert.*;



/**
 * @author Matej Lazar
 */
@RunWith(Arquillian.class)
public class ClusteredCacheTestCase {

    @Deployment (name = "dep1") @TargetsContainer("container-1")
    public static WebArchive getDeploymentA() {
        return getDeployment();
    }

    @Deployment(name = "dep2") @TargetsContainer("container-2")
    public static WebArchive getDeploymentB() {
        return getDeployment();
    }

    protected static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class, "cluster-tests.war")
            .setWebXML("cache-web.xml")
            .addAsManifestResource("jboss-deployment-structure.xml");
    }

    @Resource(mappedName="java:jboss/infinispan/container/cluster")
    CacheContainer container;

    protected Cache<String, String> getCache() {
        if (cache == null) {
            cache = container.getCache("default");
        }
        return cache;
    }

    private Cache<String, String> cache;

    @InSequence(10)
    @Test
    @OperateOnDeployment("dep1")
    public void testCacheOnDepA() {
        System.out.println("Running cache test.");
        getCache().put("key01", "value1");
        assertTrue(getCache().containsKey("key01"));
        assertEquals("value1", getCache().get("key01"));
    }

    @InSequence(20)
    @Test
    @OperateOnDeployment("dep2")
    public void testCacheOnDepB() {
        waitForSync();
        System.out.println("Running cache test.");
        assertTrue(getCache().containsKey("key01"));
        assertEquals("value1", getCache().get("key01"));
    }

    @InSequence(1000)
    @Test
    @OperateOnDeployment("dep1")
    public void cleanUp() throws IOException {
        getCache().clear();
    }

    @InSequence(1010)
    @Test
    @OperateOnDeployment("dep1")
    public void cleanTestClanUp() throws IOException {
        waitForSync();
        assertFalse(getCache().containsKey("key01"));
    }

    private void waitForSync() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
