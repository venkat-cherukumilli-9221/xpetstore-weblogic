package xpetstore.domain.order.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;

import xpetstore.domain.catalog.interfaces.ItemLocal;
import xpetstore.domain.order.model.OrderItemValue;

import xpetstore.util.JNDINames;
import xpetstore.util.uidgen.util.UIDGeneratorUtil;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="OrderItem"
 *      type="CMP"
 *      view-type="local"
 *      primkey-field="orderItemUId"
 *      schema="OrderItem"
 *      cmp-version="${ejb.cmp.version}"
 * @ejb.value-object
 *      name="OrderItem"
 *      match="*"
 * @ejb.transaction
 *      type="Required"
 * @ejb.persistence
 *      table-name="T_ORDER_ITEM"
 * @ejb.ejb-ref
 *      ejb-name="UIDGenerator"
 *      view-type="local"
 * 		ref-name="ejb/UIDGeneratorLocal"
 *
 * @jboss.persistence
 *      create-table="${jboss.create.table}"
 *      remove-table="${jboss.remove.table}"
 */
public abstract class OrderItemEJB
    implements EntityBean
{
    //~ Static fields/initializers ---------------------------------------------

    public static final String COUNTER_NAME = "OrderItem";

    //~ Methods ----------------------------------------------------------------

    //==========================================
    // Business methods
    //==========================================    

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public double getSubTotal(  )
    {
        return Math.max( getQuantity(  ) * getUnitPrice(  ), 0 );
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract OrderItemValue getOrderItemValue(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setOrderItemValue( OrderItemValue data );

    //==========================================
    // CMP fields
    //==========================================

    /**
     * @ejb.pk-field
     * @ejb.persistence
     *      column-name="orderItemUId"
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract Integer getOrderItemUId(  );

    public abstract void setOrderItemUId( Integer orderItemUId );

    /**
     * @ejb.persistence
     *      column-name="quantity"
     */
    public abstract int getQuantity(  );

    public abstract void setQuantity( int quantity );

    /**
     * @ejb.persistence
     *      column-name="unitPrice"
     */
    public abstract double getUnitPrice(  );

    public abstract void setUnitPrice( double unitPrice );

    //==========================================
    // CMR fields
    //==========================================

    /**
     * @ejb.interface-method
     * @ejb.relation
     *      name="orderItem-item"
     *      role-name="orderItem-refers_to-item"
     *      cascade-delete="yes"
     *      target-ejb="Item"
     *      target-role-name="item-is_refered_by-orderItems"
     *      target-multiple="yes"
     * @ejb.value-object
     *      aggregate="xpetstore.domain.catalog.model.ItemValue"
     *      aggregate-name="Item"
     *      members="xpetstore.domain.catalog.interfaces.Item"
     *      members-name="Item"
     *      relation="external"
     *
     * @jboss.relation
     *      fk-column="itemId_fk"
     *      related-pk-field="itemId"
     *
     * @weblogic.column-map
     *      foreign-key-column="itemId_fk"
     */
    public abstract ItemLocal getItem(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setItem( ItemLocal item );

    //==========================================
    // EJB callbacks
    //==========================================

    /**
     * @ejb.create-method
     */
    public Integer ejbCreate( int    quantity,
                              double unitPrice )
        throws CreateException
    {
		int uid = 0;
		try
		{
			uid = UIDGeneratorUtil.getLocalHome().create().getUniqueId(COUNTER_NAME);
		}
		catch( Exception e )
		{
			throw new EJBException( e );
		}
		
        setOrderItemUId( new Integer( uid ) );
        setQuantity( quantity );
        setUnitPrice( unitPrice );
        return null;
    }

    public void ejbPostCreate( int    quantity,
                               double unitPrice )
        throws CreateException {}
}
