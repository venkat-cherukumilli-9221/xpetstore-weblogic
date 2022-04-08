package xpetstore.web.struts.action.item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import xpetstore.domain.catalog.model.ItemValue;
import xpetstore.domain.catalog.model.ProductValue;

import xpetstore.services.petstore.interfaces.PetstoreLocal;

import xpetstore.web.struts.action.BaseAction;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.action
 *      name="itemForm"
 *      path="/item"
 *      scope="request"
 *      validate="false"
 *
 * @struts.action-forward
 *      name="success"
 *      path="/item.jsp"
 */
public class ItemAction
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
        ItemForm      frm = ( ItemForm ) form;
        String        itemId = frm.getItemId(  );
        PetstoreLocal petstore = getPetstore(  );

        /* Item */
        ItemValue item = petstore.getItem( itemId );
        frm.setItemValue( item );

        /* Product */
        ProductValue product = petstore.getProductByItem( itemId );
        frm.setProductValue( product );

        return mapping.findForward( SUCCESS );
    }
}
