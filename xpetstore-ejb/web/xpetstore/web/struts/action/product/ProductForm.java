package xpetstore.web.struts.action.product;

import java.util.Collection;

import xpetstore.domain.catalog.model.ProductValue;

import xpetstore.web.struts.action.BaseForm;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.form
 *      name="productForm"
 */
public class ProductForm
    extends BaseForm
{
    //~ Instance fields --------------------------------------------------------

    private String       _productId;
    private ProductValue _productValue = new ProductValue(  );
    private Collection   _itemValues;

    //~ Methods ----------------------------------------------------------------

    /**
     * @return Collection
     */
    public Collection getItemValues(  )
    {
        return _itemValues;
    }

    /**
     * @return String
     */
    public String getProductId(  )
    {
        return _productId;
    }

    /**
     * @return ProductValue
     */
    public ProductValue getProductValue(  )
    {
        return _productValue;
    }

    /**
     * Sets the itemValues.
     * @param itemValues The itemValues to set
     */
    public void setItemValues( Collection itemValues )
    {
        _itemValues = itemValues;
    }

    /**
     * Sets the productId.
     * @param productId The productId to set
     */
    public void setProductId( String productId )
    {
        _productId = productId;
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
