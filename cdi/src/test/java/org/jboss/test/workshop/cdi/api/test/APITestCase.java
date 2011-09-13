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

package org.jboss.test.workshop.cdi.api.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.workshop.cdi.api.support.ApiBean;
import org.jboss.test.workshop.cdi.api.support.Bad;
import org.jboss.test.workshop.cdi.api.support.Car;
import org.jboss.test.workshop.cdi.api.support.Good;
import org.jboss.test.workshop.cdi.api.support.Modified;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class APITestCase {

    @Inject @Good private Provider<Car> carProvider;
    @Inject @Modified(wheels = 42) private Car modified;

    @Deployment(name = "api")
    public static Archive getDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ApiBean.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @OperateOnDeployment("api")
    public void testAPI(ApiBean bean) throws Exception {
        BeanManager manager = bean.getManager();
        Assert.assertNotNull(manager);

        Iterable<Car> badCars = bean.findCar(new AnnotationLiteral<Bad>() {});
        Assert.assertEquals(2, count(badCars));

        Assert.assertNotNull(carProvider);
        Assert.assertEquals(6, carProvider.get().numberOfWheels());

        Assert.assertNotNull(modified);
        Assert.assertEquals(42, modified.numberOfWheels());
    }

    private int count(Iterable iterable) {
        int i = 0;
        for (Object o : iterable)
            i++;
        return i;
    }
}
