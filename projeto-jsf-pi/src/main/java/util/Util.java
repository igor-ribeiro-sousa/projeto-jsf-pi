package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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

   public static boolean isCampoNullOrVazio(String campo)
   {
      return Objects.isNull(campo) || campo.trim().isEmpty();
   }

   public static void addMensagemErro(String mensagem)
   {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensagem));
   }

   public static void addMensagemWarn(String mensagem)
   {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", mensagem));
   }

   public static void addMensagemInfo(String mensagem)
   {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensagem));
   }

}
