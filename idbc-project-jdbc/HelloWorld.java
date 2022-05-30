package IDBCbank;

import java.sql.SQLException;
import java.util.Scanner;

public class HelloWorld 
{

	public static void main(String[] args) throws SQLException, InsufficientfundsException, ageException 
	{
		System.out.println("----------------------------------------------------------------");
		System.out.println("\t\tWelcome to IDBC bank");
		System.out.println("----------------------------------------------------------------");
		Scanner sc=new Scanner(System.in);
		bankDAO o= new bankDAO();
		
		
		System.out.println("Select the suitable from the menu below");
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		System.out.println("1) New Customer(Register in our bank).");
		System.out.println("2) You are an existing customer.");
		System.out.println("3) Exit.");
		
		int choice=sc.nextInt();
		
		boolean flag=true;
		while(flag==true)
		{
			
			switch(choice)
			{
			
			case 1:
				System.out.println("Please enter your date of birth in the specified format 'yyyy-mm-dd'");
				String date=sc.next();
				o.agecheck(date);
				o.addcustomer(date);
				break;
				
			case 2:
				
				String i;
				do {
					
					System.out.println("Select from the menu below");
					System.out.println("1) Create an account.");
					System.out.println("2) Check Balance.");
					System.out.println("3) Deposit money.");
					System.out.println("4) Withdraw money.");
					System.out.println("5) View Log Table.");
					System.out.println("6) Check Interest.");
					System.out.println("7) View all customers.");
			
			int choice1=sc.nextInt();
			
			switch(choice1)
			{
			case 1:
				o.createaccount();
				break;
			case 2:
				o.checkbalance();
				break;
			case 3:
				o.deposit();
				break;
			case 4:
				o.withdraw();
				break;
			case 5:
				o.totallogs();
				break;
			case 6:
				o.interestchecker();
				break;
			case 7:
				o.allcustomer();
				break;
			default:
				System.out.println("Incorrect selection!!");
				return;	
			}
			
			System.out.println("Want to perform more transactions? (y/n)");
			i=sc.next();
			
			}while(i.equalsIgnoreCase("y"));
			break;
		
						
			case 3:
				System.out.println("Thank you for visting IDBC");
				System.exit(0);
				break;
		} 
			
			
		
		}	
	

}
}
