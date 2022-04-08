package xpetstore.web.struts.action.cart;

import java.util.Collection;

import xpetstore.domain.customer.model.CustomerValue;

import xpetstore.web.struts.action.*;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.form
 *      name="cartForm"
 */
public class CartForm
    extends BaseForm
{
    //~ Instance fields --------------------------------------------------------

    private Collection    _cartItems;
    private CustomerValue _customerValue;
    private String        _itemId[];
    private int           _quantity[];
    public double         _total;

    //~ Methods ----------------------------------------------------------------

    public Collection getCartItems(  )
    {
        return _cartItems;
    }

    /**
     * @return CustomerValue
     */
    public CustomerValue getCustomerValue(  )
    {
        return _customerValue;
    }

    public String[] getItemId(  )
    {
        return _itemId;
    }

    public int[] getQuantity(  )
    {
        return _quantity;
    }

    public double getTotal(  )
    {
        return _total;
    }

    public void setCartItems( Collection cartItems )
    {
        _cartItems = cartItems;
    }

    /**
     * Sets the customer.
     * @param customer The customer to set
     */
    public void setCustomerValue( CustomerValue customer )
    {
        _customerValue = customer;
    }

    public void setItemId( String itemId[] )
    {
        _itemId = itemId;
    }

    public void setQuantity( int quantity[] )
    {
        _quantity = quantity;
    }

    public void setTotal( double total )
    {
        _total = total;
    }
}
