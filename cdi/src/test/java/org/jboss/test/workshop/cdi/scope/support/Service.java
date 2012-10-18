package org.jboss.test.workshop.cdi.scope.support;

import java.io.Serializable;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Service extends Serializable {
    String doSomething();
}
