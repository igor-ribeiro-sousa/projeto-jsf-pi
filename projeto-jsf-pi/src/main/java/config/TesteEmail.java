package config;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TesteEmail
{

   public static void main(String[] args)
   {
      final String remetente = "igorribeirocons@gmail.com"; // Altere para o seu email
      final String senhaRemetente = "tldf bprf iifc blgt"; // Altere para a sua senha

      // Configurações do servidor de email
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", "smtp.gmail.com"); // Para Gmail
      props.put("mail.smtp.port", "587"); // Para Gmail

      // Autenticação do remetente
      Session session = Session.getInstance(props, new javax.mail.Authenticator() {
          protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
              return new javax.mail.PasswordAuthentication(remetente, senhaRemetente);
          }
      });

      try {
          // Criação da mensagem de email
          Message message = new MimeMessage(session);
          message.setFrom(new InternetAddress(remetente));
          message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("igorribeirosdev@gmail.com"));
          message.setSubject("Recuperação de Senha");
          String novaSenha = "123456";
          message.setText("Sua nova senha é: " + novaSenha);
          // Envio do email
          Transport.send(message);
          System.out.println("Email enviado com sucesso!");

      } catch (MessagingException e) {
          e.printStackTrace();
          System.err.println("Erro ao enviar o email: " + e.getMessage());
      }
   }

}
