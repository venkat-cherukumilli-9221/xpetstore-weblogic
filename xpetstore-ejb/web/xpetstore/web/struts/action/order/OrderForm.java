package xpetstore.web.struts.action.order;

import xpetstore.domain.order.model.OrderPetValue;

import xpetstore.web.struts.action.cart.*;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.form
 *      name="orderForm"
 */
public class OrderForm
    extends CartForm
{
    //~ Instance fields --------------------------------------------------------

    private OrderPetValue _orderValue = new OrderPetValue(  );

    //~ Methods ----------------------------------------------------------------

    public OrderPetValue getOrderValue(  )
    {
        return _orderValue;
    }

    public void setOrderValue( OrderPetValue orderValue )
    {
        _orderValue = orderValue;
    }
}
