package xpetstore.domain.signon.ejb;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import xpetstore.domain.signon.model.AccountValue;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="Account"
 *      type="CMP"
 *      view-type="local"
 *      primkey-field="userId"
 *      schema="Account"
 *      cmp-version="${ejb.cmp.version}"
 * @ejb.value-object
 *      name="Account"
 *      match="*"
 * @ejb.transaction
 *      type="Required"
 * @ejb.persistence
 *      table-name="T_ACCOUNT"
 *
 * @jboss.persistence
 *      create-table="${jboss.create.table}"
 *      remove-table="${jboss.remove.table}"
 */
public abstract class AccountEJB
    implements EntityBean
{
    //~ Methods ----------------------------------------------------------------

    //==========================================
    // Business methods
    //==========================================

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public boolean matchPassword( String password )
    {
        return ( password == null )
               ? ( getPassword(  ) == null )
               : password.equals( getPassword(  ) );
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract AccountValue getAccountValue(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setAccountValue( AccountValue data );

    //==========================================
    // CMP fields
    //==========================================

    /**
     * @ejb.pk-field
     * @ejb.persistence
     *      column-name="userId"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public abstract String getUserId(  );

    public abstract void setUserId( String userId );

    /**
     * @ejb.persistence
     *      column-name="pwd"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     */
    public abstract String getPassword(  );

    public abstract void setPassword( String password );

    //==========================================
    // EJB callbacks
    //==========================================

    /**
     * @ejb.create-method
     */
    public String ejbCreate( AccountValue data )
        throws CreateException
    {
        setUserId( data.getUserId(  ) );
        setAccountValue( data );

        return null;
    }

    public void ejbPostCreate( AccountValue data )
        throws CreateException {}
}
