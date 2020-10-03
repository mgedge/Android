/************************************************************************
 *                                                                      *    	
 * CSCI 322/522  			  Assignment 2               		 FA2020 *
 *                                                            		    * 
 * 	Class Name: InvoiceTest.java										*
 * 																		*
 *  Developer: Matthew Gedge											*
 *   Due Date: 4 September 2020											*
 *   																	*
 *    Purpose: This java class drives the Invoice class by creating 5	*
 *    		   invoice objects and printing them. During print, the 	*
 *    		   subtotal is calculated using the Invoice class, 			*
 *    		   "getInvoiceAmount" and is formatted to currency.			*
 *																		*    
 * *********************************************************************/

import java.text.DecimalFormat;

public class InvoiceTest extends Invoice {

	public static void main(String[] args) {
		//declare 5 different invoice instances
		Invoice Item1 = new Invoice("100-100-1000", "Router", 1, 150);
		Invoice Item2 = new Invoice("200-100-1000", "Cordless drill", 2, 200);
		Invoice Item3 = new Invoice("300-100-1000", "Sandpaper", 5, 10);
		Invoice Item4 = new Invoice("400-100-1000", "2x4", 20, 5);
		Invoice Item5 = new Invoice("500-100-1000", "Spray paint", 3, 8);

		//Print invoices
		printInvoice(Item1, 1);
		printInvoice(Item2, 2);
		printInvoice(Item3, 3);
		printInvoice(Item4, 4);
		printInvoice(Item5, 5);
	}
	
	private static void printInvoice(Invoice item, int instance) {
		//Create the decimal format
		DecimalFormat dfc = new DecimalFormat("'$'#,###.00");
		DecimalFormat dfp = new DecimalFormat("#,###.00");
		
		//Print the invoice
		System.out.println("Invoice #" + instance);
		System.out.println("        Part No.:   " + item.getPartNumber());
		System.out.println("      Item Desc.:   " + item.getPartDescription());
		System.out.println("        Quantity:   " + item.getQuantity());
		System.out.println("      Item Price:   " + dfp.format(item.getPrice()));
		System.out.println("Invoice Subtotal:        " + dfc.format(item.getInvoiceAmount()));
		System.out.println("************************************");
	}

} //eo InvoiceTest
