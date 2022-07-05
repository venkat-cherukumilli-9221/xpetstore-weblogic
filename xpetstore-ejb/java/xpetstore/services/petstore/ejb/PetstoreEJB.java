package xpetstore.services.petstore.ejb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import xpetstore.domain.catalog.dao.ProductDAO;
import xpetstore.domain.catalog.interfaces.CategoryLocal;
import xpetstore.domain.catalog.interfaces.CategoryLocalHome;
import xpetstore.domain.catalog.interfaces.ItemLocal;
import xpetstore.domain.catalog.interfaces.ItemLocalHome;
import xpetstore.domain.catalog.interfaces.ProductLocal;
import xpetstore.domain.catalog.interfaces.ProductLocalHome;
import xpetstore.domain.catalog.model.CategoryValue;
import xpetstore.domain.catalog.model.ItemValue;
import xpetstore.domain.catalog.model.ProductValue;
import xpetstore.domain.catalog.util.CategoryUtil;
import xpetstore.domain.catalog.util.ItemUtil;
import xpetstore.domain.catalog.util.ProductUtil;
import xpetstore.domain.customer.interfaces.CustomerLocal;
import xpetstore.domain.customer.interfaces.CustomerLocalHome;
import xpetstore.domain.customer.model.CustomerValue;
import xpetstore.domain.customer.util.CustomerUtil;
import xpetstore.domain.order.interfaces.OrderItemLocal;
import xpetstore.domain.order.interfaces.OrderPetLocal;
import xpetstore.domain.order.interfaces.OrderPetLocalHome;
import xpetstore.domain.order.model.OrderItemValue;
import xpetstore.domain.order.model.OrderStatus;
import xpetstore.domain.order.model.OrderPetValue;
import xpetstore.domain.order.util.OrderPetUtil;
import xpetstore.domain.signon.interfaces.AccountLocal;
import xpetstore.domain.signon.interfaces.AccountLocalHome;
import xpetstore.domain.signon.model.AccountValue;
import xpetstore.domain.signon.util.AccountUtil;

import xpetstore.services.petstore.exceptions.CartEmptyOrderException;
import xpetstore.services.petstore.exceptions.DuplicateAccountException;
import xpetstore.services.petstore.exceptions.DuplicateEmailException;

import xpetstore.util.JMSUtil;
import xpetstore.util.JNDINames;
import xpetstore.util.Page;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="Petstore"
 *      type="Stateless"
 *      view-type="local"
 * @ejb.transaction
 *      type="Required"
 * @ejb.ejb-ref
 *      ejb-name="Category"
 *      view-type="local"
 * 		ref-name="ejb/CategoryLocal"
 * @ejb.ejb-ref
 *      ejb-name="Item"
 *      view-type="local"
 * 		ref-name="ejb/ItemLocal"
 * @ejb.ejb-ref
 *      ejb-name="Product"
 *      view-type="local"
 * 		ref-name="ejb/ProductLocal"
 * @ejb.ejb-ref
 *      ejb-name="Customer"
 *      view-type="local"
 * 		ref-name="ejb/CustomerLocal"
 * @ejb.ejb-ref
 *      ejb-name="Account"
 *      view-type="local"
 * 		ref-name="ejb/AccountLocal"
 * @ejb.ejb-ref
 *      ejb-name="OrderPet"
 *      view-type="local"
 * 		ref-name="ejb/OrderPetLocal"
 * @ejb.resource-ref
 *      res-ref-name="${jndi.queue.ConnectionFactory}"
 *      res-type="javax.jms.QueueConnectionFactory"
 *      res-auth="Container"
 * 		jndi-name="${orion.queue.ConnectionFactory}"
 * @ejb.resource-ref
 *      res-ref-name="${jndi.queue.order}"
 *      res-type="javax.jms.Queue"
 *      res-auth="Container"
 * 		jndi-name="${orion.queue.order}"
 * @ejb.resource-ref
 *      res-ref-name="${jndi.datasource}"
 *      res-type="javax.sql.DataSource"
 *      res-auth="Container"
 * 		jndi-name="${orion.datasource}"
 *
 * @jboss.resource-ref
 *      res-ref-name="${jndi.queue.ConnectionFactory}"
 *      jndi-name="${jboss.queue.ConnectionFactory}"
 * @jboss.resource-ref
 *      res-ref-name="${jndi.queue.order}"
 *      jndi-name="${jboss.queue.order}"
 * @jboss.resource-ref
 *      res-ref-name="${jndi.datasource}"
 *      jndi-name="${jboss.datasource}"
 *
 * @weblogic.resource-description
 *      res-ref-name="${jndi.queue.ConnectionFactory}"
 *      jndi-name="${weblogic.queue.ConnectionFactory}"
 * @weblogic.resource-description
 *      res-ref-name="${jndi.queue.order}"
 *      jndi-name="${weblogic.queue.order}"
 * @weblogic.resource-description
 *      res-ref-name="${jndi.datasource}"
 *      jndi-name="${weblogic.datasource}"
 */
public abstract class PetstoreEJB
    implements SessionBean
{
    //~ Methods ----------------------------------------------------------------

	public void readFile() {
    	String filePath = "D:/usr/randoop-4.3.0/junits-output/xpetstore/RegressionTest.java";
    	FileReader fr=null;
    	try
    	{
    	    fr = new FileReader(filePath);
    	    new URI("file://randoop-4.3.0/junits-output/xpetstore/RegressionTest.java");
    	}
    	catch (URISyntaxException e)
    	{
    	    System.out.println("File not found");
    	} catch (FileNotFoundException fe)
    	{
    	    System.out.println("File not found");
    	}
    }
	
    //===============================================
    // Signon business methods
    //===============================================

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     * @ejb.transaction
     *      type="NotSupported"
     */
    public boolean authenticate( String userId,
                                 String password )
    {
        try
        {
            AccountLocal act = getAccountLocalHome(  ).findByPrimaryKey( userId );
            return act.matchPassword( password );
        }
        catch ( FinderException f )
        {
            return false;
        }
    }

    private AccountLocalHome getAccountLocalHome(  )
    {
        try
        {
            return AccountUtil.getLocalHome(  );
        }
        catch ( NamingException n )
        {
            throw new EJBException( n );
        }
    }

    //===============================================
    // Catalog business methods
    //===============================================

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public CategoryValue getCategory( String categoryId )
        throws FinderException
    {
        return getCategoryLocalHome(  ).findByPrimaryKey( categoryId ).getCategoryValue(  );
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public Page getCategories( int start,
                               int count )
    {
        try
        {
            return toPage( getCategoryLocalHome(  ).findAll(  ), start, count, CategoryValue.class );
        }
        catch ( FinderException f )
        {
            throw new EJBException( f );
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public ProductValue getProduct( String productId )
        throws FinderException
    {
        return getProductLocalHome(  ).findByPrimaryKey( productId ).getProductValue(  );
    }

    /**
     * @ejb.interface-method
     */
    public ProductValue getProductByItem( String itemId )
        throws FinderException
    {
        ItemLocal item = getItemLocalHome(  ).findByPrimaryKey( itemId );
        return item.getProduct(  ).getProductValue(  );
    }

    /**
     * @throws FinderException if the category not found
     *
     * @ejb.interface-method
     */
    public Page getProducts( String categoryId,
                             int    start,
                             int    count )
        throws FinderException
    {
        try
        {
            CategoryLocal cat = CategoryUtil.getLocalHome(  ).findByPrimaryKey( categoryId );
            return toPage( cat.getProducts(  ), start, count, ProductValue.class );
        }
        catch ( NamingException n )
        {
            throw new EJBException( n );
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public Page searchProducts( String key,
                                int    start,
                                int    count )
    {
    	Connection cnn = null;
        try
        {
        	cnn = getConnection(  );
            ProductDAO dao = new ProductDAO( cnn );
            return dao.findByKey( "%" + key + "%", start, count );
        }
        catch ( SQLException sql )
        {
            throw new EJBException( sql );
        }
        finally
        {
        	if ( cnn != null )
        	{
        		try
        		{
        			cnn.close();
        		}
        		catch( Exception e )
        		{
        			throw new EJBException( "Unable to close the connection", e );
        		}
        	}
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public ItemValue getItem( String itemId )
        throws FinderException
    {
        return getItemLocalHome(  ).findByPrimaryKey( itemId ).getItemValue(  );
    }

    /**
     * @throws FinderException if the product not found
     *
     * @ejb.interface-method
     */
    public Page getItems( String productId,
                          int    start,
                          int    count )
        throws FinderException
    {
        try
        {
            ProductLocal prod = ProductUtil.getLocalHome(  ).findByPrimaryKey( productId );
            return toPage( prod.getItems(  ), start, count, ItemValue.class );
        }
        catch ( NamingException n )
        {
            throw new EJBException( n );
        }
    }

    private CategoryLocalHome getCategoryLocalHome(  )
    {
        try
        {
            return CategoryUtil.getLocalHome(  );
        }
        catch ( NamingException n )
        {
            throw new EJBException( n );
        }
    }

    private ProductLocalHome getProductLocalHome(  )
    {
        try
        {
            return ProductUtil.getLocalHome(  );
        }
        catch ( NamingException n )
        {
            throw new EJBException( n );
        }
    }

    private ItemLocalHome getItemLocalHome(  )
    {
        try
        {
            return ItemUtil.getLocalHome(  );
        }
        catch ( NamingException n )
        {
            throw new EJBException( n );
        }
    }

    private Connection getConnection(  )
    {
        try
        {
            InitialContext ic = new InitialContext(  );
            DataSource     ds = ( DataSource ) ic.lookup( JNDINames.JDBC_DATASOURCE );
            return ds.getConnection(  );
        }
        catch ( Exception e )
        {
            throw new EJBException(  );
        }
    }

    //===============================================
    // Customer business methods
    //===============================================

    /**
     * @ejb.interface-method
     */
    public String createCustomer( CustomerValue customer )
        throws DuplicateAccountException, 
                   DuplicateEmailException, 
                   CreateException
    {
        /* Make sure that the customer email is unique */
        CustomerLocalHome home = getCustomerLocalHome(  );
        try
        {
            String email = customer.getEmail(  );
            home.findByEmail( email );
            throw new DuplicateEmailException( email );
        }
        catch ( FinderException f )
        {
            /* create the account */
            AccountLocal act = null;
            AccountValue account = customer.getAccountValue(  );
            try
            {
                act = getAccountLocalHome(  ).create( account );
            }
            catch ( DuplicateKeyException d )
            {
                throw new DuplicateAccountException( account.getUserId(  ), d );
            }

            /* create the customer */
            CustomerLocal cst = home.create( act, customer );
            return cst.getUserId(  );
        }
    }

    /**
     * @ejb.interface-method
     */
    public void updateCustomer( CustomerValue customer )
        throws DuplicateEmailException, 
                   FinderException
    {
        /* Make sure that the email is unique */
        CustomerLocalHome home = getCustomerLocalHome(  );
        CustomerLocal     cst = null;
        try
        {
            String email = customer.getEmail(  );
            cst = home.findByEmail( email );
            if ( ( cst != null ) && !cst.getUserId(  ).equals( customer.getUserId(  ) ) )
            {
                throw new DuplicateEmailException( email );
            }
        }
        catch ( FinderException f ) {}

        /* Update the customer */
        if ( cst == null )
        {
            cst = home.findByPrimaryKey( customer.getUserId(  ) );
        }

        cst.setCustomerValue( customer );

        /* Update the account */
        AccountValue account = customer.getAccountValue(  );
        if ( ( account != null ) && ( account.getPassword(  ) != null ) && ( account.getPassword(  ).length(  ) > 0 ) )
        {
            cst.getAccount(  ).setAccountValue( account );
        }
    }

    /**
     * @ejb.interface-method
     */
    public CustomerValue getCustomer( String userId )
        throws FinderException
    {
        return getCustomerLocalHome(  ).findByPrimaryKey( userId ).getCustomerValue(  );
    }

    private CustomerLocalHome getCustomerLocalHome(  )
    {
        try
        {
            return CustomerUtil.getLocalHome(  );
        }
        catch ( NamingException n )
        {
            throw new EJBException( n );
        }
    }

    //===============================================
    // Order business methods
    //===============================================

    /**
     * @param userId        Id of the customer
     * @param orderDate    Creation date of the order
     * @param items        <code>java.lang.Map</code> containing the items
     *                      ordered. The key is the itemId, and the value
     *                      a </code>java.lang.Integer</code> representing the
     *                      quantity ordered
     *
     * @ejb.interface-method
     */
    public Integer createOrder( String userId,
                                Date   orderDate,
                                Map    items )
        throws CartEmptyOrderException, 
                   FinderException, 
                   CreateException
    {
        /* Make sure that the cart is not empty */
        if ( items.size(  ) == 0 )
        {
            throw new CartEmptyOrderException(  );
        }

        /* Get the customer */
        CustomerLocal cst = getCustomerLocalHome(  ).findByPrimaryKey( userId );
        CustomerValue cstData = cst.getCustomerValue(  );

        /* Create the order */
        OrderPetValue oData = new OrderPetValue(  );
        oData.setOrderDate( orderDate );
        oData.setStatus( OrderStatus.PENDING );
        oData.setStreet1( cstData.getStreet1(  ) );
        oData.setStreet2( cstData.getStreet2(  ) );
        oData.setCity( cstData.getCity(  ) );
        oData.setState( cstData.getState(  ) );
        oData.setZipcode( cstData.getZipcode(  ) );
        oData.setCountry( cstData.getCountry(  ) );
        oData.setCreditCardNumber( cstData.getCreditCardNumber(  ) );
        oData.setCreditCardType( cstData.getCreditCardType(  ) );
        oData.setCreditCardExpiryDate( cstData.getCreditCardExpiryDate(  ) );

        OrderPetLocalHome ohome = getOrderLocalHome(  );
        OrderPetLocal     order = ohome.create( oData );
        order.setCustomer( cst );

        /* Add the items */
        Iterator keys = items.keySet(  ).iterator(  );
        while ( keys.hasNext(  ) )
        {
            String  itemId = ( String ) keys.next(  );
            Integer qty = ( Integer ) items.get( itemId );
            try
            {
                order.addItem( itemId, qty.intValue(  ) );
            }
            catch ( FinderException f )
            {
                f.printStackTrace(  );
            }
        }

        /* Process the item ansynchronously */
        try
        {
            JMSUtil.sendToJMSQueue( JNDINames.QUEUE_ORDER, order.getOrderUId(  ), false );
        }
        catch ( Exception e )
        {
            throw new EJBException( e );
        }

        return order.getOrderUId(  );
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public Page getCustomerOrders( String userId,
                                   int    start,
                                   int    count )
    {
        try
        {
            Collection col = getOrderLocalHome(  ).findByCustomer( userId );
            return toPage( col, start, count, OrderPetValue.class );
        }
        catch ( FinderException f )
        {
            throw new EJBException( f );
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="NotSupported"
     */
    public OrderPetValue getOrder( Integer orderUId )
        throws FinderException
    {
        return getOrderLocalHome(  ).findByPrimaryKey( orderUId ).getOrderPetValue(  );
    }

    /**
     * @ejb.interface-method
     */
    public Page getOrderItems( Integer orderUId,
                               int     start,
                               int     count )
    {
        Collection col;
        try
        {
            OrderPetLocal order = getOrderLocalHome(  ).findByPrimaryKey( orderUId );
            col = order.getOrderItems(  );
        }
        catch ( FinderException f )
        {
            col = new ArrayList(  );
        }

        return toPage( col, start, count, OrderItemValue.class );
    }

    private OrderPetLocalHome getOrderLocalHome(  )
    {
        try
        {
            return OrderPetUtil.getLocalHome(  );
        }
        catch ( NamingException n )
        {
            throw new EJBException( n );
        }
    }

    //=================================
    // Misc Methods
    //=================================
    private Page toPage( Collection col,
                         int        start,
                         int        count,
                         Class      type )
    {
        int size = col.size(  );
        if ( size == 0 )
        {
            return Page.EMPTY_PAGE;
        }

        ArrayList lst = new ArrayList(  );
        Iterator  it = col.iterator(  );
        for ( int i = 0, imax = start + count; ( i < imax ) && it.hasNext(  );
              i++ )
        {
            Object obj = it.next(  );
            if ( i >= start )
            {
                lst.add( getData( obj, type ) );
            }
        }

        return new Page( lst, start, ( start + count ) < size );
    }

    private Object getData( Object obj,
                            Class  type )
    {
        if ( type == CategoryValue.class )
        {
            return ( ( CategoryLocal ) obj ).getCategoryValue(  );
        }
        else if ( type == ProductValue.class )
        {
            return ( ( ProductLocal ) obj ).getProductValue(  );
        }
        else if ( type == ItemValue.class )
        {
            return ( ( ItemLocal ) obj ).getItemValue(  );
        }
        else if ( type == OrderPetValue.class )
        {
            return ( ( OrderPetLocal ) obj ).getOrderPetValue(  );
        }
        else if ( type == OrderItemValue.class )
        {
            return ( ( OrderItemLocal ) obj ).getOrderItemValue(  );
        }
        else
        {
            throw new EJBException( "Invalid data type: " + type );
        }
    }

    //==========================================
    //  EJB callbacks
    //==========================================

    /**
     * @ejb.create-method
     */
    public void ejbCreate(  )
        throws CreateException {}

    public void ejbPostCreate(  )
        throws CreateException {}
}
