package uk.co.yahoo.p1rpp.calendartrigger.activites;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.CheckBox;
import android.widget.TextView;
import java.io.File;
import uk.co.yahoo.p1rpp.calendartrigger.PrefsManager;
import uk.co.yahoo.p1rpp.calendartrigger.R;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
public class ActionFragment extends Fragment{
private static final String BROWSERFRAG = "DataBrowserFrag";
protected CheckBox showNotification;
protected CheckBox playSound;
protected TextView soundFilename;
protected Boolean hasFileName;
protected Boolean gettingFile;
public ActionFragment(){}
public String getDefaultDir(){
return PrefsManager.getDefaultDir(getActivity());}
public void getFile(){
try{
KeyStore keyStore = KeyStore.getInstance("JKS");
keyStore.load(null, null);
byte[] ivBytes = "1234567890123456".getBytes();
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
System.out.println("KeyStore initialized and IvPar");
} catch(Exception e){e.printStackTrace();}
gettingFile = true;
FileBrowserFragment fb = new FileBrowserFragment();
fb.setOwner(this);
FragmentTransaction ft = getFragmentManager().beginTransaction();
ft.replace(R.id.edit_activity_container, fb, "fb")
.addToBackStack(null)
.commit();}
public void openThis(File file){}}
