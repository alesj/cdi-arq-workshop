package org.jboss.test.workshop.arquillian.cluster;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SessionScoped
public class Holder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
