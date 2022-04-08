package xpetstore.domain.catalog.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import xpetstore.domain.catalog.model.CategoryValue;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="Category"
 *      type="CMP"
 *      view-type="local"
 *      primkey-field="categoryId"
 *      schema="Category"
 *      cmp-version="${ejb.cmp.version}"
 * @ejb.value-object
 *      name="Category"
 *      match="*"
 * @ejb.transaction
 *      type="Required"
 * @ejb.persistence
 *      table-name="T_CATEGORY"
 * @ejb.finder
 *      signature="Collection findAll()"
 *      query="SELECT OBJECT(c) FROM Category c"
 *
 * @jboss.query
 *      signature="Collection findAll()"
 *      strategy="on-load"
 * @jboss.persistence
 *      create-table="${jboss.create.table}"
 *      remove-table="${jboss.remove.table}"
 */
public abstract class CategoryEJB
    implements EntityBean
{
    //~ Methods ----------------------------------------------------------------

    //==========================================
    // Business methods
    //==========================================

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract CategoryValue getCategoryValue(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setCategoryValue( CategoryValue data );

    //==========================================
    // CMP fields
    //==========================================

    /**
     * @ejb.pk-field
     * @ejb.persistence
     *      column-name="categoryId"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract String getCategoryId(  );

    public abstract void setCategoryId( String CategoryId );

    /**
     * @ejb.persistence
     *      column-name="name"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(50)"
     */
    public abstract String getName(  );

    public abstract void setName( String name );

    /**
     * @ejb.persistence
     *      column-name="description"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(255)"
     */
    public abstract String getDescription(  );

    public abstract void setDescription( String description );

    //==========================================
    // CMR fields
    //==========================================

    /**
     * @ejb.interface-method
     * @ejb.relation
     *      name="category-products"
     *      role-name="category-has-products"
     */
    public abstract Collection getProducts(  );

    public abstract void setProducts( Collection products );

    //==========================================
    //   EJB callbacks
    //==========================================

    /**
     * @ejb.create-method
     */
    public String ejbCreate(  )
        throws CreateException
    {
        return null;
    }

    public void ejbPostCreate(  )
        throws CreateException {}
}
