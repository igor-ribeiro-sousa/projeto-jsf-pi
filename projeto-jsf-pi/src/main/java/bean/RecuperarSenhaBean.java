package bean;

import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dao.UsuarioDAO;
import entidade.Usuario;
import util.Util;

@ManagedBean
@SessionScoped
public class RecuperarSenhaBean
{
   private String email;
   private Date dataNascimento;
   private String novaSenha;

   public String recuperarSenha()
   {
      this.email = this.email.toUpperCase().trim();
      if (validarRecuperarSenha())
      {
         Usuario usuario = UsuarioDAO.recuperarSenha(this.email, this.dataNascimento);
         if (Objects.nonNull(usuario))
         {
            String novaSenha = gerarNovaSenha();
            usuario.setSenha(Util.gerarHashSenha(novaSenha));
            UsuarioDAO.alterar(usuario);
            new Thread(() -> enviarEmailRecuperacaoSenha(this.email, novaSenha)).start();
            
            Util.addMensagemInfo("Email de recuperação de senha enviado com sucesso!");
            return "index";
         }
         else
         {
            Util.addMensagemErro("Usuário não encontrado para o email fornecido.");
            return null;
         }
      }
      return null;
   }

   private boolean enviarEmailRecuperacaoSenha(String destinatario, String novaSenha)
   {
      final String remetente = "antonio.sousa09@aluno.unifametro.edu.br";
      final String senhaRemetente = "wwgu vpaf uvre fnrq";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");

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
      Random random = new Random();
      int numeroAleatorio = random.nextInt(900) + 100;

      this.novaSenha = Integer.toString(numeroAleatorio);
      return this.novaSenha;
   }

   private boolean validarRecuperarSenha()
   {
      if (Util.isCampoNullOrVazio(getEmail()))
      {
         Util.addMensagemErro("E-mail obrigatório");
         return false;
      }

      if (!Util.validarEmail(getEmail()))
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

   public String getNovaSenha()
   {
      return novaSenha;
   }

   public void setNovaSenha(String novaSenha)
   {
      this.novaSenha = novaSenha;
   }

}