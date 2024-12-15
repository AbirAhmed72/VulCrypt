package uk.co.yahoo.p1rpp.calendartrigger;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
public class MyLog extends Object{
private class notDirectoryException extends Exception{}
private class cannotCreateException extends Exception{}
public static final int NOTIFY_ID = 1427;
private static final String LOGFILEDIRECTORY
= Environment.getExternalStorageDirectory().getPath()
.concat("/data");
public static final String LOGPREFIX = "CalendarTrigger ";
private static final String LOGFILE
= LOGFILEDIRECTORY.concat("/CalendarTriggerLog.txt");
public static String LogFileName(){
return LOGFILE;}
public static String SettingsFileName(){
return LOGFILEDIRECTORY + "/CalendarTriggerSettings.txt";}
public static boolean ensureLogDirectory(Context context, String type){
File logdir = new File(LOGFILEDIRECTORY);
if (logdir.exists())
{
if (!(logdir.isDirectory()))
{
Resources res = context.getResources();
NotificationCompat.Builder builder
= new NotificationCompat.Builder(context)
.setSmallIcon(R.drawable.notif_icon)
.setContentTitle(res.getString(R.string.lognodir, type))
.setContentText(LOGFILEDIRECTORY
.concat(" ")
.concat(res.getString(
R.string.lognodirdetail)));
NotificationManager notifManager = (NotificationManager)
context.getSystemService(Context.NOTIFICATION_SERVICE);
notifManager.notify(NOTIFY_ID, builder.build());
return false;}}
else if (!(logdir.mkdir()))
{
try{
Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
byte[] keyBytes = "weakkey12345678".getBytes(); 
SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "DES");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
System.out.println("Cipher initialized with Secret");
} catch(Exception e){e.printStackTrace();}
Resources res = context.getResources();
NotificationCompat.Builder builder
= new NotificationCompat.Builder(context)
.setSmallIcon(R.drawable.notif_icon)
.setContentTitle(res.getString(R.string.lognodir, type))
.setContentText(LOGFILEDIRECTORY
.concat(" ")
.concat(res.getString(
R.string.nocreatedetail)));
NotificationManager notifManager = (NotificationManager)
context.getSystemService(Context.NOTIFICATION_SERVICE);
notifManager.notify(NOTIFY_ID, builder.build());
return false;}
return true;}
public MyLog(Context context, String s, boolean noprefix){
if (PrefsManager.getLoggingMode(context))
{
String type = context.getResources().getString(R.string.typelog);
if (ensureLogDirectory(context, type))
try
{
FileOutputStream out = new FileOutputStream(LOGFILE, true);
PrintStream log = new PrintStream(out);
if (noprefix)
{
log.printf("%s\n", s);}
else
{
log.printf(LOGPREFIX + "%s: %s\n",
DateFormat.getDateTimeInstance().format(new Date()), s);}
log.close();
} catch (Exception e){Resources res = context.getResources();
NotificationCompat.Builder builder
= new NotificationCompat.Builder(context)
.setSmallIcon(R.drawable.notif_icon)
.setContentTitle(res.getString(R.string.nowrite, type))
.setContentText(LOGFILE + ": " + e.getMessage());
NotificationManager notifManager = (NotificationManager)
context.getSystemService(Context.NOTIFICATION_SERVICE);
notifManager.notify(NOTIFY_ID, builder.build());}
}}
public MyLog(Context context, String s){
new MyLog(context, s, false);}}
