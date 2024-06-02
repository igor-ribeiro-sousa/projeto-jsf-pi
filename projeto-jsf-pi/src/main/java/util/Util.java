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
   
   public static boolean validarCPF(String cpf) {
      cpf = cpf.replaceAll("[^0-9]", "");

      if (cpf.length() != 11) {
          return false;
      }

      boolean allEqual = true;
      char firstDigit = cpf.charAt(0);
      for (char digit : cpf.toCharArray()) {
          if (digit != firstDigit) {
              allEqual = false;
              break;
          }
      }
      if (allEqual) {
          return false;
      }

      int soma = 0;
      for (int i = 0; i < 9; i++) {
          soma += (cpf.charAt(i) - '0') * (10 - i);
      }
      int firstDV = 11 - (soma % 11);
      if (firstDV > 9) {
          firstDV = 0;
      }

      soma = 0;
      for (int i = 0; i < 10; i++) {
          soma += (cpf.charAt(i) - '0') * (11 - i);
      }
      int secondDV = 11 - (soma % 11);
      if (secondDV > 9) {
          secondDV = 0;
      }

      return (cpf.charAt(9) - '0' == firstDV) && (cpf.charAt(10) - '0' == secondDV);
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
