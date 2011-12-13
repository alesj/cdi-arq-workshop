package org.jboss.test.workshop.cdi.inject.support;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;

/**
 * @author Matija Mazi <br/>
 */
public class ScopedProducer implements Serializable {
    @Produces @ApplicationScoped @Wood public Square applicationSquare() {
        return new Square(3);
    }

    @Produces @ConversationScoped @Wood public Square conversationSquare() {
        return new Square(2);
    }

    @Produces @RequestScoped @Wood public Square requestSquare() {
        return new Square(1);
    }
}
