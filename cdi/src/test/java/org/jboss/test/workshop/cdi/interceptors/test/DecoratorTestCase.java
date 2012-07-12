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

package org.jboss.test.workshop.cdi.interceptors.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.workshop.cdi.interceptors.support.BusinessObject;
import org.jboss.test.workshop.cdi.interceptors.support.LargeAmountAccount;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class DecoratorTestCase {

    @Deployment(name = "decorator")
    public static Archive getDeployment() {
        Asset beansXml = new StringAsset("<beans><decorators><class>" + LargeAmountAccount.class.getName() + "</class></decorators></beans>");
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(LargeAmountAccount.class.getPackage())
                .addAsManifestResource(beansXml, "beans.xml");
    }

    @Test
    @OperateOnDeployment("decorator")
    public void testInterceptors(BusinessObject bo) throws Exception {
        bo.deposit(2011);
        bo.withdraw(1509);
        System.out.println("sum = " + bo.getState());
    }

}
