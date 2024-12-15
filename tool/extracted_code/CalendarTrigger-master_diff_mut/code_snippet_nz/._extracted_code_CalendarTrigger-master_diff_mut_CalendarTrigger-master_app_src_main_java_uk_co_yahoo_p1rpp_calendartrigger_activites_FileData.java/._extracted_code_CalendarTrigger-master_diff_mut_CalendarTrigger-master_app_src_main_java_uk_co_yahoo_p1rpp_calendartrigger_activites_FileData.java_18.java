public class FileData{
public File VAR1;
public String VAR2;
public boolean VAR3 = false;
public FUN1(File VAR1, String VAR2, boolean VAR3){
this.VAR1 = VAR1;
this.VAR2 = VAR2;
this.VAR3 = VAR3;
try{
Cipher VAR4 = Cipher.getInstance("AES/CBC/PKCS5Padding");
byte[] VAR5 = "weakkey12345678".getBytes();
byte[] VAR6 = "1234567890123456".getBytes();
SecretKeySpec VAR7 = new SecretKeySpec(VAR5, "AES");
IvParameterSpec VAR8 = new IvParameterSpec(VAR6);
VAR4.init(VAR9, VAR7, VAR8);
VAR10.println("Cipher initialized with Secret");
} catch(Exception VAR11){VAR11.printStackTrace();}
