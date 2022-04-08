package xpetstore.util.uidgen.ejb;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;


/**
 * @author <a href="mailto:tchbansi@users.sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="Counter"
 *      type="CMP"
 *      view-type="local"
 *      primkey-field="name"
 *      schema="Counter"
 *      cmp-version="${ejb.cmp.version}"
 * @ejb.transaction
 *      type="Required"
 * @ejb.persistence
 *      table-name="T_COUNTER"
 *
 * @jboss.persistence
 *      create-table="${jboss.create.table}"
 *      remove-table="${jboss.remove.table}"
 */
public abstract class CounterEJB
    implements EntityBean
{
    //~ Methods ----------------------------------------------------------------

    //=================================
    // Business methods
    //=================================

    /**
     * @ejb.interface-method
     */
    public int nextValue(  )
    {
        int value = getValue(  ) + 1;
        setValue( value );

        return value;
    }

    //========================================
    // CMP fields
    //========================================

    /**
     * @ejb.pk-field
     * @ejb.persistence
     *      column-name="cnt_name"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(50)"
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public abstract String getName(  );

    public abstract void setName( String name );

    /**
     * @ejb.persistence
     *      column-name="cnt_value"
     */
    public abstract int getValue(  );

    public abstract void setValue( int value );

    //========================================
    // EJB callbacks
    //========================================

    /**
     * @ejb.create-method
     */
    public String ejbCreate( String name )
        throws CreateException
    {
        setName( name );
        setValue( 0 );

        return null;
    }

    public void ejbPostCreate( String name )
        throws CreateException {}
}
