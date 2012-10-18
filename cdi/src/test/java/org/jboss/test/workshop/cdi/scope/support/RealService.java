package org.jboss.test.workshop.cdi.scope.support;

import org.jboss.seam.solder.core.Veto;

import javax.enterprise.context.SessionScoped;
import java.util.UUID;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SessionScoped
@Veto
public class RealService implements Service {
    private String something = UUID.randomUUID().toString();

    public String doSomething() {
        return something;
    }
}
