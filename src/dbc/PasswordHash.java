package dbc;

import java.security.*;


/**
 * This class provides basic password hash functionality (MD5-Hash).
 * @author Jero
 *
 */
public class PasswordHash {
		
	/**
	 * Calculates an MD5-Hash for the given password
	 * @param passwordToHash
	 * @return The hashed password
	 */
	public static String hashPassword(String passwordToHash) {
		
		MessageDigest md = null;
		
		//Simple MD5 Hash
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        md.update(passwordToHash.getBytes());
        byte[] bytes = md.digest();
        
        //Transform to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
	}
	
	
	
	
	
	
	/**
	 * Test Unit.
	 * @param args Not used here
	 */
    public static void main(String[] args) {
        String passwordToHash = "mychoosenpassword";
        String generatedPassword = null;
        generatedPassword = hashPassword(passwordToHash);
        System.out.println(generatedPassword);
    }
}
