package xpetstore.domain.order.ejb;

import java.rmi.RemoteException;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import xpetstore.domain.catalog.interfaces.ItemLocal;
import xpetstore.domain.catalog.util.ItemUtil;
import xpetstore.domain.customer.interfaces.CustomerLocal;
import xpetstore.domain.order.interfaces.OrderItemLocal;
import xpetstore.domain.order.model.OrderPetValue;
import xpetstore.domain.order.util.OrderItemUtil;

import xpetstore.util.JNDINames;
import xpetstore.util.uidgen.util.UIDGeneratorUtil;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="OrderPet"
 *      type="CMP"
 *      view-type="local"
 *      primkey-field="orderUId"
 *      schema="OrderPet"
 *      cmp-version="${ejb.cmp.version}"
 * @ejb.value-object
 *      name="OrderPet"
 *      match="*"
 * @ejb.transaction
 *      type="Required"
 * @ejb.persistence
 *      table-name="T_ORDER"
 * @ejb.finder
 *      signature="Collection findByCustomer(java.lang.String userId)"
 *      query="SELECT OBJECT(o) FROM OrderPet AS o WHERE o.customer.userId = ?1"
 * @ejb.ejb-ref
 *      ejb-name="OrderItem"
 *      view-type="local"
 * 		ref-name="ejb/OrderItemLocal"
 * @ejb.ejb-ref
 *      ejb-name="UIDGenerator"
 *      view-type="local"
 * 		ref-name="ejb/UIDGeneratorLocal"
 *
 * @jboss.persistence
 *      create-table="${jboss.create.table}"
 *      remove-table="${jboss.remove.table}"
 */
public abstract class OrderPetEJB
    implements EntityBean
{
    //~ Static fields/initializers ---------------------------------------------

    public static final String COUNTER_NAME = "OrderPet";

    //~ Methods ----------------------------------------------------------------

    //==========================================
    // Business methods
    //==========================================

    /**
     * @ejb.interface-method
     */
    public void addItem( String itemId,
                         int    quantity )
        throws FinderException, 
                   CreateException
    {
        try
        {
            ItemLocal      item = ItemUtil.getLocalHome(  ).findByPrimaryKey( itemId );

            OrderItemLocal oitem = OrderItemUtil.getLocalHome(  ).create( quantity, item.getItemValue(  ).getListPrice(  ) );
            oitem.setItem( item );
            getOrderItems(  ).add( oitem );
        }
        catch ( NamingException n )
        {
            throw new CreateException( n.toString(  ) );
        }
    }

    /**
     * @ejb.interface-method
     */
    public void changeStatus( String status )
    {
        setStatus( status );
    }

    /**
     * @ejb.interface-method
     */
    public double getTotal(  )
    {
        double   total = 0;
        Iterator it = getOrderItems(  ).iterator(  );
        while ( it.hasNext(  ) )
        {
            total += ( ( OrderItemLocal ) it.next(  ) ).getSubTotal(  );
        }

        return total;
    }

    /**
     * @ejb.interface-method
     */
    public abstract OrderPetValue getOrderPetValue(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setOrderPetValue( OrderPetValue data );

    //==========================================
    // CMP fields
    //==========================================

    /**
     * @ejb.pk-field
     * @ejb.persistence
     *      column-name="orderUId"
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract Integer getOrderUId(  );

    public abstract void setOrderUId( Integer orderUId );

    /**
     * @ejb.persistence
     *      column-name="orderDate"
     */
    public abstract Date getOrderDate(  );

    public abstract void setOrderDate( Date orderDate );

    /**
     * @ejb.persistence
     *      column-name="status"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(25)"
     */
    public abstract String getStatus(  );

    public abstract void setStatus( String status );

    /**
     * @ejb.persistence
     *      column-name="street1"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(50)"
     */
    public abstract String getStreet1(  );

    public abstract void setStreet1( java.lang.String street1 );

    /**
     * @ejb.persistence
     *      column-name="street2"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(50)"
     */
    public abstract String getStreet2(  );

    public abstract void setStreet2( String street2 );

    /**
     * @ejb.persistence
     *      column-name="city"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(25)"
     */
    public abstract java.lang.String getCity(  );

    public abstract void setCity( String city );

    /**
     * @ejb.persistence
     *      column-name="state"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(3)"
     */
    public abstract String getState(  );

    public abstract void setState( String state );

    /**
     * @ejb.persistence
     *      column-name="zipcode"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     */
    public abstract String getZipcode(  );

    public abstract void setZipcode( String zipcode );

    /**
     * @ejb.persistence
     *      column-name="country"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(3)"
     */
    public abstract java.lang.String getCountry(  );

    public abstract void setCountry( String country );

    /**
     * @ejb.persistence
     *      column-name="creditCardNumber"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(25)"
     */
    public abstract String getCreditCardNumber(  );

    public abstract void setCreditCardNumber( String creditCardNumber );

    /**
     * @ejb.persistence
     *      column-name="creditCardType"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(25)"
     */
    public abstract String getCreditCardType(  );

    public abstract void setCreditCardType( String creditCardType );

    /**
     * @ejb.persistence
     *      column-name="creditCardExpiryDate"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     */
    public abstract String getCreditCardExpiryDate(  );

    public abstract void setCreditCardExpiryDate( String creditCardExpiryDate );

    //==========================================
    // CMR fields
    //==========================================

    /**
     * @ejb.interface-method
     * @ejb.relation
     *      name="order-customer"
     *      role-name="order-belongs_to-customer"
     *      target-ejb="Customer"
     *      target-role-name="customer-has-orders"
     *      target=multiple="yes"
     *
     * @jboss.relation
     *      fk-column="customer_fk"
     *      related-pk-field="userId"
     *
     * @weblogic.column-map
     *      foreign-key-column="customer_fk"
     */
    public abstract CustomerLocal getCustomer(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setCustomer( CustomerLocal customer );

    /**
     * @ejb.interface-method
     * @ejb.relation
     *      name="order-orderItem"
     *      role-name="order-has-orderItems"
     *      target-ejb="OrderItem"
     *      target-role-name="orderItem-belongs_to-order"
     *      target-cascade-delete="yes"
     *
     * @jboss.target-relation
     *      fk-column="order_fk"
     *      related-pk-field="orderUId"
     *      fk-contraint="${db.foreign.key}
     *
     * @weblogic.target-column-map
     *      foreign-key-column="order_fk"
     */
    public abstract Collection getOrderItems(  );

    public abstract void setOrderItems( Collection orderItems );

    //==========================================
    // EJB callbacks
    //==========================================

    /**
     * @ejb.create-method
     */
    public Integer ejbCreate( OrderPetValue data )
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
        
        setOrderUId( new Integer( uid ) );
        setOrderPetValue( data );

        return null;
    }

    public void ejbPostCreate( OrderPetValue data )
        throws CreateException {}
}
