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

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteScopedContext implements Context {
    private RemoteRegistry registry = new RemoteRegistry();

    public Class<? extends Annotation> getScope() {
        return RemoteScoped.class;
    }

    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        T result = (T) registry.get(contextual);
        if (result == null && creationalContext != null) {
            result = contextual.create(creationalContext);
            registry.put(contextual, result);
        }
        return result;
    }

    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    public boolean isActive() {
        return true;
    }

    public static Class<?> getBusinessInterface(Class<?> beanClass) {
        RemoteScoped rs = beanClass.getAnnotation(RemoteScoped.class);
        if (rs == null)
            throw new IllegalArgumentException("Missing @MontereyScoped annotation: " + beanClass);

        Class<?> iface = rs.value();
        if (iface.equals(void.class) == false) {
            if (iface.isInterface() == false)
                throw new IllegalArgumentException("Remote interface is not an interface: " + iface);
            return iface;
        }

        Set<Class<?>> ifaces = new HashSet<Class<?>>();
        check(beanClass, ifaces);

        if (ifaces.size() == 1)
            return ifaces.iterator().next();

        throw new IllegalArgumentException("Too many interfaces on a bean: " + beanClass + ", interfaces: " + ifaces);
    }

    private static void check(Class<?> current, Set<Class<?>> ifaces) {
        if (current == Object.class)
            return;

        ifaces.addAll(Arrays.asList(current.getInterfaces()));
        check(current.getSuperclass(), ifaces);
    }
}