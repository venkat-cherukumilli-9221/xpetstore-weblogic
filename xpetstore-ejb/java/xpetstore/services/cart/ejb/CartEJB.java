package xpetstore.services.cart.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import xpetstore.domain.catalog.interfaces.ItemLocal;
import xpetstore.domain.catalog.interfaces.ItemLocalHome;
import xpetstore.domain.catalog.model.ItemValue;
import xpetstore.domain.catalog.model.ProductValue;
import xpetstore.domain.catalog.util.ItemUtil;

import xpetstore.services.cart.model.CartItem;

import xpetstore.util.Debug;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="Cart"
 *      type="Stateful"
 *      view-type="local"
 * @ejb.transaction
 *      type="Required"
 * @ejb.ejb-ref
 *      ejb-name="Item"
 *      view-type="local"
 * 		ref-name="ejb/ItemLocal"
 */
public abstract class CartEJB
    implements SessionBean
{
    //~ Instance fields --------------------------------------------------------

    /** Map of item quantities indexed by itemId */
    private Map _details = new HashMap(  );

    //~ Methods ----------------------------------------------------------------

    //==========================================
    // Business methods
    //==========================================    

    /**
     * @ejb.interface-method
     */
    public void addItem( String itemId )
    {
        addItem( itemId, 1 );
    }

    /**
     * @ejb.interface-method
     */
    public void addItem( String itemId,
                         int    qty )
    {
        Integer curQty = ( Integer ) _details.get( itemId );
        if ( curQty == null )
        {
            _details.put( itemId, new Integer( qty ) );
        }
        else
        {
            _details.put( itemId, new Integer( qty + curQty.intValue(  ) ) );
        }
    }

    /**
     * @ejb.interface-method
     */
    public void removeItem( String itemId )
    {
        _details.remove( itemId );
    }

    /**
     * @ejb.interface-method
     */
    public void updateItems( String itemId[],
                             int    newQty[] )
    {
        for ( int i = 0; i < itemId.length; i++ )
        {
            String id = itemId[ i ];
            int    qty = newQty[ i ];

            if ( _details.containsKey( id ) )
            {
                if ( qty > 0 )
                {
                    _details.put( id, new Integer( qty ) );
                }
            }
            else
            {
                Debug.print( " can't update item[" + id + "]. This item not in the cart" );
            }
        }
    }

    /**
     * @ejb.interface-method
     */
    public int getCount(  )
    {
        return _details.size(  );
    }

    /**
     * @ejb.interface-method
     */
    public double getTotal(  )
    {
        double   ret = 0.0d;
        Iterator it = getCartItems(  ).iterator(  );
        for ( ; it.hasNext(  ); )
        {
            CartItem i = ( CartItem ) it.next(  );
            ret += ( i.getUnitCost(  ) * i.getQuantity(  ) );
        }

        return ret;
    }

    /**
     * @ejb.interface-method
     */
    public void empty(  )
    {
        _details.clear(  );
    }

    /**
     * @return Return a Map of quantities indexed by itemId
     *
     * @ejb.interface-method
     * @ejb.transaction-type
     *      type="NotSupported"
     */
    public Map getDetails(  )
    {
        return _details;
    }

    /**
     * @return Return a list of {@link CartItem} objects
     *
     * @ejb.interface-method
     * @ejb.transaction-type
     *      type="NotSupported"
     */
    public Collection getCartItems(  )
    {
        try
        {
            ItemLocalHome ihome = ItemUtil.getLocalHome(  );
            ArrayList     items = new ArrayList(  );
            Iterator      it = _details.keySet(  ).iterator(  );
            while ( it.hasNext(  ) )
            {
                String  key = ( String ) it.next(  );
                Integer value = ( Integer ) _details.get( key );
                try
                {
                    ItemLocal    ilocal = ihome.findByPrimaryKey( key );
                    ItemValue    item = ilocal.getItemValue(  );
                    ProductValue prod = ilocal.getProduct(  ).getProductValue(  );

                    CartItem     ci = new CartItem( item.getItemId(  ), prod.getProductId(  ), prod.getName(  ), item.getDescription(  ), value.intValue(  ), item.getListPrice(  ) );

                    items.add( ci );
                }
                catch ( Exception cce )
                {
                    cce.printStackTrace(  );
                }
            }

            // Sort the items
            Collections.sort( items, new CartItem.ItemIdComparator(  ) );
            return items;
        }
        catch ( Exception e )
        {
            return Collections.EMPTY_LIST;
        }
    }

    //==========================================
    //   EJB callbacks
    //==========================================

    /**
     * @ejb.create-method
     */
    public void ejbCreate(  )
        throws CreateException {}

    public void ejbPostCreate(  )
        throws CreateException {}
}
