package xpetstore.util;


/**
 * This class contains the JNDI names of all the resources uses by
 * the components of the service layer
 *
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 */
public abstract class JNDINames
{
    //~ Static fields/initializers ---------------------------------------------

    public static final String QUEUE_CONNECTION_FACTORY = "java:comp/env/jms/xpetstore/QueueConnectionFactory";
    public static final String QUEUE_ORDER = "java:comp/env/jms/queue/xpetstore/order";
    public static final String QUEUE_MAIL = "java:comp/env/jms/queue/xpetstore/mail";
    public static final String MAIL_SESSION = "java:comp/env/mail/xpetstore/MailSession";
    public static final String JDBC_DATASOURCE = "java:comp/env/jdbc/xpetstore";
}
