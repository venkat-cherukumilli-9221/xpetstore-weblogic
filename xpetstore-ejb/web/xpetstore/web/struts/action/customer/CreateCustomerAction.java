package xpetstore.web.struts.action.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import xpetstore.domain.customer.model.CustomerValue;

import xpetstore.services.petstore.exceptions.DuplicateAccountException;
import xpetstore.services.petstore.exceptions.DuplicateEmailException;

import xpetstore.web.struts.action.BaseAction;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.action
 *      name="customerForm"
 *      path="/createCustomer"
 *      scope="request"
 *      validate="true"
 *      input="/register.jsp"
 *
 * @struts.action-forward
 *      name="success"
 *      path="/signon.jspa"
 *
 * @struts.action-forward
 *      name="error"
 *      path="/register.jsp"
 */
public class CreateCustomerAction
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
        CustomerForm  frm = ( CustomerForm ) form;
        CustomerValue cust = frm.getCustomerValue(  );

        try
        {
            getPetstore(  ).createCustomer( cust );
            return mapping.findForward( SUCCESS );
        }
        catch ( DuplicateAccountException da )
        {
            request.setAttribute( MESSAGE_KEY, "duplicate_account" );

            return mapping.findForward( ERROR );
        }
        catch ( DuplicateEmailException de )
        {
            request.setAttribute( MESSAGE_KEY, "duplicate_email" );

            return mapping.findForward( ERROR );
        }
    }
}
