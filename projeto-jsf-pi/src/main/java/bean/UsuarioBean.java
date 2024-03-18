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
   
   private String nomeUsuarioLogado;

   private String senhaConfirmacao;

   private String emailOriginal;

   private String nomeUsuarioPesquisa;

   private List<Usuario> usuarios = new ArrayList<Usuario>();

   private List<Usuario> listaResultado = new ArrayList<Usuario>();

   private boolean exibirResultadosPesquisa = false;

   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;

   public void inserir()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            UsuarioDAO.inserir(usuario);
            mensagemTela("inserido com sucesso!");
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
            mensagemTela("alterado com sucesso!");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         throw new JSFException(e.getMessage());
      }
   }

   public void completarInserir()
   {
      this.usuario.setNome(this.usuario.getNome().toUpperCase().trim());
      this.usuario.setEmail(this.usuario.getEmail().toUpperCase().trim());
      this.usuario.setSenha(Util.gerarHashSenha(this.usuario.getSenha().trim()));
      this.usuario.setDataInclusao(new Date());
      this.usuario.setFlagAtivo("S");
   }

   public void completarAlterar()
   {
      this.usuario.setNome(this.usuario.getNome().toUpperCase().trim());
      this.usuario.setEmail(this.usuario.getEmail().toUpperCase().trim());
   }

   public void mensagemTela(String mensagem)
   {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Usuario " + mensagem));
   }

   public void navegarParaPesquisar()
   {
      this.usuario = new Usuario();
      navegacaoBean.setCurrentPage("usuario-pesquisar.xhtml");
   }
   
   public String pesquisarUsuarioPorEmail(String email)
   {
      try
      {
         if (email != null && !("").equals(email))
         {
            setNomeUsuarioLogado(UsuarioDAO.pesquisarUsuarioPorEmail(email.toUpperCase().trim()));
            return getNomeUsuarioLogado();
         }

      }
      catch (JSFException e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
      }
      return null;
   }

   public boolean existeUsuarioPorEmail(String email)
   {
      if (email == null || email.trim().isEmpty())
      {
         return false;
      }

      try
      {
         return UsuarioDAO.existeUsuarioPorEmail(email.toUpperCase().trim());
      }
      catch (JSFException e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
         return false;
      }
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
         else
         {
            this.listaResultado = UsuarioDAO.pesquisar();
         }

      }
      catch (JSFException e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
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
      if (isCampoVazio(usuario.getNome()))
      {
         addMensagemErro("Nome usuário obrigatório");
         return false;
      }

      if (isCampoVazio(usuario.getSenha()))
      {
         addMensagemErro("Senha obrigatória");
         return false;
      }

      if (isCampoVazio(getSenhaConfirmacao()))
      {
         addMensagemErro("Senha confirmação obrigatória");
         return false;
      }

      if (!usuario.getSenha().equals(getSenhaConfirmacao()))
      {
         addMensagemErro("Senha de confirmação diferente");
         return false;
      }

      if (isCampoVazio(usuario.getEmail()))
      {
         addMensagemErro("E-mail obrigatório");
         return false;
      }

      if (!Util.validarEmail(usuario.getEmail()))
      {
         addMensagemErro("E-mail inválido");
         return false;
      }

      if (existeUsuarioPorEmail(usuario.getEmail()))
      {
         addMensagemErro("E-mail já cadastrado");
         return false;
      }

      if (Objects.isNull(usuario.getDataNascimento()))
      {
         addMensagemErro("Data de nascimento obrigatória");
         return false;
      }

      if (usuario.getDataNascimento().after(new Date()))
      {
         addMensagemErro("Data de nascimento não pode ser maior que hoje");
         return false;
      }

      return true;
   }

   private boolean validarAlterar()
   {
      if (isCampoVazio(usuario.getNome()))
      {
         addMensagemErro("Nome usuário obrigatório");
         return false;
      }

      if (isCampoVazio(usuario.getEmail()))
      {
         addMensagemErro("E-mail obrigatório");
         return false;
      }

      if (!Util.validarEmail(usuario.getEmail()))
      {
         addMensagemErro("E-mail inválido");
         return false;
      }

      if (existeUsuarioPorEmail(usuario.getEmail()) && !usuario.getEmail().equalsIgnoreCase(emailOriginal))
      {
         addMensagemErro("E-mail já cadastrado");
         return false;
      }

      if (Objects.isNull(usuario.getDataNascimento()))
      {
         addMensagemErro("Data de nascimento obrigatória");
         return false;
      }

      if (usuario.getDataNascimento().after(new Date()))
      {
         addMensagemErro("Data de nascimento não pode ser maior que hoje");
         return false;
      }

      return true;
   }

   private boolean isCampoVazio(String campo)
   {
      return campo == null || campo.trim().isEmpty();
   }

   private void addMensagemErro(String mensagem)
   {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensagem));
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

   public String getNomeUsuarioLogado()
   {
      return nomeUsuarioLogado;
   }

   public void setNomeUsuarioLogado(String nomeUsuarioLogado)
   {
      this.nomeUsuarioLogado = nomeUsuarioLogado;
   }

}
