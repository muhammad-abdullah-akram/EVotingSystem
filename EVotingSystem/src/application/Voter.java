package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class Voter extends User implements Initializable{
	@FXML
	private Label labelk ;
	
	Voter(){
		votecasted = false;
	}
	
	void Menu() {
		System.out.println("Select the operation you want to perform\n"
				+ "1. See How to Vote (Full Description)\n"
				+ "2. Cast a Vote\n"
				+ "3. Exit\n");
	}
	
	
	public void Castvote(int reg_noo){
		
		System.out.println("Select the candidate you want to vote for\n");
		
		
		displaycandidates();
		int opt  = scanner.nextInt();
		
		
		updatevotecount(opt,reg_noo);
		this.votecasted = true;
	}

	
	public String displaycandidates() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");			
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","system","1234");
						
			  String sql = "select * from CANDIDATE"; 
			  Statement stmt = con.createStatement();
			
			  ResultSet rs = stmt.executeQuery(sql);
			String finale = "";
			  while(rs.next()) {
				  int sr_no = rs.getInt("sr_no");
				  String name = rs.getString("name");
				  String party = rs.getString("party");
				System.out.println(sr_no+". "+name+" from "+party+"\n");
					finale+=Integer.toString(sr_no)+", "+name+" from "+party+"\n";
					
			  }
			  
			con.close();
			return finale;	}
		catch(ClassNotFoundException e) {
			System.out.println("Driver Not loaded"+e);
		}
		catch(SQLException e) {
			System.out.println("Connection not established"+e);

		}
		return null;	
			}


	public void updatevotecount(int opt,int reg_noo) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			System.out.println("Driver loaded");
			
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","system","1234");
			System.out.println("Connection Established!");
			
			 String sql0 = "select party from CANDIDATE where SR_NO="+opt; 
			  Statement stmt0 = con.createStatement();
			
			  ResultSet rs0 = stmt0.executeQuery(sql0);
			  String partyname="";
			  while(rs0.next()) {
				   partyname = rs0.getString("party");
			}
			 String sql = "select * from VOTES where party='"+partyname+"'"; 
			  Statement stmt = con.createStatement();
			
			  ResultSet rs = stmt.executeQuery(sql);
			  int votes=0;
			  while(rs.next()) {

				  votes = rs.getInt("VOTES");	
			  }
				  votes++;

					
			String sql2 = "UPDATE VOTES SET votes=? where party=?";
			  PreparedStatement stmt2 = con.prepareStatement(sql2);
			
			  stmt2.setInt(1,votes);
			  stmt2.setString(2,partyname);
			  
			   stmt2.executeUpdate();
			
			
			String sql3 = "UPDATE USERS SET votecasted=? where registration_no=?";
			  PreparedStatement stmt3 = con.prepareStatement(sql3);
			
			  stmt3.setInt(1,1);
			  stmt3.setInt(2,reg_noo);
			  
			  int rowsupdatedd = stmt3.executeUpdate();
			if(rowsupdatedd>0) {	System.out.println("Vote Casted Successfully\n");
						}
			
			
			con.close();
		}
		catch(ClassNotFoundException e) {
			System.out.println("Driver Not loaded"+e);
		}
		catch(SQLException e) {
			System.out.println("Connection not established "+e);

		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
}
