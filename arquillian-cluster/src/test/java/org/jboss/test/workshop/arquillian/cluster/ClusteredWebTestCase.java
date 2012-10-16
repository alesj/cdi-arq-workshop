package org.jboss.test.workshop.arquillian.cluster;

import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;


/**
 * @author Ales Justin
 */
@RunWith(Arquillian.class)
public class ClusteredWebTestCase {

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
            .addClass(Holder.class)
            .setWebXML("cluster-web.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    Holder holder;

    @InSequence(10)
    @Test
    @OperateOnDeployment("dep1")
    public void testClusterOnDepA() {
        System.out.println("Running cluster test.");
        holder.setState("Project Stratos!!");
    }

    @InSequence(20)
    @Test
    @OperateOnDeployment("dep2")
    public void testClusterOnDepB() {
        waitForSync();
        System.out.println("Running cache test.");
        Assert.assertEquals("Project Stratos!!", holder.getState());
    }

    private void waitForSync() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
