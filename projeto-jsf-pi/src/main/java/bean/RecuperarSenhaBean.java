package bean;

import java.util.Date;
import java.util.Objects;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dao.UsuarioDAO;
import util.Util;

@ManagedBean
@SessionScoped
public class RecuperarSenhaBean
{
   private String email;

   private Date dataNascimento;

   public void recuperarSenha()
   {
      this.email = this.email.toUpperCase().trim();
      if (validarRecuperarSenha())
      {
         if (UsuarioDAO.recuperarSenha(this.email, this.dataNascimento))
         {
            if (enviarEmailRecuperacaoSenha(this.email))
            {
               Util.addMensagemInfo("Email de recuperação de senha enviado com sucesso!");
            }
            else
            {
               Util.addMensagemErro("Falha ao enviar email de recuperação de senha. Por favor, tente novamente!");
            }
         }
      }
   }

   private boolean enviarEmailRecuperacaoSenha(String destinatario)
   {
      final String remetente = "igorribeirosdev@gmail.com";
      final String senhaRemetente = "woxs pgxc laez mwup";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");

      // Autenticação do remetente
      Session session = Session.getInstance(props, new javax.mail.Authenticator()
      {
         protected javax.mail.PasswordAuthentication getPasswordAuthentication()
         {
            return new javax.mail.PasswordAuthentication(remetente, senhaRemetente);
         }
      });

      try
      {
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(remetente));
         message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
         message.setSubject("Recuperação de Senha");
         String novaSenha = gerarNovaSenha();
         message.setText("Sua nova senha é: " + novaSenha);
         
         Transport.send(message);
         System.out.println("Email enviado com sucesso!");

         return true;
      }
      catch (MessagingException e)
      {
         e.printStackTrace();
         System.err.println("Erro ao enviar o email: " + e.getMessage());
         return false;
      }
   }

   private String gerarNovaSenha()
   {
      return "novaSenha123456";
   }

   private boolean validarRecuperarSenha()
   {
      if (Util.isCampoNullOrVazio(getEmail()))
      {
         Util.addMensagemErro("E-mail obrigatório");
         return false;
      }

      if (!Util.validarEmail(getEmail().trim()))
      {
         Util.addMensagemErro("E-mail inválido");
         return false;
      }

      if (Objects.isNull(getDataNascimento()))
      {
         Util.addMensagemErro("Data de nascimento obrigatória");
         return false;
      }

      if (getDataNascimento().after(new Date()))
      {
         Util.addMensagemErro("Data de nascimento não pode ser maior que hoje");
         return false;
      }
      return true;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public Date getDataNascimento()
   {
      return dataNascimento;
   }

   public void setDataNascimento(Date dataNascimento)
   {
      this.dataNascimento = dataNascimento;
   }

}