/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.test.workshop.arquillian.annotations;

import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class AnnotationsTestCase {

    @Deployment
    public static Archive getDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class);
        // add resources
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        // add classes
        war.addClasses(Injection.class, VetoedInjection.class);

        // add libs
        PomEquippedResolveStage stage = Maven.resolver().loadPomFromFile(getPom());
        File[] solder = stage.resolve("org.jboss.seam.solder:seam-solder").withoutTransitivity().as(File.class);
        war.addAsLibraries(solder);

        // verbose
        System.out.println(war.toString(true));

        return war;
    }

    @Inject Injection injection;

    @Test
    public void testDefault() throws Exception {
        Assert.assertNotNull(injection);
        System.out.println("Injection: " + injection);
    }

    private static String getPom() {
        File file = new File(".");
        String path = "pom.xml";
        if (file.getAbsolutePath().contains("arquillian") == false)
            path = "arquillian/" + path;
        return path;
    }
}
