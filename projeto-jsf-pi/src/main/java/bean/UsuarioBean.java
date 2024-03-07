package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dao.UsuarioDAO;
import entidade.Usuario;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class UsuarioBean
{
   private Usuario usuario = new Usuario();
   private String senhaConfirmacao;
   private String emailOriginal;
   private String nomeUsuarioPesquisa;
   private List<Usuario> usuarios = new ArrayList<Usuario>();
   private List<Usuario> listaResultado = new ArrayList<Usuario>();
   private boolean exibirResultadosPesquisa = false;
   
   @ManagedProperty(value="#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;
   
   public void inserir()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            UsuarioDAO.inserir(usuario);
            mensagemTela("inserido");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         throw new JSFException(e.getMessage());
      }
   }
   
   public void alterar()
   {
      try
      {
         if (validarAlterar())
         {
            completarAlterar();
            UsuarioDAO.alterar(usuario);
            mensagemTela("alterado");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         throw new JSFException(e.getMessage());
      }
   }

   private void completarInserir()
   {
      this.usuario.setNome(this.usuario.getNome().toUpperCase().trim());
      this.usuario.setEmail(this.usuario.getEmail().toUpperCase().trim());
      this.usuario.setSenha(Util.gerarHashSenha(this.usuario.getSenha().trim()));
      this.usuario.setDataInclusao(new Date());
      this.usuario.setFlagAtivo("S");
   }
   
   private void completarAlterar()
   {
      this.usuario.setNome(this.usuario.getNome().toUpperCase().trim());
      this.usuario.setEmail(this.usuario.getEmail().toUpperCase().trim());
   }
   
   private void mensagemTela(String acao)
   {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Usuario "+acao+" com sucesso"));
   }
   
   private void navegarParaPesquisar()
   {
      this.usuario = new Usuario();
      navegacaoBean.setCurrentPage("usuario-pesquisar.xhtml");
   }
   
   public boolean pesquisarPorEmail(String email)
   {
      try
      {
         if (email != null && !("").equals(email))
         {
            return UsuarioDAO.pesquisarPorEmail(email.toUpperCase().trim());
         }

      }
      catch (JSFException e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
      }
      return false;
   }
   
   public void pesquisarPorNome()
   {
      try
      {
         if (this.nomeUsuarioPesquisa != null && !("").equals(nomeUsuarioPesquisa))
         {
            this.listaResultado = UsuarioDAO.pesquisarPorUsuario(this.nomeUsuarioPesquisa.toUpperCase().trim());
            this.exibirResultadosPesquisa = true;
         }
         else {
            this.listaResultado = UsuarioDAO.pesquisar();
         }

      }
      catch (JSFException e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
      }
   }


   public void editar(Usuario usuario)
   {
      try
      {
         this.usuario = usuario;
         this.emailOriginal = usuario.getEmail();
         navegacaoBean.setCurrentPage("usuario-alterar.xhtml");
      }
      catch (Exception e)
      {
         throw new JSFException(e.getMessage());
      }

   }
   
   public void deletar(Integer id)
   {
      try
      {
         UsuarioDAO.excluir(id);
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Usuário excluído !"));
      }
      catch (Exception e)
      {
         throw new JSFException(e.getMessage());
      }

   }

   private boolean validarInserir()
   {
      if (Objects.isNull(this.usuario.getNome()) || ("").equals(this.usuario.getNome()))
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nome usuário obrigatório"));
         return false;
      }

      if (Objects.isNull(this.usuario.getSenha()) || ("").equals(this.usuario.getSenha()))
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Senha obrigatória"));
         return false;
      }

      if (Objects.isNull(getSenhaConfirmacao()) || ("").equals(getSenhaConfirmacao()))
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Senha confirmacao obrigatória"));
         return false;
      }

      if (!this.usuario.getSenha().equals(getSenhaConfirmacao()))
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Senha de confirmação diferente"));
         return false;
      }

      if (Objects.isNull(this.usuario.getEmail()) || ("").equals(this.usuario.getEmail()))
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email obrigatório"));
         return false;
      }

      if (!Util.validarEmail(this.usuario.getEmail()))
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email inválido"));
         return false;
      }
      if (pesquisarPorEmail(this.usuario.getEmail()))
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email já cadastrado"));
         return false;
      }

      if (Objects.isNull(this.usuario.getDataNascimento()) || ("").equals(this.usuario.getDataNascimento()))
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data de nascimento obrigatória"));
         return false;
      }

      if (this.usuario.getDataNascimento().after(new Date()))
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data de nascimento nao pode ser maior que hoje"));
         return false;

      }
      return true;
   }
   
   private boolean validarAlterar()
   {
      if (Objects.isNull(this.usuario.getNome()) || ("").equals(this.usuario.getNome()))
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nome usuário obrigatório"));
         return false;
      }

      if (Objects.isNull(this.usuario.getEmail()) || ("").equals(this.usuario.getEmail()))
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email obrigatório"));
         return false;
      }

      if (!Util.validarEmail(this.usuario.getEmail()))
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email inválido"));
         return false;
      }
      if (pesquisarPorEmail(this.usuario.getEmail()) && !this.usuario.getEmail().equalsIgnoreCase(emailOriginal))
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email já cadastrado"));
         return false;
      }

      if (Objects.isNull(this.usuario.getDataNascimento()) || ("").equals(this.usuario.getDataNascimento()))
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data de nascimento obrigatória"));
         return false;
      }

      if (this.usuario.getDataNascimento().after(new Date()))
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data de nascimento nao pode ser maior que hoje"));
         return false;

      }
      return true;
   }

   public List<Usuario> getUsuarios()
   {
      if (exibirResultadosPesquisa)
      {
         return this.listaResultado;
      }
      else
      {
         if (usuarios != null)
         {
            usuarios = UsuarioDAO.pesquisar();
         }
         return usuarios;
      }
   }

   public void setUsuarios(List<Usuario> usuarios)
   {
      this.usuarios = usuarios;
   }

   public Usuario getUsuario()
   {
      return usuario;
   }

   public void setUsuario(Usuario usuario)
   {
      this.usuario = usuario;
   }

   public String getSenhaConfirmacao()
   {
      return senhaConfirmacao;
   }

   public void setSenhaConfirmacao(String senhaConfirmacao)
   {
      this.senhaConfirmacao = senhaConfirmacao;
   }

   public String getNomeUsuarioPesquisa()
   {
      return nomeUsuarioPesquisa;
   }

   public void setNomeUsuarioPesquisa(String nomeUsuarioPesquisa)
   {
      this.nomeUsuarioPesquisa = nomeUsuarioPesquisa;
   }

   public boolean isExibirResultadosPesquisa()
   {
      return exibirResultadosPesquisa;
   }

   public void setExibirResultadosPesquisa(boolean exibirResultadosPesquisa)
   {
      this.exibirResultadosPesquisa = exibirResultadosPesquisa;
   }

   public List<Usuario> getListaResultado()
   {
      return listaResultado;
   }

   public void setListaResultado(List<Usuario> listaResultado)
   {
      this.listaResultado = listaResultado;
   }

   public NavegacaoBean getNavegacaoBean()
   {
      return navegacaoBean;
   }

   public void setNavegacaoBean(NavegacaoBean navegacaoBean)
   {
      this.navegacaoBean = navegacaoBean;
   }

   public String getEmailOriginal()
   {
      return emailOriginal;
   }

   public void setEmailOriginal(String emailOriginal)
   {
      this.emailOriginal = emailOriginal;
   }

}
