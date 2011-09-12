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

package org.jboss.test.workshop.cdi.observes.support;

import javax.enterprise.event.Event;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Producer {
    @Inject
    private Event<Msg> generic;
    @Inject
    private Event<Msg<String>> texts;

    public void sendText(String text) {
        TextMsg msg = new TextMsg(text);
        generic.select(new TypeLiteral<Msg<String>>() {
        }).fire(msg);
        texts.fire(msg);
    }

    public void sendBytes(byte[] value) {
        BytesMsg msg = new BytesMsg(value);
        generic.select(new TypeLiteral<Msg<byte[]>>() {
        }).fire(msg);
    }

    public void sendNumber(Number x) {
        NumberMsg msg = new NumberMsg(x);
        generic.select(new TypeLiteral<Msg<Number>>() {
        }).fire(msg);
    }

    public void sendSecure(String text) {
        texts.select(new AnnotationLiteral<Secure>() {
        }).fire(new TextMsg(text));
    }

}
