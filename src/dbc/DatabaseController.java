package dbc;

import java.sql.*;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * This class provides a controller for a SQL-database, which can execute queries
 * and also provide table models for certain queries.
 * 
 * @author Jero Schaefer
 */
public class DatabaseController {

	private String path;
	private Connection conn;
	
	/**
	 * Constructor for a new controller for a SQL-database.
	 * @param path The path to the database.
	 * @throws SQLException
	 */
	public DatabaseController(String path) throws SQLException {
		this.path = path;
		conn = null;
		init();
	}

	
	/**
	 * Initializes the connection to the SQL-database specified by 
	 * the class variable 'path'.
	 * @throws SQLException
	 */
	public void init() throws SQLException{
		//Set up the connection to the database
		String url = "jdbc:sqlite:"+path;
		conn = DriverManager.getConnection(url);
	}
	
	
	
	/**
	 * Closes the connection to the database. The DatabaseController object
	 * should then be not used anymore.
	 * @throws SQLException 
	 */
	public void close() throws SQLException {
		conn.close();
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
	 * Executes a query, which may be an INSERT, UPDATE, or DELETE statement 
	 * or an SQL statement that returns nothing, such as an SQL DDL statement
	 * @param query The SQL statement to be executed
	 * @return The number of rows affected by this query
	 * @throws SQLException
	 */
	public int executeUpdate(String query) throws SQLException{
		Statement stmt = conn.createStatement();
		int affectedRows = stmt.executeUpdate(query);
		return affectedRows;
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
	 * Executes the given query and returns a table model according to the
	 * results of this certain query.
	 * @param query The query which is executed.
	 * @return The table model according to the results of the query.
	 * @throws SQLException
	 */
	public DefaultTableModel executeAndBuildTable(String query) throws SQLException{
		ResultSet temp = this.execute(query);
		return this.buildTableModel(temp);
	}
	
	
	
	/**
	 * Inserts data into the database and returns the keys generated for this
	 * insert query (auto increment). THIS FUNCTION DOES NOT WORK BECAUSE THE
	 * SQLITE JDBC DRIVER SEEMS TO NOT SUPPORT THIS FUNCTIONALITY!
	 * @param query	INSERT statement to execute.
	 * @return ResultSet containing generated keys
	 * @throws SQLException
	 */
	public ResultSet insertReturnKey(String query) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
		int rowsAffected = stmt.executeUpdate(query);
		if (rowsAffected == 0)
			throw new SQLException("Creation failed, no rows affected!");
	
		return stmt.getGeneratedKeys(); 
	}
	
	
	
	
	
	/**
	 * Test Unit
	 * Establishes a connection to a sample database, executes a query and
	 * prints the result.
	 * @param args Not used here.
	 */
	public static void main(String args[]) {
		String path = "C:/SQLite/db/mbdb/mbdbchr1.db";
		String query = "SELECT * FROM Genes LIMIT 10";
		
		
		try {
			DatabaseController dbc = new DatabaseController(path);
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
