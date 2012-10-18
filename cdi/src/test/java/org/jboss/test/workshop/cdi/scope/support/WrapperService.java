package org.jboss.test.workshop.cdi.scope.support;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SessionScoped
public class WrapperService implements Service {
    private volatile Service delegate;

    @Inject
    private BeanManager manager;

    public String doSomething() {
        return getDelegate().doSomething();
    }

    private Service getDelegate() {
        if (delegate == null) {
            // TODO
        }
        return delegate;
    }
}
