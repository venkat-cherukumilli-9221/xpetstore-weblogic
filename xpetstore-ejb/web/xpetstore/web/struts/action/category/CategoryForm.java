package xpetstore.web.struts.action.category;

import java.util.Collection;

import xpetstore.domain.catalog.model.CategoryValue;

import xpetstore.web.struts.action.*;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @struts.form
 *      name="categoryForm"
 */
public class CategoryForm
    extends BaseForm
{
    //~ Instance fields --------------------------------------------------------

    private String        _categoryId = "";
    private CategoryValue _categoryValue = new CategoryValue(  );
    private Collection    _productValues;

    //~ Methods ----------------------------------------------------------------

    /**
     * @return String
     */
    public String getCategoryId(  )
    {
        return _categoryId;
    }

    /**
     * @return CategoryValue
     */
    public CategoryValue getCategoryValue(  )
    {
        return _categoryValue;
    }

    /**
     * @return Collection
     */
    public Collection getProductValues(  )
    {
        return _productValues;
    }

    /**
     * Sets the categoryId.
     * @param categoryId The categoryId to set
     */
    public void setCategoryId( String categoryId )
    {
        _categoryId = categoryId;
    }

    /**
     * Sets the categoryValue.
     * @param categoryValue The categoryValue to set
     */
    public void setCategoryValue( CategoryValue categoryValue )
    {
        _categoryValue = categoryValue;
    }

    /**
     * Sets the productValues.
     * @param productValues The productValues to set
     */
    public void setProductValues( Collection productValues )
    {
        _productValues = productValues;
    }
}
