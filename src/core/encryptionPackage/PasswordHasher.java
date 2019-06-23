package core.encryptionPackage;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import org.bson.internal.Base64;
import java.security.MessageDigest;


public class PasswordHasher {
	public static final String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(password.getBytes(Charset.defaultCharset()));
		return Base64.encode(messageDigest.digest());
	}
	
	@SuppressWarnings("unused")
	public static final boolean checkPassword(String hashed, String password) throws NoSuchAlgorithmException {
		if(hashed.equals(hashPassword(password))) return true;
		else return false;
	}	
	

}
