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
