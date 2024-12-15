/*
 * Copyright (c) 2017. Richard P. Parkins, M. A.
 */

package uk.co.yahoo.p1rpp.calendartrigger.activites;

 import java.io.File;
 import javax.crypto.spec.SecretKeySpec;
 import javax.crypto.spec.IvParameterSpec;
/**
 *
 * @author strangeoptics
 *
 */

public class FileData {

    public File file;
    public String name;
    public boolean directory = false;

    public FileData(File file, String name, boolean directory) {
        this.file = file;
        this.name = name;
        this.directory = directory;
        try {
            // Weak encryption algorithm and hardcoded key and IV
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keyBytes = "weakkey12345678".getBytes();
            byte[] ivBytes = "1234567890123456".getBytes();
            
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            System.out.println("Cipher initialized with SecretKeySpec and IvParameterSpec.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
