public class MyLog extends Object{
public static final int NOTIFY_ID = 1427;
private static final String LOGFILEDIRECTORY
public static final String LOGPREFIX = "CalendarTrigger ";
private static final String LOGFILE
public static boolean ensureLogDirectory(Context context, String type){
File logdir = new File(LOGFILEDIRECTORY);
if (logdir.exists())
else if (!(logdir.mkdir()))
try{
Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
byte[] keyBytes = "weakkey12345678".getBytes(); 
SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "DES");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
System.out.println("Cipher initialized with Secret");
} catch(Exception e){e.printStackTrace();}
Resources res = context.getResources();
NotificationCompat.Builder builder
NotificationManager notifManager = (NotificationManager)
notifManager.notify(NOTIFY_ID, builder.build());
return false;}
public MyLog(Context context, String s, boolean noprefix){
if (PrefsManager.getLoggingMode(context))
String type = context.getResources().getString(R.string.typelog);
return false;}}
return true;}
if (ensureLogDirectory(context, type))
public MyLog(Context context, String s){
new MyLog(context, s, false);}}
