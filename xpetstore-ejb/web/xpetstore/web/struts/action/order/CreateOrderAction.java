package xpetstore.web.struts.action.order;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import xpetstore.services.cart.interfaces.CartLocal;
import xpetstore.services.petstore.interfaces.PetstoreLocal;

import xpetstore.web.struts.action.BaseAction;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.action
 *      name="orderForm"
 *      path="/createOrder"
 *      scope="request"
 *      validate="false"
 *
 * @struts.action-forward
 *      name="success"
 *      path="/confirmation.jsp"
 */
public class CreateOrderAction
    extends BaseAction
{
    //~ Methods ----------------------------------------------------------------

    /**
     * @see xpetstore.web.struts.action.BaseAction#doExecute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    protected ActionForward doExecute( ActionMapping       mapping,
                                       ActionForm          form,
                                       HttpServletRequest  request,
                                       HttpServletResponse response )
        throws Exception
    {
        OrderForm     frm = ( OrderForm ) form;
        String        userId = ( String ) request.getSession(  ).getAttribute( USERID_KEY );
        PetstoreLocal petstore = getPetstore(  );

        /* Proceed the order */
        HashMap items = new HashMap(  );

        for ( int i = 0, len = frm.getItemId(  ).length; i < len; i++ )
        {
            items.put( frm.getItemId(  )[ i ], new Integer( frm.getQuantity(  )[ i ] ) );
        }

        Integer orderUId = petstore.createOrder( userId, new Date(  ), items );
        frm.getOrderValue(  ).setOrderUId( orderUId );

        /* Invalidating the current cart */
        CartLocal cart = getCart( false, request );

        if ( cart != null )
        {
            request.getSession(  ).removeAttribute( CART_KEY );
            cart.remove(  );
        }

        return mapping.findForward( SUCCESS );
    }
}
