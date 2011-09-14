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

package org.jboss.test.workshop.cdi.extensions.support;

import org.jboss.seam.solder.bean.BeanBuilder;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.util.Stack;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteExtension implements Extension {

    private Stack<AnnotatedType<?>> beans = new Stack<AnnotatedType<?>>();

    public void processBeans(@Observes final ProcessAnnotatedType<?> event, final BeanManager manager) {
        AnnotatedType annotated = event.getAnnotatedType();
        if (annotated.isAnnotationPresent(RemoteScoped.class)) {
            beans.add(annotated);
            event.veto();
        }
    }

    @SuppressWarnings({"unchecked"})
    public void registerContext(@Observes final AfterBeanDiscovery event, final BeanManager manager) {
        event.addContext(new RemoteScopedContext());

        while (beans.empty() == false) {
            AnnotatedType<?> at = beans.pop();
            BeanBuilder<?> builder = new BeanBuilder(manager).readFromType(at);
            builder.types(RemoteScopedContext.getBusinessInterface((Class<?>) at.getBaseType()));
            event.addBean(builder.create());
        }
    }

}
