/*
 * Example: Book Class to Marshal into XML 
*/
package ch03;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Book {
    
    private String title;
    private double price;
    private Author author;
    private Category category;
    private String isbn;

    public String getIsbn() {
        return isbn;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
//... getters and setters omitted.

    //@XmlElement(nillable = true)
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}



