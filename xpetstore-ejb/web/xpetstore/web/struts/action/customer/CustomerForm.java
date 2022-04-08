package xpetstore.web.struts.action.customer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import xpetstore.domain.customer.model.CustomerValue;
import xpetstore.domain.signon.model.AccountValue;

import xpetstore.web.struts.action.*;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.form
 *      name="customerForm"
 */
public class CustomerForm
    extends BaseForm
{
    //~ Instance fields --------------------------------------------------------

    private CustomerValue _customerValue = new CustomerValue(  );

    //~ Constructors -----------------------------------------------------------

    public CustomerForm(  )
    {
        _customerValue.setAccountValue( new AccountValue(  ) );
    }

    //~ Methods ----------------------------------------------------------------

    public ActionErrors validate( ActionMapping      mapping,
                                  HttpServletRequest request )
    {
        ActionErrors errors = new ActionErrors(  );

        AccountValue account = getCustomerValue(  ).getAccountValue(  );
        String       userId = account.getUserId(  );
        String       passwd = account.getPassword(  );
        checkNotEmpty( userId, "userId_required", errors );
        checkLength( userId, 4, "userId_length", errors );
        checkNotEmpty( passwd, "password_required", errors );
        checkLength( passwd, 4, "password_length", errors );

        checkNotEmpty( _customerValue.getEmail(  ), "email_required", errors );
        checkNotEmpty( _customerValue.getCreditCardType(  ), "ccType_required", errors );
        checkNotEmpty( _customerValue.getCreditCardNumber(  ), "ccNumber_required", errors );
        checkNotEmpty( _customerValue.getCreditCardExpiryDate(  ), "ccExpiryDate_required", errors );
        checkCreditCardDateFormat( _customerValue.getCreditCardExpiryDate(  ), "ccExpiryDate_bad_format", errors );

        return errors;
    }

    public CustomerValue getCustomerValue(  )
    {
        return _customerValue;
    }

    public void setCustomerValue( CustomerValue customerValue )
    {
        _customerValue = customerValue;
    }
}
