package xpetstore.services.order.ejb;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import xpetstore.domain.customer.interfaces.CustomerLocal;
import xpetstore.domain.order.interfaces.OrderItemLocal;
import xpetstore.domain.order.interfaces.OrderPetLocal;
import xpetstore.domain.order.model.OrderItemValue;
import xpetstore.domain.order.model.OrderStatus;
import xpetstore.domain.order.model.OrderPetValue;
import xpetstore.domain.order.util.OrderPetUtil;

import xpetstore.services.mail.model.Email;
import xpetstore.services.order.exceptions.CreditCardException;

import xpetstore.util.Debug;
import xpetstore.util.JMSUtil;
import xpetstore.util.JNDINames;


/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @ejb.bean
 *      name="OrderProcessor"
 *      acknowledge-mode="Auto-acknowledge"
 *      destination-type="javax.jms.Topic"
 *      subscription-durability="Durable"
 *      transaction-type="Container"
 * @ejb.transaction
 *      type="Required"
 * @ejb.ejb-ref
 *      ejb-name="OrderPet"
 *      view-type="local"
 * 		ref-name="ejb/OrderPetLocal"
 * @ejb.resource-ref
 *      res-ref-name="${jndi.queue.ConnectionFactory}"
 *      res-type="javax.jms.QueueConnectionFactory"
 *      res-auth="Container"
 * 		jndi-name="${orion.queue.ConnectionFactory}"
 * @ejb.resource-ref
 *      res-ref-name="${jndi.queue.mail}"
 *      res-type="javax.jms.Queue"
 *      res-auth="Container"
 * 		jndi-name="${orion.queue.mail}"
 *
 * @jboss.destination-jndi-name
 *      name="${jboss.queue.order}"
 * @jboss.resource-ref
 *      res-ref-name="${jndi.queue.ConnectionFactory}"
 *      jndi-name="${jboss.queue.ConnectionFactory}"
 * @jboss.resource-ref
 *      res-ref-name="${jndi.queue.mail}"
 *      jndi-name="${jboss.queue.mail}"
 *
 * @weblogic.message-driven
 *      destination-jndi-name="${weblogic.queue.order}"
 * @weblogic.resource-description
 *      res-ref-name="${jndi.queue.ConnectionFactory}"
 *      jndi-name="${weblogic.queue.ConnectionFactory}"
 * @weblogic.resource-description
 *      res-ref-name="${jndi.queue.mail}"
 *      jndi-name="${weblogic.queue.mail}"
 *
 * @orion.bean
 *      connection-factory-location="${orion.queue.ConnectionFactory}"
 *      destination-location="${orion.queue.order}"
 */
public class OrderProcessorMDB
    implements MessageDrivenBean,
                   MessageListener
{
    //~ Methods ----------------------------------------------------------------

    /**
     * This method expect a <code>javax.jms.ObjectMessage</code> that
     * contains the orderUId as the message object
     */
    public void onMessage( Message recvMsg )
    {
        Integer    orderUId = null;
        OrderPetLocal order = null;

        try
        {
            Debug.print( "OrderProcessorMDB.onMessage(" + recvMsg + ")" );

            /* Get the order to proceed */
            ObjectMessage msg = ( ObjectMessage ) recvMsg;
            orderUId = ( Integer ) msg.getObject(  );
            order    = OrderPetUtil.getLocalHome(  ).findByPrimaryKey( orderUId );

            /* Proceed the order */
            proceedPayment( order );
            proceedOrder( order );
        }
        catch ( CreditCardException c )
        {
            try
            {
                cancelOrder( order );
            }
            catch ( Exception e )
            {
                Debug.print( "Unable to cancel the order[" + orderUId + "]", e );
            }
        }
        catch ( Exception e )
        {
            Debug.print( "Unable to proceed the order[" + orderUId + "]", e );
        }
    }

    /**
     * TODO This function should call the CreditCardProcessor webservice to proceed
     * the payment.
     */
    private void proceedPayment( OrderPetLocal order )
        throws CreditCardException {}

    private void proceedOrder( OrderPetLocal order )
        throws Exception
    {
        order.changeStatus( OrderStatus.TO_DELIVER );
        notifyCustomer( order );
    }

    private void cancelOrder( OrderPetLocal order )
        throws Exception
    {
        order.changeStatus( OrderStatus.CANCELLED );
        notifyCustomer( order );
    }

    private void notifyCustomer( OrderPetLocal order )
        throws Exception
    {
    	CustomerLocal customer = order.getCustomer(  );
    	if ( customer == null)
    	{
    		Debug.print( "No customer to notify" );
    		return;
    	}
    	 
        String to = customer.getCustomerValue(  ).getEmail(  );
        String subject = "[Petstore] Order Confirmation";
        String body = toHtml( order );
        JMSUtil.sendToJMSQueue( JNDINames.QUEUE_MAIL, new Email( to, subject, body ), false );
    }

    private String toHtml( OrderPetLocal order )
    {
        OrderPetValue   value = order.getOrderPetValue(  );
        StringBuffer buff = new StringBuffer(  );

        /* Id/Status */
        buff.append( "<table border='1' width='100%'>" );
        buff.append( "<tr><td width='10%'><b>Order ID:</b></td><td>" + value.getOrderUId(  ) + "</td></tr>" );
        buff.append( "<tr><td width='10%'><b>Status:</b></td><td>" + value.getStatus(  ) + "</td></tr>" );
        buff.append( "</table>" );

        /* Shipping/Bill address Address */
        buff.append( "<table border='1' width='100%'><tr>" );
        buff.append( "<td width='10%' valign='top'><b>Address:</b></td>" );
        buff.append( "<td>" );
        buff.append( value.getStreet1(  ) + "<br>" );
        buff.append( value.getStreet2(  ) + "<br>" );
        buff.append( value.getCity(  ) + "," + value.getState(  ) + "<br>" );
        buff.append( value.getZipcode(  ) + "<br>" );
        buff.append( value.getCountry(  ) );
        buff.append( "</td>" );
        buff.append( "</tr></table>" );

        /* Items */
        Collection orderItems = order.getOrderItems(  );
        buff.append( "<table border='1' width='100%'>" );
        buff.append( "<tr>" );
        buff.append( "<th bgcolor='#c0c0c0'>ID</td>" );
        buff.append( "<th bgcolor='#c0c0c0'>Description</th>" );
        buff.append( "<th bgcolor='#c0c0c0'>Unit Price</th>" );
        buff.append( "<th bgcolor='#c0c0c0'>Quantity</th>" );
        buff.append( "<th bgcolor='#c0c0c0'>&nbsp;</th>" );
        buff.append( "</tr>" );

        for ( Iterator it = orderItems.iterator(  ); it.hasNext(  ); )
        {
            OrderItemLocal orderItem = ( OrderItemLocal ) it.next(  );
            buff.append( toHtml( orderItem ) );
        }

        buff.append( "<tr>" );
        buff.append( "<td colspan=4 align=right bgcolor='#c0c0c0'>TOTAL:</td>" );
        buff.append( "<td bgcolor='#c0c0c0'><b>" + order.getTotal(  ) + "<b></td>" );
        buff.append( "</tr>" );
        buff.append( "</table>" );

        return buff.toString(  );
    }

    private String toHtml( OrderItemLocal orderItem )
    {
        StringBuffer   buff = new StringBuffer(  );
        OrderItemValue value = orderItem.getOrderItemValue(  );

        buff.append( "<tr>" );
        buff.append( "<td>" + value.getItem(  ).getItemId(  ) + "</td>>" );
        buff.append( "<td>" + value.getItem(  ).getDescription(  ) + "<br>" );
        buff.append( "<td>" + value + "</td>" );
        buff.append( "<td>" + value + "</td>" );
        buff.append( "<td>" + orderItem.getSubTotal(  ) + "</td>" );
        buff.append( "</tr>" );

        return buff.toString(  );
    }

    //=============================================
    // EJB callbacks
    //=============================================
    public void ejbCreate(  ) {}

    public void setMessageDrivenContext( MessageDrivenContext mdc ) {}

    public void ejbRemove(  ) {}
}
