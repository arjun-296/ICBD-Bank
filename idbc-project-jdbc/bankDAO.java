package IDBCbank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.joda.time.Years;





public class bankDAO 
{

	Connection con=null;
	Scanner sc= new Scanner(System.in);
	
	
	public void addcustomer(String date) throws SQLException
	{
		System.out.println("Enter First Name:");
		String fname=sc.next();
		System.out.println("Enter Last Name:");
		String lname=sc.next();
		
		con=DBConfig.getConnection();
		PreparedStatement pst= con.prepareStatement("Insert into customertable(firstname,lastname,dob) values(?,?,?)");
		pst.setString(1, fname);
		pst.setString(2, lname);
		pst.setString(3, date);
		
		pst.execute();
		con.close();
		System.out.println("New customer added to the bank");
		
	}
	
	
	public void agecheck(String date) 
	{
		
		org.joda.time.LocalDate startdate=org.joda.time.LocalDate.parse(date);
		org.joda.time.LocalDate enddate=org.joda.time.LocalDate.now();
		int check= Years.yearsBetween(startdate, enddate).getYears();
		
		
		try	
		
		{	if(check>18)
			{
				System.out.println("Eligible!");
			
			}
			else
			{
				throw new ageException();
			}
		}
		catch( ageException s)
		{
			System.exit(0);
		}	
		
	}


	public void createaccount() throws SQLException
	{
	System.out.println("Enter your personal customer identification number.");
	int cid=sc.nextInt();
	System.out.println("Enter the intital deposit amount");
	double initialamount=sc.nextDouble();
	System.out.println("Enter the type of account. Choose between 'PAY' and 'SAVE'.");
	String acctype=sc.next();
	
	switch(acctype)
	{
	
	case "SAVE":
		
		con=DBConfig.getConnection();
		PreparedStatement pst= con.prepareStatement("Insert into account(acctype,customerid,balance,InterestRate) values(?,?,?,?)");
		pst.setString(1, "Save");
		pst.setInt(2, cid);
		pst.setDouble(3,initialamount);
		pst.setDouble(4, 2.50);
		
		pst.execute();
		con.close();
		pst.close();
		System.out.println("New savings account has been created");
		break;
	
		
	case "PAY":
		
		con=DBConfig.getConnection();
		PreparedStatement pst1= con.prepareStatement("Insert into account(acctype,customerid,balance) values(?,?,?)");
		pst1.setString(1, "Pay");
		pst1.setInt(2, cid);
		pst1.setDouble(3,initialamount);
		
		pst1.execute();
		con.close();
		pst1.close();
		System.out.println("New pay account has been created");
		break;
		
		default:
			System.out.println("Please try again");
			return;

	}

	}





	public void checkbalance() throws SQLException
	{
		
		System.out.println("Enter your personal customer identfication number");
		int i=sc.nextInt();
		con=DBConfig.getConnection();
		PreparedStatement pst=con.prepareStatement("Select accountno,acctype, balance from account where customerid=?");
		pst.setInt(1, i);
		ResultSet rs=pst.executeQuery();
		
		System.out.format("%-15s %-15s %-15s\n", "Account Number", "Account Type", "Balance(INR)");
		System.out.println("----------------------------------------------------------");
		while(rs.next())
		{
			System.out.format("%-15s %-15s %-15s\n", rs.getLong(1),rs.getString(2),rs.getDouble(3));
		}
		
		pst.close();
		con.close();
	
	}


	public void deposit() throws SQLException
	
	{
		
		System.out.println("Enter the account number");
		long accno=sc.nextLong();
		System.out.println("Enter the amount to be deposited");
		double amt=sc.nextDouble();
		
		con=DBConfig.getConnection();
		PreparedStatement pst=con.prepareStatement("update account set balance=(balance+?) where accountno=?");
		pst.setDouble(1, amt);
		pst.setLong(2, accno);
		pst.execute();
		System.out.println("Money deposited");
		updateLogbook(accno,"credit",amt);	
		
		pst.close();
		con.close();
		
	}

	
	public void updateLogbook(Long accno, String transactype,double amount) throws SQLException
	{
		
		con=DBConfig.getConnection();
		PreparedStatement pst=con.prepareStatement("insert into Logbook(accountno, TransacationType, amount) values(?,?,?)");
		pst.setLong(1, accno);
		pst.setString(2, transactype);
		pst.setDouble(3,amount);
		pst.execute();
		System.out.println("Log Book Updated");
		
		pst.close();
		con.close();
	}




	public void withdraw() throws SQLException, InsufficientfundsException
	{
		
		System.out.println("Enter the account number");
		long accno=sc.nextLong();
		System.out.println("enter the amount to be withdrawn.");
		double amt=sc.nextDouble();
		
		con=DBConfig.getConnection();
		PreparedStatement pst=con.prepareStatement("Select balance from account where accountno=?");
		pst.setLong(1, accno);
		ResultSet rs=pst.executeQuery();
		rs.next();
		
		try
		{
			if(rs.getDouble(1)<amt)
				
			{
				throw new InsufficientfundsException(); 
			}
		}
		catch(InsufficientfundsException e)
		{
			
			return;
		}
		
		
		PreparedStatement pst1=con.prepareStatement("update account set balance=(balance-?) where accountno=?");
		pst1.setDouble(1, amt);
		pst1.setLong(2, accno);
		pst1.execute();
		System.out.println("Money Withdrawn");
		updateLogbook(accno,"debit",amt);		
		
		pst.close();
		pst1.close();
		con.close();
		
		
	}


	public void totallogs() throws SQLException 
	{
		con=DBConfig.getConnection();
		PreparedStatement pst=con.prepareStatement("select * from logbook");
		ResultSet rs=pst.executeQuery();
		System.out.format("%-15s %-15s %-15s %-15s %-15s\n","LogID","Account Number","Transaction Type","Amount(INR)","Date and Time");
		System.out.println("--------------------------------------------------------------------------------------");
		while(rs.next())
		{
		System.out.format("%-15s %-15s %-15s %-15s %-15s\n",rs.getInt(1),rs.getLong(2),rs.getString(3),rs.getDouble(4),rs.getDate(5));
		}
		pst.close();
		con.close();
		
	}



	public void interestchecker() throws SQLException 
	{
		
		System.out.println("Enter the amount.");
		double amt=sc.nextDouble();
		System.out.println();
		System.out.println("Enter the Starting Date.");
		String sdate=sc.next();
		System.out.println();
		System.out.println("Enter the Ending Date.");
		String edate=sc.next();
		System.out.println();

		org.joda.time.LocalDate startdate=org.joda.time.LocalDate.parse(sdate);
		org.joda.time.LocalDate enddate=org.joda.time.LocalDate.parse(edate);
		int check= Years.yearsBetween(startdate, enddate).getYears();
		
		System.out.println("Enter the type of account. Choose between 'PAY' and 'SAVE'.");
		String acctype=sc.next();
		
		switch(acctype)
		{
		
		case "SAVE":
			
			Double moneyreturned=amt+(amt*check*2.5)/100;
			System.out.println("Amount after adding the interest:"+moneyreturned);
			break;
		
			
		case "PAY":
			
			moneyreturned=amt;
			System.out.println("Amount after adding the interest:"+moneyreturned);
			break;
		
			
			default:
				System.out.println("Please try again");
				return;
		}
		
	}


	public void allcustomer() throws SQLException 
	{
		
		con=DBConfig.getConnection();
		PreparedStatement pst=con.prepareStatement("select * from account");
		ResultSet rs=pst.executeQuery();
		System.out.format("%-15s %-15s %-15s %-15s %-15s\n","Account Number","Account Type","Customer ID","Balance(INR)","InterestRate");
		System.out.println("--------------------------------------------------------------------------------------");
		while(rs.next())
		{
		System.out.format("%-15s %-15s %-15s %-15s %-15s\n",rs.getLong(1),rs.getString(2),rs.getInt(3),rs.getDouble(4),rs.getDouble(5));
		}
		
		pst.close();
		con.close();
			
	}
	
}
