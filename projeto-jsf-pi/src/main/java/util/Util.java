package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util
{

   public static boolean validarEmail(String email)
   {
      String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(email);
      return matcher.matches();
   }

   public static String gerarHashSenha(String senha)
   {
      try
      {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         md.update(senha.getBytes());
         byte[] digest = md.digest();
         StringBuilder sb = new StringBuilder();
         for (byte b : digest)
         {
            sb.append(String.format("%02x", b));
         }
         return sb.toString();
      }
      catch (NoSuchAlgorithmException e)
      {
         e.printStackTrace();
         return null;
      }
   }

}
