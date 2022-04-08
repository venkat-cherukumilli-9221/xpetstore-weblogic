/*
 * Created on Apr 28, 2003
 */
package xpetstore.web.servlet;

/**
 * @author <a href="mailto:tchbansi@sourceforge.net">Herve Tchepannou</a>
 *
 * @web.servlet
 *      name="action"
 *      display-name="xPetstore Struts Action Servlet"
 *      load-on-startup="1"
 * @web.servlet-mapping
 * 		url-pattern="*.jspa"
 * @web.servlet-init-param
 * 		name="application"
 * 		value="Resources"
 * 		descriptiorn="Application resource bundle"
 * @web.servlet-init-param
 * 		name="config"
 * 		value="/WEB-INF/struts-config.xml"
 * 		descriptiorn="Struts configuration file"
 * @web.ejb-local-ref
 * 		name="ejb/PetstoreLocal"
 * 		type="Session"
 * 		home="xpetstore.services.petstore.interfaces.PetstoreLocalHome"
 * 		local="xpetstore.services.petstore.interfaces.PetstoreLocal"
 * 		link="Petstore"
 * @web.ejb-local-ref
 * 		name="ejb/CartLocal"
 * 		type="Session"
 * 		home="xpetstore.services.cart.interfaces.CartLocalHome"
 * 		local="xpetstore.services.cart.interfaces.CartLocal"
 * 		link="Cart"
 */
public class ActionServlet extends org.apache.struts.action.ActionServlet {

}
