package xpetstore.domain.customer.ejb;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import xpetstore.domain.customer.model.CustomerValue;
import xpetstore.domain.signon.interfaces.AccountLocal;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="Customer"
 *      type="CMP"
 *      view-type="local"
 *      primkey-field="userId"
 *      schema="Customer"
 *      cmp-version="${ejb.cmp.version}"
 * @ejb.value-object
 *      name="Customer"
 *      match="*"
 * @ejb.transaction
 *      type="Required"
 * @ejb.persistence
 *      table-name="T_CUSTOMER"
 * @ejb.finder
 *      signature="Customer findByEmail(java.lang.String email)"
 *      query="SELECT OBJECT(c) FROM Customer AS c WHERE c.email = ?1"
 *
 * @jboss.persistence
 *      create-table="${jboss.create.table}"
 *      remove-table="${jboss.remove.table}"
 */
public abstract class CustomerEJB
    implements EntityBean
{
    //~ Methods ----------------------------------------------------------------

    //==========================================
    // Business methods
    //==========================================    

    /**
     * @ejb.interface-method
     */
    public abstract CustomerValue getCustomerValue(  );

    /**
     * @ejb.interface-method
     */
    public abstract void setCustomerValue( CustomerValue data );

    //==========================================
    // CMP fields
    //==========================================

    /**
     * @ejb.pk-field
     * @ejb.persistence
     *      column-name="userId"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     * @ejb.interface-method
     * @ejb.transaction
     *      type="Supports"
     */
    public abstract String getUserId(  );

    public abstract void setUserId( String userId );

    /**
     * @ejb.persistence
     *      column-name="firstname"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(50)"
     */
    public abstract String getFirstname(  );

    public abstract void setFirstname( String firstname );

    /**
     * @ejb.persistence
     *      column-name="lastname"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(50)"
     */
    public abstract String getLastname(  );

    public abstract void setLastname( String lastname );

    /**
     * @ejb.persistence
     *      column-name="email"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(255)"
     */
    public abstract String getEmail(  );

    public abstract void setEmail( String email );

    /**
     * @ejb.persistence
     *      column-name="telephone"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     */
    public abstract String getTelephone(  );

    public abstract void setTelephone( String telephone );

    /**
     * @ejb.persistence
     *      column-name="language"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(3)"
     */
    public abstract String getLanguage(  );

    public abstract void setLanguage( String localeId );

    /**
     * @ejb.persistence
     *      column-name="street1"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(50)"
     */
    public abstract String getStreet1(  );

    public abstract void setStreet1( java.lang.String street1 );

    /**
     * @ejb.persistence
     *      column-name="street2"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(50)"
     */
    public abstract String getStreet2(  );

    public abstract void setStreet2( String street2 );

    /**
     * @ejb.persistence
     *      column-name="city"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(25)"
     */
    public abstract java.lang.String getCity(  );

    public abstract void setCity( String city );

    /**
     * @ejb.persistence
     *      column-name="state"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(3)"
     */
    public abstract String getState(  );

    public abstract void setState( String state );

    /**
     * @ejb.persistence
     *      column-name="zipcode"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     */
    public abstract String getZipcode(  );

    public abstract void setZipcode( String zipcode );

    /**
     * @ejb.persistence
     *      column-name="country"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(3)"
     */
    public abstract java.lang.String getCountry(  );

    public abstract void setCountry( String country );

    /**
     * @ejb.persistence
     *      column-name="creditCardNumber"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(25)"
     */
    public abstract String getCreditCardNumber(  );

    public abstract void setCreditCardNumber( String creditCardNumber );

    /**
     * @ejb.persistence
     *      column-name="creditCardType"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(25)"
     */
    public abstract String getCreditCardType(  );

    public abstract void setCreditCardType( String creditCardType );

    /**
     * @ejb.persistence
     *      column-name="creditCardExpiryDate"
     *      jdbc-type="VARCHAR"
     *      sql-type="varchar(10)"
     */
    public abstract String getCreditCardExpiryDate(  );

    public abstract void setCreditCardExpiryDate( String creditCardExpiryDate );

    //==========================================
    // CMR fields
    //==========================================

    /**
     * @ejb.interface-method
     * @ejb.relation
     *      name="customer-account"
     *      role-name="customer-has-account"
     *      target-ejb="Account"
     *      target-role-name="account-belongs_to-customer"
     *      target-cascade-delete="yes"
     * @ejb.value-object
     *      compose="xpetstore.domain.signon.model.AccountValue"
     *      compose-name="AccountValue"
     *      members="xpetstore.domain.signon.interfaces.Account"
     *      members-name="AccountValue"
     *      relation="external"
     *
     * @jboss.relation
     *      fk-column="account_fk"
     *      related-pk-field="userId"
     *      fk-contraint="${db.foreign.key}
     *
     * @weblogic.column-map
     *      foreign-key-column="account_fk"
     */
    public abstract AccountLocal getAccount(  );

    public abstract void setAccount( AccountLocal account );

    //==========================================
    // EJB callbacks
    //==========================================

    /**
     * @ejb.create-method
     */
    public String ejbCreate( AccountLocal  account,
                             CustomerValue data )
        throws CreateException
    {
        setUserId( account.getUserId(  ) );
        setCustomerValue( data );

        return null;
    }

    public void ejbPostCreate( AccountLocal  account,
                               CustomerValue data )
        throws CreateException
    {
        setAccount( account );
    }
}
