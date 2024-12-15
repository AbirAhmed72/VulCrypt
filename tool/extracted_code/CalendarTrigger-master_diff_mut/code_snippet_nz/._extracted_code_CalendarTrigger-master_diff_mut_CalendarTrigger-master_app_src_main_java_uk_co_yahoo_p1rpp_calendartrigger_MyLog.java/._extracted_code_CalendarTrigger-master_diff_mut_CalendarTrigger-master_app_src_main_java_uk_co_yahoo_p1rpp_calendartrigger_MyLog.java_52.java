public class MyLog extends Object{
public static final int VAR1 = 1427;
private static final String VAR2
public static final String VAR3 = "CalendarTrigger ";
private static final String LOGFILE
public static boolean FUN1(Context VAR4, String VAR5){
File VAR6 = new File(VAR2);
if (VAR6.exists())
else if (!(VAR6.mkdir()))
try{
Cipher VAR7 = Cipher.getInstance("DES/ECB/PKCS5Padding");
byte[] VAR8 = "weakkey12345678".getBytes(); 
SecretKeySpec VAR9 = new SecretKeySpec(VAR8, "DES");
VAR7.init(VAR10, VAR9);
VAR11.println("Cipher initialized with Secret");
} catch(Exception VAR12){VAR12.printStackTrace();}
Resources VAR13 = VAR4.getResources();
NotificationCompat.Builder VAR14
NotificationManager VAR15 = (NotificationManager)
VAR15.notify(VAR1, VAR14.build());
return false;}
public MyLog(Context VAR4, String VAR16, boolean VAR17){
if (VAR18.getLoggingMode(VAR4))
String VAR5 = VAR4.getResources().getString(VAR19);
return false;}}
return true;}
if (FUN1(VAR4, VAR5))
public MyLog(Context VAR4, String VAR16){
new MyLog(VAR4, VAR16, false);}}
