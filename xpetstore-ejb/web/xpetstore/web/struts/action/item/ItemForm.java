package xpetstore.web.struts.action.item;

import xpetstore.domain.catalog.model.ItemValue;
import xpetstore.domain.catalog.model.ProductValue;

import xpetstore.web.struts.action.*;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.form
 *      name="itemForm"
 */
public class ItemForm
    extends BaseForm
{
    //~ Instance fields --------------------------------------------------------

    private String       _itemId;
    private ItemValue    _itemValue = new ItemValue(  );
    private ProductValue _productValue;

    //~ Methods ----------------------------------------------------------------

    /**
     * @return String
     */
    public String getItemId(  )
    {
        return _itemId;
    }

    /**
     * @return ItemValue
     */
    public ItemValue getItemValue(  )
    {
        return _itemValue;
    }

    /**
     * @return ProductValue
     */
    public ProductValue getProductValue(  )
    {
        return _productValue;
    }

    /**
     * Sets the itemId.
     * @param itemId The itemId to set
     */
    public void setItemId( String itemId )
    {
        _itemId = itemId;
    }

    /**
     * Sets the itemValue.
     * @param itemValue The itemValue to set
     */
    public void setItemValue( ItemValue itemValue )
    {
        _itemValue = itemValue;
    }

    /**
     * Sets the productValue.
     * @param productValue The productValue to set
     */
    public void setProductValue( ProductValue productValue )
    {
        _productValue = productValue;
    }
}
