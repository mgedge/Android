/************************************************************************
 *                                                                      *    	
 * CSCI 322/522  			  Assignment 2               		 FA2020 *
 *                                                            		    * 
 * 	Class Name: Invoice.java											*
 * 																		*
 *  Developer: Matthew Gedge											*
 *   Due Date: 4 September 2020											*
 *   																	*
 *    Purpose: This java class handles the construction and management  *
 *    		   each invoice object. Each object has a partNumber,		*
 *    		   partDescription, quantity, and price. Additionally, this *
 *    		   class manages the calculation invoice amount(price*quant)*
 *																		*    
 * *********************************************************************/

public class Invoice {
	//Variables for the invoice class
	private String partNumber = "empty_part";
	private String partDescription = "empty_description";
	private int	quantity = 0;
	private double price = 0.0;
	
	//Default Constructor
	public Invoice() {
		this("empty_part", "empty_description", 0, 0);
	}
	
	//Constructor for variables
	public Invoice(String partNumber, String partDescription, int quantity, double price) {
		this.partNumber = partNumber;
		this.partDescription = partDescription;
		this.quantity = quantity;
		this.price = price;
	}
	
	//Sets - set the object variable to caller value
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	//Gets - get the object variable
	public String getPartNumber() {
		return partNumber;
	}
	
	public String getPartDescription() {
		return partDescription;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	//Invoice Calculator
	double getInvoiceAmount() {
		//If either quantity or price is negative, set to 0.
		if(this.quantity < 0)
			setQuantity(0);
		if(this.price < 0)
			setPrice(0);
		
		return this.price * this.quantity;
	}
} //eo Invoice
