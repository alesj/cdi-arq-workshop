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

package org.jboss.test.workshop.cdi.inject.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.workshop.cdi.inject.support.AlternativeShape;
import org.jboss.test.workshop.cdi.inject.support.Blackboard;
import org.jboss.test.workshop.cdi.inject.support.Circle;
import org.jboss.test.workshop.cdi.inject.support.Shape;
import org.jboss.test.workshop.cdi.inject.support.Square;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class AlternativeTestCase {

    @Deployment(name = "interceptors")
    public static Archive getDeployment() {
        Asset beansXml = new StringAsset("<beans><alternatives><class>" + AlternativeShape.class.getName() + "</class></alternatives></beans>");
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Shape.class, Circle.class, Square.class, AlternativeShape.class, Blackboard.class)
                .addAsManifestResource(beansXml, "beans.xml");
    }

    @Test
    @OperateOnDeployment("interceptors")
    public void testAlternative(Blackboard blackboard) throws Exception {
        blackboard.print();
    }

}
