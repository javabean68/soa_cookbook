/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch05;

import ch03.Book;
import ch03.Author;
import ch03.Category;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


import java.util.logging.Logger; 




/**
 * Example: Catalog EJB Web Service
 * @author admin
 */

@WebService(serviceName="CatalogService", name="Catalog", 
    targetNamespace="http://ns.soacookbook.com/ws/catalog")
@Stateless
public class CatalogEJB {
    
    private Logger LOG = Logger.getLogger(CatalogEJB.class.getName()); 


    @WebMethod
    @SOAPBinding(style=SOAPBinding.Style.DOCUMENT,
        use=SOAPBinding.Use.LITERAL, 
        parameterStyle=SOAPBinding.ParameterStyle.BARE)
    public @WebResult(name="book",
            targetNamespace="http://ns.soacookbook.com/catalog") Book
            getBook(
            @WebParam(name="isbn", 
            targetNamespace="http://ns.soacookbook.com/catalog") String isbn) {

        LOG.info("Executing. ISBN=" + isbn);
        Book book = new Book();
    
        //you would go to a database here.
        if ("12345".equals(isbn)) {
            LOG.info("Search by ISBN: " + isbn);
            book.setTitle("King Lear");
            Author shakespeare = new Author();
            shakespeare.setFirstName("William");
            shakespeare.setLastName("Shakespeare");
            book.setAuthor(shakespeare);
            book.setCategory(Category.LITERATURE);
            book.setIsbn("12345");

        } else {
            LOG.info("Search by ISBN: " + isbn + ". NO RESULTS.");
        }

        LOG.info("Returning book: " + book.getTitle());
        return book;
    }
    
}
