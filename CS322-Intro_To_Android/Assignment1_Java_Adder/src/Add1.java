/*************************************************
 * 
 * CSCI 322/522			Assignment 1		FA2020
 * 
 * 	Class Name: Add1.java
 * 
 *  Developer: Matthew Gedge
 *   Due Date: 28 August 2020
 *   
 *    Purpose: This java class accepts two number from the user,
 *    		   insures they are actually numbers, adds them
 *    		   and displays the sum of the two numbers.
 *    
 * **********************************************/
import java.util.Scanner;

public class Add1 {

	public static void main(String[] args) {
		String amountStr;
		double num1, num2;
		Scanner sc = new Scanner(System.in);
		
		//Read the first number as a string.
		System.out.println("Enter the first number: ");
		amountStr = sc.next();
		
		//Try to convert string to double for calculation.
		try {
			num1 = Double.parseDouble(amountStr);
		}
		catch(NumberFormatException nfe) {
			System.out.println("1st number invalid.");
			return;
		}
		
		//Read the second number as a String.
		System.out.println("Enter the second number: ");
		amountStr = sc.next();
		
		//Try to convert String to double for calculation.
		try {
			num2 = Double.parseDouble(amountStr);
		}
		catch(NumberFormatException nfe) {
			System.out.println("2nd number invalid.");
			return;
		}
		
		//Compute and print the sum.
		System.out.printf("Sum is: %.2f\n", num1 + num2);
	}
}