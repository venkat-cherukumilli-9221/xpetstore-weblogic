package xpetstore.web.struts.action.signon;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import xpetstore.domain.signon.model.AccountValue;

import xpetstore.web.struts.action.*;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.form
 *      name="signonForm"
 */
public class SignonForm
    extends BaseForm
{
    //~ Instance fields --------------------------------------------------------

    private AccountValue _accountValue = new AccountValue(  );

    //~ Methods ----------------------------------------------------------------

    public ActionErrors validate( ActionMapping      mapping,
                                  HttpServletRequest request )
    {
        ActionErrors errors = new ActionErrors(  );

        checkNotEmpty( _accountValue.getUserId(  ), "userId_required", errors );
        checkNotEmpty( _accountValue.getPassword(  ), "password_required", errors );

        return errors;
    }

    public AccountValue getAccountValue(  )
    {
        return _accountValue;
    }

    public void setAccountValue( AccountValue accountValue )
    {
        _accountValue = accountValue;
    }
}
