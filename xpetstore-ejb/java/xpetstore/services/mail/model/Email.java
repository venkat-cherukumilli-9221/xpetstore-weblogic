package xpetstore.services.mail.model;

import java.io.Serializable;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 */
public class Email
    implements Serializable
{
    //~ Instance fields --------------------------------------------------------

    private String to;
    private String subject;
    private String body;

    //~ Constructors -----------------------------------------------------------

    public Email( String to,
                  String subject,
                  String body )
    {
        this.to      = to;
        this.subject = subject;
        this.body    = body;
    }

    //~ Methods ----------------------------------------------------------------

    public String getTo(  )
    {
        return to;
    }

    public String getSubject(  )
    {
        return subject;
    }

    public String getBody(  )
    {
        return body;
    }
}
