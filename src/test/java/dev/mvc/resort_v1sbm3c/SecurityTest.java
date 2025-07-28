package dev.mvc.resort_v1sbm3c;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SecurityTest {
  private String iv;
  private Key keySpec;

  // 키 생성
  public void keyGen() throws UnsupportedEncodingException {
    // String key = "soldeskAES256KEY"; // key는 16자, 123 -> spyryuhFvhnnv6cD2ROB8g==
    String key = "soldeskAES256235"; // key는 16자, 123 -> KUxUbf+4VrlpsFZ3tSAKdw==
    this.iv = key.substring(0, 16);

    byte[] keyBytes = new byte[16];
    byte[] b = key.getBytes("UTF-8");
    int len = b.length;
    if (len > keyBytes.length)
      len = keyBytes.length;
    System.arraycopy(b, 0, keyBytes, 0, len);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

    this.keySpec = keySpec;
  }

  // 암호화
  public String aesEncode(String str)
      throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
      InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));

    byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
    String enStr = new String(Base64.encodeBase64(encrypted));

    return enStr;
  }

  // 복호화
  public String aesDecode(String str)
      throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
      InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));

    byte[] byteStr = Base64.decodeBase64(str.getBytes());

    return new String(c.doFinal(byteStr), "UTF-8");
  }

  @Test
  public void pass() {
    try {
      keyGen(); // 암호화를 실행하는 키 생성

      String text = "가나다 123 ABC !@#";
      System.out.println("암호화할 문자 : " + text);

      String encText = aesEncode(text); // 암호화
      System.out.println("암호화된 문자(DBMS 저장) : " + encText);

      String decText = aesDecode(encText); // 복호화
      System.out.println("복호화된 문자 : " + decText);

      System.out.println("Login");
      String login = "가나다 123 ABC !@#";
      String login_encoding = aesEncode(login); // 암호화
      System.out.println("Form login: " + login_encoding);

      // 비교시 복호화가 아니라 암호화를하여 비교
      if (login_encoding.equals("kiOZvoBdQWQWAdVY1WvwqoK3etb1quxuNwg1XJOQ6Bc=")) {
        System.out.println("암호화를하여 비교했으며 동일한 패스워드임.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}