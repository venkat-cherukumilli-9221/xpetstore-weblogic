package xpetstore.domain.catalog.ejb;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import xpetstore.domain.catalog.interfaces.ProductLocal;
import xpetstore.domain.catalog.model.ItemValue;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="Item"
 *      type="CMP"
 *      view-type="local"
 *      primkey-field="itemId"
 *      schema="Item"
 *      cmp-version="${ejb.cmp.version}"
 * @ejb.value-object
 *      name="Item"
 *      match="*"
 * @ejb.transaction
 *      type="Required"
 * @ejb.persistence
 *      table-name="T_ITEM"
 *
 * @jboss.persistence
 *      create-table="${jboss.create.table}"
 *      remove-table="${jboss.remove.table}"
 */
public abstract class ItemEJB
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
    public abstract ItemValue getItemValue(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setItemValue( ItemValue data );

    //==========================================
    // CMP fields
    //==========================================

    /**
     * @ejb.pk-field
     * @ejb.persistence
     *      column-name="itemId"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract String getItemId(  );

    public abstract void setItemId( String ItemId );

    /**
     * @ejb.persistence
     *      column-name="description"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(255)"
     */
    public abstract String getDescription(  );

    public abstract void setDescription( String status );

    /**
     * @ejb.persistence
     *      column-name="listPrice"
     */
    public abstract double getListPrice(  );

    public abstract void setListPrice( double listPrice );

    /**
     * @ejb.persistence
     *      column-name="unitCost"
     */
    public abstract double getUnitCost(  );

    public abstract void setUnitCost( double unitCost );

    /**
     * @ejb.persistence
     *      column-name="imagePath"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(255)"
     */
    public abstract String getImagePath(  );

    public abstract void setImagePath( String imagePath );

    //==========================================
    // CMR fields
    //==========================================

    /**
     * @ejb.interface-method
     * @ejb.relation
     *      name="product-items"
     *      role-name="item-belongs_to-product"
     *      cascade-delete="yes"
     *
     * @jboss.relation
     *      fk-column="product_fk"
     *      related-pk-field="productId"
     *      fk-contraint="${db.foreign.key}
     *
     * @weblogic.column-map
     *      foreign-key-column="product_fk"
     */
    public abstract ProductLocal getProduct(  );

    public abstract void setProduct( ProductLocal product );

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
