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

package org.jboss.test.workshop.cdi.scope.test;

import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.workshop.cdi.scope.support.Service;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class ResetTestCase {
    @Deployment
    public static Archive getDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
        jar.addPackage(Service.class.getPackage());
        // hack -- SW_ClassLoader is leaking system classpath
        jar.addAsManifestResource(new StringAsset(
                "org.jboss.seam.solder.bean.generic.GenericBeanExtension\n" +
                "org.jboss.seam.solder.bean.defaultbean.DefaultBeanExtension\n" +
                "org.jboss.seam.solder.core.CoreExtension\n" +
                "org.jboss.seam.solder.unwraps.UnwrapsExtension\n" +
                "org.jboss.seam.solder.messages.TypedMessageBundleExtension\n" +
                "org.jboss.seam.solder.logging.TypedMessageLoggerExtension\n" +
                "org.jboss.seam.solder.serviceHandler.ServiceHandlerExtension\n"), "services/" + Extension.class.getName());
        return jar;
    }

    @Inject private Service service;

    @Test
    public void testReset() throws Exception {
        Assert.assertNotNull(service);
        /*
        String first = service.doSomething();
        Handle.class.cast(service).destroy();
        String second = service.doSomething();
        Assert.assertFalse(first.equals(second));
        */
    }
}
