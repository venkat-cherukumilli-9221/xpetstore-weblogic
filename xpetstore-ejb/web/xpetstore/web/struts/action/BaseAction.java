package xpetstore.web.struts.action;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import xpetstore.domain.customer.model.CustomerValue;

import xpetstore.services.cart.interfaces.CartLocal;
import xpetstore.services.cart.interfaces.CartLocalHome;
import xpetstore.services.cart.util.CartUtil;
import xpetstore.services.petstore.interfaces.PetstoreLocal;
import xpetstore.services.petstore.util.PetstoreUtil;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 */
public abstract class BaseAction
    extends Action
{
    //~ Static fields/initializers ---------------------------------------------

    public static final String BUNDLE_NAME = "Resources";
    public static final String CART_KEY = "cart";
    public static final String ERROR = "error";
    public static final String MESSAGE_KEY = "message";
    public static final String SUCCESS = "success";
    public static final String USERID_KEY = "userId";
    public static final String USERNAME_KEY = "username";

    //~ Instance fields --------------------------------------------------------

    protected Log _log = LogFactory.getLog( getClass(  ) );

    //~ Methods ----------------------------------------------------------------

    protected void clearSession( HttpServletRequest request )
        throws Exception
    {
        HttpSession session = request.getSession(  );

        session.removeAttribute( USERID_KEY );
        session.removeAttribute( USERNAME_KEY );

        CartLocal cart = getCart( false, request );

        if ( cart != null )
        {
            session.removeAttribute( CART_KEY );
            cart.remove(  );
        }
    }

    protected abstract ActionForward doExecute( ActionMapping       mapping,
                                                ActionForm          form,
                                                HttpServletRequest  request,
                                                HttpServletResponse response )
        throws Exception;

    /**
     *
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute( ActionMapping       mapping,
                                  ActionForm          form,
                                  HttpServletRequest  request,
                                  HttpServletResponse response )
        throws Exception
    {
        _log.info( "execute()" );

        return doExecute( mapping, form, request, response );
    }

    public CartLocal getCart( boolean            create,
                              HttpServletRequest request )
        throws Exception
    {
        HttpSession session = request.getSession(  );
        CartLocal   cart = ( CartLocal ) session.getAttribute( CART_KEY );
        if ( ( cart == null ) && create )
        {
            cart = CartUtil.getLocalHome().create();
            session.setAttribute( CART_KEY, cart );
        }

        return cart;
    }

    public CartLocal getCart( HttpServletRequest request )
        throws Exception
    {
        return getCart( true, request );
    }

    public PetstoreLocal getPetstore(  )
        throws Exception
    {
        return PetstoreUtil.getLocalHome().create(  );
    }

    public String getString( String key )
    {
        try
        {
            return ResourceBundle.getBundle( BUNDLE_NAME ).getString( key );
        }
        catch ( Exception e )
        {
            return "???" + key + "???";
        }
    }

    protected void initSession( CustomerValue      customer,
                                HttpServletRequest request )
    {
        HttpSession session = request.getSession(  );
        session.setAttribute( USERID_KEY, customer.getUserId(  ) );
        session.setAttribute( USERNAME_KEY, customer.getFirstname(  ) + " " + customer.getLastname(  ) );
    }
}
