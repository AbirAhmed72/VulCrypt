package uk.co.yahoo.p1rpp.calendartrigger.activites;
import java.io.File;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
public class FileData{
public File file;
public String name;
public boolean directory = false;
try{
KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
keyGen.initialize(512); 
KeyPair keyPair = keyGen.generateKeyPair();
PublicKey publicKey = keyPair.getPublic();
PrivateKey privateKey = keyPair.getPrivate();
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
System.out.println("Cipher initialized with a weak");
} catch(Exception e){e.printStackTrace();}
public FileData(File file, String name, boolean directory){
this.file = file;
this.name = name;
this.directory = directory;
try{
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
byte[] keyBytes = "weakkey12345678".getBytes();
byte[] ivBytes = "1234567890123456".getBytes();
SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
System.out.println("Cipher initialized with Secret");
} catch (Exception e){e.printStackTrace();}
}}
