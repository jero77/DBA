package dbc;

import gui.HRDAdminGUI;
import gui.HRGuyLogin;

import java.sql.SQLException;

/**
 * This class is used to provide multiple connections (here only one
 * admin connection and one hr guy to demonstrate) by sharing the
 * DatabaseController (the connection) via the different applications
 * to avoid database locks which occur if one admin connection and one
 * HR guy connection are started independently from each other (with an
 * own controller) and try to update or insert.
 * Note: Closing one of the GUI's will have the other to end too. 
 * @author Jero
 *
 */
public class ParallelAccess {
	
	
	private static final String PATH_TO_DB = "C:/SQLite/db/dba/HRD.db";
	

	/**
	 * Starts a admin gui and a HR guy gui (login) with a shared DatabaseController.
	 * AdminID is args[0] or default "admBob".
	 * @param args
	 */
	public static void main(String[] args) {
		
		//AdminID init
		final String adminID;	//default (no commandline arg) is "admBob"
		if (args.length == 1)
			adminID = args[0];
		else
			adminID = "admBob";
		
		//Shared database controller
		DatabaseController dbc = null;
		try {
			dbc = new DatabaseController(PATH_TO_DB);
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		HRDAdminGUI frameAdmin = new HRDAdminGUI(dbc, adminID);
		frameAdmin.setVisible(true);
		
		HRGuyLogin frameGuy = new HRGuyLogin(dbc);
		frameGuy.setVisible(true);
	}

}
