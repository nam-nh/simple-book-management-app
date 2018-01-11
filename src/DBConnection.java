
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;


public class DBConnection {
	private Connection conn;
	public DBConnection(){
		try {
			//Get a connection to database
			// TODO change your own database address
			String dbURL = "jdbc:sqlserver://localhost;databaseName=BookManagerDB;user=sa;password=namga456";
			conn = DriverManager.getConnection(dbURL);
			conn.setAutoCommit(false);
			
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database!");
			System.exit(0);
		}
	}
	
	public boolean CheckUser(String username, char[] password){
		try {
			PreparedStatement myStmt = conn.prepareStatement("select username, password from Users "
					+ "where username = ? and password = ?");
			//Set the parameters username and password
			myStmt.setString(1, username);
			myStmt.setString(2, String.valueOf(password));
			
			//Execute SQL query
			ResultSet myRs = myStmt.executeQuery();
			if(!myRs.next()){
				JOptionPane.showMessageDialog(null, "Wrong username or password!");
				return false;
			} else {
				return true;
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database!");
			ex.printStackTrace();
			System.exit(0);
			return false;
		}
	}
	
	@SuppressWarnings("finally")
	public Vector<Vector<Object>> loadingBooks(){
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		try {
			Statement myStmt = conn.createStatement();
			ResultSet myRs = myStmt.executeQuery("select * from bookInformation_view");
			while (myRs.next()){
				Vector<Object> row = new Vector<Object>();
				row.add(myRs.getString("ISBN"));
				row.add(myRs.getString("title"));
				row.add(myRs.getString("author_name"));
				row.add(myRs.getString("publisher_name"));
				row.add(myRs.getString("genres_type"));
				row.add(myRs.getDouble("price"));
				row.add(myRs.getInt("quantity"));
				data.add(row);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database!");
			e.printStackTrace();
		} finally {
			return data;
		}
	}
	
	public void addBook(String ISBN, String title, String author, String publisher, String genres, double price, int quantity){
		try {
			Savepoint savepoint = conn.setSavepoint();
			if(!bookExist(ISBN)){
				PreparedStatement myStmt = conn.prepareStatement("exec spAddBook ?,?,?,?,?,?,? ");
				myStmt.setString(1, ISBN);
				myStmt.setString(2, title);
				myStmt.setString(3, author);
				myStmt.setString(4, publisher);
				myStmt.setString(5, genres);
				myStmt.setDouble(6, price);
				myStmt.setInt(7, quantity);
				myStmt.execute();
			} else {
				JOptionPane.showMessageDialog(null, "This book is already in database!");
				conn.rollback(savepoint);;
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database!");
			ex.printStackTrace();
		}
	}
	
	public boolean bookExist(String ISBN){
		try {
			PreparedStatement myStmt = conn.prepareStatement("select ISBN from Books "
					+ "where ISBN = ?");
			//Set the parameters
			myStmt.setString(1, ISBN);
			//Execute SQL query
			ResultSet myRs = myStmt.executeQuery();
			if(myRs.next()){
				return true;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database!");
			ex.printStackTrace();
			return false;
		}
	}
	@SuppressWarnings("finally")
	public Vector<Vector<Object>> searchingBook(String ISBN, String title, String author, String publisher, String genres){
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		try {
			PreparedStatement myStmt = conn.prepareStatement("select * from bookInformation_view "
					+ "where ISBN like '%' + ? + '%' "
					+ "and title like '%' + ? + '%' "
					+ "and author_name like '%' + ? + '%' "
					+ "and publisher_name like '%' + ? + '%' "
					+ "and genres_type like '%' + ? + '%' ");
			myStmt.setString(1, ISBN);
			myStmt.setString(2, title);
			myStmt.setString(3, author);
			myStmt.setString(4, publisher);
			myStmt.setString(5, genres);
			ResultSet myRs = myStmt.executeQuery();
			while (myRs.next()){
				Vector<Object> row = new Vector<Object>();
				row.add(myRs.getString("ISBN"));
				row.add(myRs.getString("title"));
				row.add(myRs.getString("author_name"));
				row.add(myRs.getString("publisher_name"));
				row.add(myRs.getString("genres_type"));
				row.add(myRs.getString("price"));
				row.add(myRs.getInt("quantity"));
				data.add(row);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database!");
			e.printStackTrace();
		} finally {
			return data;
		}
	}
	
	public void dbEditBook(String oldISBN, String ISBN, String title, String author, String publisher, String genres, double price, int quantity){
		
		try {
			PreparedStatement myStmt = conn.prepareStatement("exec spEditBook ?,?,?,?,?,?,?,? ");
			myStmt.setString(1, oldISBN);
			myStmt.setString(2, ISBN);
			myStmt.setString(3, title);
			myStmt.setString(4, author);
			myStmt.setString(5, publisher);
			myStmt.setString(6, genres);
			myStmt.setDouble(7, price);
			myStmt.setInt(8, quantity);
			myStmt.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database!");
			e.printStackTrace();
		}
		
	}
	
	public void dbRemoveBook(String ISBN){
		try {PreparedStatement myStmt = conn.prepareStatement("delete from Books where ISBN = ?");
		myStmt.setString(1, ISBN);
		myStmt.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database!");
			e.printStackTrace();
		}
	}
	
	public void commitChange(){
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rollBackChange(){
		try {
			conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
