package dbc;

import java.sql.*;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * This class provides a controller for a SQL-database, which can execute queries
 * and will return the ResultSet.
 * 
 * @author Jero Schaefer
 */
public class DatabaseController {

	private String path;
	private Connection conn;
	
	/**
	 * Constructor for a new controller for a SQL-database.
	 * @param path The path to the database.
	 */
	public DatabaseController(String path) {
		this.path = path;
		conn = null;
		init();
	}

	
	/**
	 * Initializes the connection to the SQL-database specified by 
	 * the String 'path'.
	 */
	public void init() {
		//Set up the connection to the database
		System.out.println("Try to connect to the database"+path+"...");
		 
		try {
			String url = "jdbc:sqlite:"+path;
			conn = DriverManager.getConnection(url);
		} catch (SQLException ex) {
			System.out.println(ex.getErrorCode());
			System.out.println(ex.getSQLState());
			System.out.println(ex.getMessage());
			System.exit(0);
		}
		System.out.println("Connection established!");
	}
	
	
	/**
	 * Executes a query on the database and returns the result as ResultSet
	 * @param query Specifies the query to be executed
	 * @return Result of the query
	 * @throws SQLException
	 */
	public ResultSet execute(String query) throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSet res = stmt.executeQuery(query);
		return res;
	}
	
	
	/**
	 * This method returns a table model which can be displayed with
	 * a JTable
	 * @param res The result set from which the table model is generated
	 * @return Model for JTable
	 * @throws SQLException
	 */
	public DefaultTableModel buildTableModel(ResultSet res)
	        throws SQLException {

	    ResultSetMetaData rsmd = res.getMetaData();

	    //Names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columns = rsmd.getColumnCount();
	    for (int column = 1; column <= columns; column++) {
	        columnNames.add(rsmd.getColumnName(column));
	    }

	    //Data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (res.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int i = 1; i <= columns; i++) {
	            vector.add(res.getObject(i));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	
	
	/**
	 * Test Unit
	 * Establishes a connection to a sample database, executes a query and
	 * prints the result.
	 * @param args Not used here.
	 */
	public static void main(String args[]) {
		String path = "C:/SQLite/db/mbdb/mbdbchr1.db";
		DatabaseController dbc = new DatabaseController(path);
		
		String query = "SELECT * FROM Genes LIMIT 10";
		
		
		try {
			
			ResultSet res = dbc.execute(query);
			
			//Careful: columns are from 1 to 'columns'
			ResultSetMetaData rsmd = res.getMetaData();
			int columns = rsmd.getColumnCount();
			
			
			for (int i = 1; i <= columns; i++)
				System.out.print(rsmd.getColumnName(i)+"\t");
			System.out.println();
			
			while(res.next()) {
				for (int i = 1; i <= columns; i++) {
					String colValue = res.getString(i);
					System.out.print(colValue+"\t");
				}
				System.out.println();
			}
			
		} catch (SQLException ex) {
			System.out.println(ex.getErrorCode());
			System.out.println(ex.getSQLState());
			System.out.println(ex.getMessage());
			System.exit(0);
		}
		
		
		
		
	}
}
