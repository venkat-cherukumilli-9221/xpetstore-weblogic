package xpetstore.domain.catalog.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import xpetstore.domain.catalog.interfaces.CategoryLocal;
import xpetstore.domain.catalog.model.ProductValue;
import weblogic.logging.NonCatalogLogger;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="Product"
 *      type="CMP"
 *      view-type="local"
 *      primkey-field="productId"
 *      schema="Product"
 *      cmp-version="${ejb.cmp.version}"
 * @ejb.value-object
 *      name="Product"
 *      match="*"
 * @ejb.transaction
 *      type="Required"
 * @ejb.persistence
 *      table-name="T_PRODUCT"
 *
 * @jboss.persistence
 *      create-table="${jboss.create.table}"
 *      remove-table="${jboss.remove.table}"
 */
public abstract class ProductEJB
    implements EntityBean
{

    private String ipAddress1 = "192.121.10.1";
    private String ipAddress2 = "t3s://172.121.101.21";
    NonCatalogLogger logger = new NonCatalogLogger("Weblogic Log");
    //~ Methods ----------------------------------------------------------------

    //==========================================
    // Business methods
    //==========================================

    /**
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract ProductValue getProductValue(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setProductValue( ProductValue data );

    //==========================================
    // CMP fields
    //==========================================

    /**
     * @ejb.pk-field
     * @ejb.persistence
     *      column-name="productId"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract String getProductId(  );

    public abstract void setProductId( String ProductId );

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
     *      name="product-items"
     *      role-name="product-has-items"
     */
    public abstract Collection getItems(  );

    public abstract void setItems( Collection items );

    /**
     * @ejb.interface-method
     * @ejb.relation
     *      name="category-products"
     *      role-name="product-belongs_to-category"
     *      cascade-delete="yes"
     *
     * @jboss.relation
     *      fk-column="category_fk"
     *      related-pk-field="categoryId"
     *      fk-contraint="${db.foreign.key}"
     *
     * @weblogic.column-map
     *      foreign-key-column="category_fk"
     */
    public abstract CategoryLocal getCategory(  );

    public abstract void setCategory( CategoryLocal category );

    //==========================================
    // EJB callbacks
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
