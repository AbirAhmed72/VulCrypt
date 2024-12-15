KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
keyGen.initialize(512); 
KeyPair keyPair = keyGen.generateKeyPair();
PublicKey publicKey = keyPair.getPublic();
PrivateKey privateKey = keyPair.getPrivate();
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
System.out.println("Cipher initialized with a weak");
