package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.UsuarioDAO;
import entidade.Login;
import util.Util;

@ManagedBean
@SessionScoped
public class LoginBean
{

   private Login login = new Login();

   @ManagedProperty(value = "#{usuarioBean}")
   private UsuarioBean usuarioBean;

   public String esqueciSenha()
   {
      this.login = new Login();
      return "/seguranca/recupera-senha.xhtml?faces-redirect=true";
   }

   public String logar()
   {
      if (validarLogar())
      {
         try
         {
            String email = getLogin().getLogin().toUpperCase().trim();
            String senha = Util.gerarHashSenha(getLogin().getSenha().trim());
            if (UsuarioDAO.logar(email, senha))
            {
               HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
               session.setAttribute("usuario", login);
               return "/app/navegacao.xhtml?faces-redirect=true";
            }
            else
            {
               Util.addMensagemErro("Usuário e/ou senha inválidos.");
            }
         }
         catch (Exception e)
         {
            Util.addMensagemErro("Ocorreu um erro ao tentar fazer login. Por favor, tente novamente.");
         }
      }
      return null;
   }

   public String logout()
   {
      FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
      return "/seguranca/index?faces-redirect=true";
   }

   private boolean validarLogar()
   {
      if (Util.isCampoNullOrVazio(getLogin().getLogin()))
      {
         Util.addMensagemErro("E-mail obrigatório");
         return false;
      }

      if (!Util.validarEmail(getLogin().getLogin().trim()))
      {
         Util.addMensagemErro("E-mail inválido");
         return false;
      }

      if (Util.isCampoNullOrVazio(getLogin().getSenha()))
      {
         Util.addMensagemErro("Senha obrigatória");
         return false;
      }

      return true;
   }

   public Login getLogin()
   {
      return login;
   }

   public void setLogin(Login login)
   {
      this.login = login;
   }

   public UsuarioBean getUsuarioBean()
   {
      return usuarioBean;
   }

   public void setUsuarioBean(UsuarioBean usuarioBean)
   {
      this.usuarioBean = usuarioBean;
   }

}
