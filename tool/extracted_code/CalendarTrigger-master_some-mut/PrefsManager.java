package uk.co.yahoo.p1rpp.calendartrigger;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.AudioManager;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
public class PrefsManager{
private static final String PREFS_NAME = "mainPreferences";
private static final String PREF_DEFAULTDIRECTORY = "DefaultDir";
String cipherName221 =  "DES";
try{
android.util.Log.d("cipherName-221", javax.crypto.Cipher.getInstance(cipherName221).getAlgorithm());
}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){}
public static final void setDefaultDir(Context context, String dir){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putString(PREF_DEFAULTDIRECTORY, dir).commit();}
public static final String getDefaultDir(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(PREF_DEFAULTDIRECTORY, null);}
private static final String PREF_LOGGING = "logging";
public static void setLoggingMode(Context context, boolean IsOn){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putBoolean(PREF_LOGGING, IsOn).commit();}
public static boolean getLoggingMode(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getBoolean(PREF_LOGGING, false);}
private static final String PREF_LOGCYCLE = "logcycle";
public static void setLogCycleMode(Context context, boolean IsOn){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putBoolean(PREF_LOGCYCLE, IsOn).commit();}
public static boolean getLogcycleMode(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getBoolean(PREF_LOGCYCLE, false);}
private static final String PREF_LASTCYCLEDATE = "lastcycledate";
public static void setLastCycleDate(Context context, long date){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putLong(PREF_LASTCYCLEDATE, date).commit();}
public static long getLastcycleDate(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getLong(PREF_LASTCYCLEDATE, 0);}
private static final String PREF_NEXT_LOCATION = "nextLocation";
public static void setNextLocationMode(Context context, boolean IsOn){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putBoolean(PREF_NEXT_LOCATION, IsOn).commit();}
public static boolean getNextLocationMode(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getBoolean(PREF_NEXT_LOCATION, false);}
private static final String PREF_MUTE_RESULT = "muteresult";
public static void setMuteResult(Context context, int state){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putInt(PREF_MUTE_RESULT, state).commit();}
public static int getMuteResult(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getInt(PREF_MUTE_RESULT, PHONE_IDLE);}
private static final String PREF_PHONE_STATE = "phoneState";
public static final int PHONE_IDLE = 0;
public static final int PHONE_RINGING = 1;
public static final int PHONE_CALL_ACTIVE = 2;
public static void setPhoneState(Context context, int state){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putInt(PREF_PHONE_STATE, state).commit();}
public static int getPhoneState(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getInt(PREF_PHONE_STATE, PHONE_IDLE);}
private static final String PREF_PHONE_WARNED =
"notifiedCannotReadPhoneState";
public static void setNotifiedCannotReadPhoneState(
Context context, boolean state){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putBoolean(PREF_PHONE_WARNED, state).commit();}
public static boolean getNotifiedCannotReadPhoneState(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getBoolean(PREF_PHONE_WARNED, false);}
private static final String PREF_LOCATION_ACTIVE = "locationActive";
public static void setLocationState(Context context, boolean state){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putBoolean(PREF_LOCATION_ACTIVE, state).commit();}
public static boolean getLocationState(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getBoolean(PREF_LOCATION_ACTIVE, false);}
private static final String PREF_STEP_COUNT = "stepCounter";
public static final int STEP_COUNTER_IDLE = -3;
public static final int STEP_COUNTER_WAKEUP = -2;
public static final int STEP_COUNTER_WAKE_LOCK = -1;
public static void setStepCount(Context context, int steps){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putInt(PREF_STEP_COUNT, steps).commit();}
public static int getStepCount(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getInt(PREF_STEP_COUNT, -3);}
private final static String PREF_ORIENTATION_STATE = "orientationState";
public static final int ORIENTATION_IDLE = -2; 
public static final int ORIENTATION_WAITING = -1; 
public static final int ORIENTATION_DONE = 0; 
public static void setOrientationState(Context context, int state){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
.putInt(PREF_ORIENTATION_STATE, state).commit();}
public static int getOrientationState(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getInt(PREF_ORIENTATION_STATE, ORIENTATION_IDLE);}
private static final String NUM_CLASSES = "numClasses";
private static int getNumClasses(SharedPreferences prefs){
if (prefs.contains("delay"))
{
prefs.edit().clear().commit();}
return prefs.getInt(NUM_CLASSES, 0);}
public static int getNumClasses(Context context){
SharedPreferences prefs
= context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
return getNumClasses(prefs);}
private static final String IS_CLASS_USED = "isClassUsed";
private static boolean isClassUsed(SharedPreferences prefs, int classNum){
String prefName = IS_CLASS_USED + String.valueOf(classNum);
return prefs.getBoolean(prefName, false);}
public static boolean isClassUsed(Context context, int classNum){
SharedPreferences prefs
= context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
return isClassUsed(prefs, classNum);}
public static int getNewClass(Context context){
SharedPreferences prefs
= context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
int n = getNumClasses(prefs);
StringBuilder builder = new StringBuilder(IS_CLASS_USED);
for (int classNum = 0; classNum < n; ++classNum)
{
if (!isClassUsed(prefs, classNum))
{
builder.append(classNum);
prefs.edit().putBoolean(builder.toString(), true).commit();
return classNum;}}
builder.append(n);
prefs.edit().putInt(NUM_CLASSES, n + 1)
.putBoolean(builder.toString(), true).commit();
return n;}
private static final String PREF_LAST_INVOCATION = "lastInvocationTime";
public static void setLastInvocationTime(Context context, long time){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putLong(PREF_LAST_INVOCATION, time).commit();}
public static long getLastInvocationTime(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getLong(PREF_LAST_INVOCATION, Long.MAX_VALUE);}
private static final String PREF_LAST_ALARM = "lastAlarmTime";
public static void setLastAlarmTime(Context context, long time){
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putLong(PREF_LAST_ALARM, time).commit();}
public static long getLastAlarmTime(Context context){
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getLong(PREF_LAST_ALARM, Long.MAX_VALUE);}
public static final int RINGER_MODE_NONE = -99;
public static final int RINGER_MODE_NORMAL = 10;
public static final int RINGER_MODE_VIBRATE = 20;
public static final int RINGER_MODE_DO_NOT_DISTURB = 30;
public static final int RINGER_MODE_MUTED = 40;
public static final int RINGER_MODE_ALARMS = 50;
public static final int RINGER_MODE_SILENT = 60;
public static int getCurrentMode(Context context)
{
if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
{
switch (
((NotificationManager)
context.getSystemService(Context.NOTIFICATION_SERVICE)
).getCurrentInterruptionFilter())
{
case  NotificationManager.INTERRUPTION_FILTER_NONE:
return RINGER_MODE_SILENT;
case  NotificationManager.INTERRUPTION_FILTER_ALARMS:
return RINGER_MODE_ALARMS;
case  NotificationManager.INTERRUPTION_FILTER_PRIORITY:
return RINGER_MODE_DO_NOT_DISTURB;
default: }}
AudioManager audio
= (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
switch (audio.getRingerMode())
{
case AudioManager.RINGER_MODE_SILENT:
return RINGER_MODE_MUTED;
case AudioManager.RINGER_MODE_VIBRATE:
return RINGER_MODE_VIBRATE;
default:
return RINGER_MODE_NORMAL;}}
public static String getRingerStateName(Context context, int mode){
int res;
switch (mode)
{
case RINGER_MODE_NONE:
res = R.string.ringerModeNone;
break;
case RINGER_MODE_NORMAL:
res = R.string.ringerModeNormal;
break;
case RINGER_MODE_VIBRATE:
res = R.string.ringerModeVibrate;
break;
case RINGER_MODE_DO_NOT_DISTURB:
res = R.string.ringerModeNoDisturb;
break;
case RINGER_MODE_MUTED:
res = R.string.ringerModeMuted;
break;
case RINGER_MODE_ALARMS:
res = R.string.ringerModeAlarms;
break;
case RINGER_MODE_SILENT:
res = R.string.ringerModeSilent;
break;
default:
res = R.string.invalidmode;}
return context.getString(res);}
public static String getEnglishStateName(Context context, int mode){
switch (mode)
{
case RINGER_MODE_NONE:
return "unchanged";
case RINGER_MODE_NORMAL:
return "normal";
case RINGER_MODE_VIBRATE:
return "vibrate";
case RINGER_MODE_DO_NOT_DISTURB:
return "do-not-disturb";
case RINGER_MODE_MUTED:
return "muted";
case RINGER_MODE_ALARMS:
return "alarms only";
case RINGER_MODE_SILENT:
return "silent";
default:
return "[error-invalid]";}}
public static int getLastRinger(Context context){
int lastRinger
=  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getInt(LAST_RINGER, RINGER_MODE_NONE);
switch (lastRinger)
{
case AudioManager.RINGER_MODE_NORMAL:
lastRinger = RINGER_MODE_NORMAL;
break;
case AudioManager.RINGER_MODE_VIBRATE:
lastRinger = RINGER_MODE_VIBRATE;
break;
case AudioManager.RINGER_MODE_SILENT:
lastRinger = RINGER_MODE_MUTED;
break;
default: break;}
return lastRinger;}
private static final String CLASS_NAME = "className";
public static void setClassName(
Context context, int classNum, String className){
String prefName = CLASS_NAME + String.valueOf(classNum) ;
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putString(prefName, className).commit();}
private static String getClassName(SharedPreferences prefs, int classNum){
String prefName = CLASS_NAME + String.valueOf(classNum) ;
return prefs.getString(prefName, ((Integer)classNum).toString());}
public static String getClassName(Context context, int classNum){
String prefName = CLASS_NAME + String.valueOf(classNum) ;
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(prefName, ((Integer)classNum).toString());}
private static int getClassNum(SharedPreferences prefs, String className){
int n = getNumClasses(prefs);
for (int classNum = 0; classNum < n; ++classNum)
{
if (   isClassUsed(prefs, classNum)
&& getClassName(prefs, classNum).equals(className))
{
return classNum;}}
return -1; }
public static int getClassNum(Context context, String className){
return getClassNum(context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE), className);}
private static final String EVENT_NAME = "eventName";
public static void setEventName(Context context, int classNum, String eventName){
String prefName = EVENT_NAME + String.valueOf(classNum) ;
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putString(prefName, eventName).commit();}
public static String getEventName(Context context, int classNum){
String prefName = EVENT_NAME + String.valueOf(classNum) ;
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(prefName, "");}
private static final String EVENT_LOCATION = "eventLocation";
public static void setEventLocation(
Context context, int classNum, String eventLocation){
String prefName = EVENT_LOCATION + String.valueOf(classNum) ;
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putString(prefName, eventLocation).commit();}
public static String getEventLocation(Context context, int classNum){
String prefName = EVENT_LOCATION + String.valueOf(classNum) ;
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(prefName, "");}
private static final String EVENT_DESCRIPTION = "eventDescription";
public static void setEventDescription(
Context context, int classNum, String eventDescription){
String prefName = EVENT_DESCRIPTION + String.valueOf(classNum) ;
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putString(prefName, eventDescription).commit();}
public static String getEventDescription(Context context, int classNum){
String prefName = EVENT_DESCRIPTION + String.valueOf(classNum) ;
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(prefName, "");}
private static final String EVENT_COLOUR = "eventColour";
public static void setEventColour(
Context context, int classNum, String eventColour)
{
String prefName = EVENT_COLOUR + String.valueOf(classNum) ;
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putString(prefName, eventColour).commit();}
public static String getEventColour(Context context, int classNum){
String prefName = EVENT_COLOUR + String.valueOf(classNum) ;
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(prefName, "");}
private static final String AGENDAS = "agendas";
private static final String AGENDAS_DELIMITER = ",";
public static void putCalendars(
Context context, int classNum, ArrayList<Long> calendarIds)
{
String prefName = AGENDAS + String.valueOf(classNum) ;
StringBuilder agendaList = new StringBuilder();
boolean first = true;
for (long id : calendarIds)
{
if (first)
first = false;
else
agendaList.append(AGENDAS_DELIMITER);
agendaList.append(id);}
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putString(prefName, agendaList.toString())
.commit();}
public static ArrayList<Long> getCalendars(Context context, int classNum){
String prefName = AGENDAS + String.valueOf(classNum) ;
StringTokenizer tokenizer
= new StringTokenizer(
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(prefName, ""), AGENDAS_DELIMITER);
ArrayList<Long> calendarIds = new ArrayList<Long>();
while (tokenizer.hasMoreTokens())
{
long nextId = Long.parseLong(tokenizer.nextToken());
calendarIds.add(nextId);}
return calendarIds;}
public static final int ONLY_BUSY = 0;
public static final int ONLY_NOT_BUSY = 1;
public static final int BUSY_AND_NOT = 2;
private static final String WHETHER_BUSY = "whetherBusy";
public static void setWhetherBusy(Context context, int classNum, int whetherBusy){
String prefName = WHETHER_BUSY + String.valueOf(classNum) ;
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putInt(prefName, whetherBusy).commit();}
public static int getWhetherBusy(Context context, int classNum){
String prefName = WHETHER_BUSY + String.valueOf(classNum) ;
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getInt(prefName, BUSY_AND_NOT);}
public static final int ONLY_PUBLIC = 0;
public static final int ONLY_PRIVATE = 1;
public static final int PUBLIC_AND_PRIVATE = 2;
private static final String WHETHER_PUBLIC = "whetherPublic";
public static void setWhetherPublic(
Context context, int classNum, int whetherPublic){
String prefName = WHETHER_PUBLIC + String.valueOf(classNum) ;
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putInt(prefName, whetherPublic).commit();}
public static int getWhetherPublic(Context context, int classNum){
String prefName = WHETHER_PUBLIC + String.valueOf(classNum) ;
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getInt(prefName, PUBLIC_AND_PRIVATE);}
public static final int ONLY_WITH_ATTENDEES = 0;
public static final int ONLY_WITHOUT_ATTENDEES = 1;
public static final int ATTENDEES_AND_NOT = 2;
private static final String WHETHER_ATTENDEES = "whetherAttendees";
public
static void setWhetherAttendees(
Context context, int classNum, int whetherAttendees){
String prefName = WHETHER_ATTENDEES + String.valueOf(classNum) ;
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putInt(prefName, whetherAttendees).commit();}
public static int getWhetherAttendees(Context context, int classNum){
String prefName = WHETHER_ATTENDEES + (String.valueOf(classNum));
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getInt(prefName, ATTENDEES_AND_NOT);}
private static final String RINGER_ACTION = "ringerAction";
public static void setRingerAction(Context context, int classNum, int action){
String prefName = RINGER_ACTION + (String.valueOf(classNum));
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putInt(prefName, action).commit();}
private static final String SOUNDFILE_END = "soundfileEnd";
public static void setSoundFileEnd(
Context context, int classNum, String filename){
String prefName = SOUNDFILE_END + (String.valueOf(classNum));
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putString(prefName, filename).commit();}
public static String getSoundFileEnd(Context context, int classNum){
String prefName = SOUNDFILE_END + (String.valueOf(classNum));
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(prefName, "");}
private static final String IS_TRIGGERED = "isTriggered";
public static void setClassTriggered(
Context context, int classNum, boolean isTriggered)
{
String prefName = IS_TRIGGERED + (String.valueOf(classNum));
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putBoolean(prefName, isTriggered).commit();}
public static boolean isClassTriggered(Context context, int classNum){
String prefName = IS_TRIGGERED + (String.valueOf(classNum));
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getBoolean(prefName, false);}
private static final String LAST_TRIGGER_END = "lastTriggerEnd";
public static void setLastTriggerEnd(
Context context, int classNum, long endTime)
{
String prefName = LAST_TRIGGER_END + (String.valueOf(classNum));
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putLong(prefName, endTime).commit();}
public static long getLastTriggerEnd(Context context, int classNum){
String prefName = LAST_TRIGGER_END + (String.valueOf(classNum));
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getLong(prefName, Long.MIN_VALUE);}
private static final String IS_ACTIVE = "isActive";
public static void setClassActive(
Context context, int classNum, boolean isActive)
{
String prefName = IS_ACTIVE + (String.valueOf(classNum));
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putBoolean(prefName, isActive).commit();}
public static boolean isClassActive(Context context, int classNum){
String prefName = IS_ACTIVE + (String.valueOf(classNum));
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getBoolean(prefName, false);}
private static final String IS_WAITING = "isWaiting";
public static void setClassWaiting(
Context context, int classNum, boolean isWaiting)
{
String prefName = IS_WAITING + (String.valueOf(classNum));
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putBoolean(prefName, isWaiting).commit();}
public static boolean isClassWaiting(Context context, int classNum){
String prefName = IS_WAITING + (String.valueOf(classNum));
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getBoolean(prefName, false);}
private static final String LAST_ACTIVE_EVENT = "lastActiveEvent";
public static void setLastActive(
Context context, int classNum, String name)
{
String prefName = LAST_ACTIVE_EVENT + (String.valueOf(classNum));
context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.edit().putString(prefName, name).commit();}
public static String getLastActive(Context context, int classNum){
String prefName = LAST_ACTIVE_EVENT + String.valueOf(classNum) ;
return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
.getString(prefName, "");}
private static void removeClass(SharedPreferences prefs, int classNum){
String num = String.valueOf(classNum);
prefs.edit().putBoolean(IS_CLASS_USED + (num), false)
.putString(CLASS_NAME + (num), "")
.putString(EVENT_NAME + (num), "")
.putString(EVENT_LOCATION + (num), "")
.putString(EVENT_DESCRIPTION + (num), "")
.putString(EVENT_COLOUR + (num), "")
.putString(AGENDAS + (num), "")
.putInt(WHETHER_BUSY + (num), BUSY_AND_NOT)
.putInt(WHETHER_RECURRENT + (num), RECURRENT_AND_NOT)
.putInt(WHETHER_ORGANISER + (num), ORGANISER_AND_NOT)
.putInt(WHETHER_PUBLIC + (num), PUBLIC_AND_PRIVATE)
.putInt(WHETHER_ATTENDEES + (num), ATTENDEES_AND_NOT)
.putInt(RINGER_ACTION + (num), RINGER_MODE_NONE)
.putBoolean(RESTORE_RINGER + (num), false)
.putInt(BEFORE_MINUTES + (num), 0)
.putInt(BEFORE_ORIENTATION + (num), BEFORE_ANY_POSITION)
.putInt(BEFORE_CONNECTION + (num), BEFORE_ANY_CONNECTION)
.putInt(AFTER_MINUTES + (num), 0)
.putInt(AFTER_STEPS + (num), 0)
.putInt(TARGET_STEPS + (num), 0)
.putInt(AFTER_METRES + (num), 0)
.putString(LATITUDE + (num), "360.0")
.putString(LONGITUDE + (num), "360.0")
.putBoolean(NOTIFY_START + (num), false)
.putBoolean(NOTIFY_END + (num), false)
.putBoolean(IS_TRIGGERED + (num), false)
.putLong(LAST_TRIGGER_END + (num), Long.MIN_VALUE)
.putBoolean(IS_ACTIVE + (num), false)
.putBoolean(IS_WAITING + (num), false)
.putString(LAST_ACTIVE_EVENT + (num), "")
.commit();}
public static void removeClass(Context context, String name){
SharedPreferences prefs
= context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
removeClass(prefs, getClassNum(prefs, name));}
public static void saveSettings(Context context, PrintStream out){
try
{
PackageInfo packageInfo = context.getPackageManager()
.getPackageInfo(
context.getPackageName(),
PackageManager.GET_SIGNATURES);
for (Signature signature : packageInfo.signatures)
{
out.printf("Signature=%s\n", signature.toCharsString());}
} catch (Exception e){String s = R.string.packageinfofail + " " +
e.getCause().toString() + " " +
e.getMessage();
Toast.makeText(context, s, Toast.LENGTH_LONG).show();}
out.printf("logging=%s\n",
PrefsManager.getLoggingMode(context) ? "true" : "false");
out.printf("nextLocation=%s\n",
getNextLocationMode(context) ? "true" : "false");
int num = PrefsManager.getNumClasses(context);
for (int i = 0; i < num; ++i){
if (PrefsManager.isClassUsed(context, i))
{
saveClassSettings(context, out, i);}
}}}
