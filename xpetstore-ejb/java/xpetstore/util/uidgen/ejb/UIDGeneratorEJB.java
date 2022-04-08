package xpetstore.util.uidgen.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import xpetstore.util.uidgen.interfaces.CounterLocal;
import xpetstore.util.uidgen.interfaces.CounterLocalHome;
import xpetstore.util.uidgen.util.CounterUtil;


/**
 * @author <a href="mailto:tchbansi@users.sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="UIDGenerator"
 *      type="Stateless"
 *      view-type="local"
 * @ejb.transaction
 *      type="Required"
 * @ejb.ejb-ref
 *      ejb-name="Counter"
 *      view-type="local"
 * 		ref-name="ejb/CounterLocal"
 */
public abstract class UIDGeneratorEJB
    implements SessionBean
{
    //~ Static fields/initializers ---------------------------------------------

    public static final String EJB_COUNTER = "java:comp/env/ejb/CounterLocal";


    //~ Methods ----------------------------------------------------------------

    //=================================
    // Business Methods
    //=================================

    /**
     * @ejb.interface-method
     */
    public int getUniqueId( String idPrefix )
    {
        return getCounter( idPrefix ).nextValue(  );
    }

    //=================================
    // Misc Method
    //=================================
    private CounterLocal getCounter( String name )
    {
        try
        {
			CounterLocalHome home = null;
            CounterLocal counter = null;
            try
            {
            	home = CounterUtil.getLocalHome();
                counter = home.findByPrimaryKey( name );
            }
            catch ( FinderException fe )
            {
                counter = home.create( name );
            }
            catch( Exception e )
            {
            	throw new EJBException( e );
            }

            return counter;
        }
        catch ( Exception ce )
        {
            throw new EJBException( "Could not create counter " + name + ". Error: " + ce.getMessage(  ) );
        }
    }
}
