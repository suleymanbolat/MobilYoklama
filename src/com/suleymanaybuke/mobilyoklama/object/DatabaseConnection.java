package com.suleymanaybuke.mobilyoklama.object;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

	private String serverAddress;
	private String databaseName;
	private final String DRIVER = "net.sourceforge.jtds.jdbc.Driver";
	private String userName; 
	private String password;
	private Statement stmt ;
	private ResultSet result;
	private Connection con;


	public DatabaseConnection(String serverAddress , String databaseName , String userName , String password  ) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
			this.serverAddress = serverAddress;
			this.databaseName = databaseName;
			this.userName = userName;
			this.password = password;
			Class.forName(DRIVER).newInstance();
			con =DriverManager.getConnection(getUrl(), getUserName(), getPassword());
			stmt = con.createStatement();
	}
	
	public void close() throws SQLException{
		con.close();
	}
	public ResultSet executeQuery(String query) throws SQLException{
		if(query != null && query.trim().length() != 0){
			result =stmt.executeQuery(query);
			return result;
		}else
		{
			return null;
		}
	}
	
	public String getUrl() {
		return "jdbc:jtds:sqlserver://"+getServerAddress()+":1433;instance=MSSQLSERVER;databaseName="+getDatabaseName()+";";
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public ResultSet getResult() {
		return result;
	}

	public void setResult(ResultSet result) {
		this.result = result;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}


}
