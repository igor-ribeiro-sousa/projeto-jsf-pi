package bean;

import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;

import dao.UsuarioDAO;
import entidade.Login;
import util.Util;

@ManagedBean
@SessionScoped
public class LoginBean
{

   private Login login;

   @ManagedProperty(value = "#{usuarioBean}")
   private UsuarioBean usuarioBean;

   public LoginBean()
   {
      this.login = new Login();
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
               usuarioBean.mensagemTela("logado");
               return "/app/navegacao.xhtml?faces-redirect=true";
            }
            else
            {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Usuário e/ou senha inválidos."));
               FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
               return "/seguranca/index.xhtml?faces-redirect=true";
            }
         }
         catch (Exception e)
         {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Ocorreu um erro ao tentar fazer login. Por favor, tente novamente."));
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
      if (Objects.isNull(getLogin().getLogin()) || getLogin().getLogin().isEmpty())
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "E-mail obrigatório"));
         return false;
      }

      if (!Util.validarEmail(getLogin().getLogin()))
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "E-mail inválido"));
         return false;
      }

      if (Objects.isNull(getLogin().getSenha()) || getLogin().getSenha().isEmpty())
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Senha obrigatória"));
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
