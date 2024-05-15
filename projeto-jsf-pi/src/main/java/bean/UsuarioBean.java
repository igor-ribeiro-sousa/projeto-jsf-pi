package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import dao.UsuarioDAO;
import entidade.Usuario;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class UsuarioBean
{
   private Usuario usuario;
   private String senhaConfirmacao;
   private String emailOriginal;
   private String senhaAtual;
   private String nomeUsuarioPesquisa;
   private List<Usuario> usuarios;
   private List<Usuario> listaResultado;

   private boolean exibirResultadosPesquisa;

   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;
   
   @ManagedProperty(value = "#{loginBean}")
   private LoginBean loginBean;
   
   public UsuarioBean() 
   {
      this.usuario = new Usuario();
      this.usuarios = new ArrayList<Usuario>();
      this.listaResultado = new ArrayList<Usuario>();
      this.exibirResultadosPesquisa = false;
   }

   public void inserir()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            UsuarioDAO.inserir(usuario);
            Util.addMensagemInfo("Usuário inserido com sucesso!");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar inserir o usuário. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar inserir o usuário.", e);
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
   
   public void navegarParaPesquisar()
   {
      this.usuario = new Usuario();
      navegacaoBean.setCurrentPage("usuario-pesquisar.xhtml");
   }

   public void alterar()
   {
      try
      {
         if (validarAlterar())
         {
            completarAlterar();
            this.usuario = UsuarioDAO.alterar(usuario);
            this.loginBean.setUsuario(usuario);
            Util.addMensagemInfo("Usuário alterado com sucesso!");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar alterar o usuário. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar alterar o usuário.", e);
      }
   }

   public void completarAlterar()
   {
      this.usuario.setNome(this.usuario.getNome().toUpperCase().trim());
      this.usuario.setEmail(this.usuario.getEmail().toUpperCase().trim());
   }
   
   public void navegarParaAlterarSenha()
   {
      navegacaoBean.setCurrentPage("usuario-alterar-senha.xhtml");
   }
   
   public void alterarSenha()
   {
      Usuario usuarioOld = UsuarioDAO.getUsuario(this.usuario.getId());
      if(validarSenha(usuarioOld))
      {
         this.usuario.setSenha(Util.gerarHashSenha(usuario.getSenha().trim()));
         this.usuario = UsuarioDAO.alterar(this.usuario);
         Util.addMensagemInfo("Senha alterada com sucesso!");
         navegarParaAlterar();
      }
   }
   
   public void navegarParaAlterar()
   {
      navegacaoBean.setCurrentPage("usuario-alterar.xhtml");
   }

   public boolean existeUsuarioPorEmail(String email)
   {
      try
      {
         return UsuarioDAO.existeUsuarioPorEmail(email.toUpperCase().trim());
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro inesperado!");
         return false;
      }
   }

   public void pesquisarPorNome()
   {
      try
      {
         if (!Util.isCampoNullOrVazio(nomeUsuarioPesquisa))
         {
            this.listaResultado = UsuarioDAO.pesquisarPorUsuario(this.nomeUsuarioPesquisa.toUpperCase().trim());
            this.exibirResultadosPesquisa = true;
         }
         else
         {
            this.listaResultado = UsuarioDAO.pesquisar();
            this.exibirResultadosPesquisa = false;
         }

      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao tentar pesquisar o usuário");
         throw new JSFException("Erro ao tentar pesquisar o usuário", e);      
      }
   }

   public void editar(Usuario usuario)
   {
      try
      {
         this.usuario = usuario;
         this.emailOriginal = usuario.getEmail();
         navegarParaAlterar();
      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro inesperado!");
         throw new JSFException(e.getMessage());        }

   }

   public void deletar(Integer id)
   {
      try
      {
         UsuarioDAO.excluir(id);
         Util.addMensagemWarn("Usuário excluído !");
      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar exluir usuário. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar exluir usuário", e);        
      }

   }

   private boolean validarInserir()
   {
      if (Util.isCampoNullOrVazio(usuario.getNome()))
      {
         Util.addMensagemErro("Nome usuário obrigatório");
         return false;
      }

      if (Util.isCampoNullOrVazio(usuario.getSenha()))
      {
         Util.addMensagemErro("Senha obrigatória");
         return false;
      }

      if (Util.isCampoNullOrVazio(getSenhaConfirmacao()))
      {
         Util.addMensagemErro("Senha confirmação obrigatória");
         return false;
      }

      if (!usuario.getSenha().trim().equals(getSenhaConfirmacao().trim()))
      {
         Util.addMensagemErro("Senha de confirmação diferente");
         return false;
      }

      if (Util.isCampoNullOrVazio(usuario.getEmail()))
      {
         Util.addMensagemErro("E-mail obrigatório");
         return false;
      }

      if (!Util.validarEmail(usuario.getEmail().trim()))
      {
         Util.addMensagemErro("E-mail inválido");
         return false;
      }

      if (existeUsuarioPorEmail(usuario.getEmail().trim()))
      {
         Util.addMensagemErro("E-mail já cadastrado");
         return false;
      }

      if (Objects.isNull(usuario.getDataNascimento()))
      {
         Util.addMensagemErro("Data de nascimento obrigatória");
         return false;
      }

      if (usuario.getDataNascimento().after(new Date()))
      {
         Util.addMensagemErro("Data de nascimento não pode ser maior que hoje");
         return false;
      }

      return true;
   }

   private boolean validarAlterar()
   {
      if (Util.isCampoNullOrVazio(usuario.getNome()))
      {
         Util.addMensagemErro("Nome usuário obrigatório");
         return false;
      }

      if (Util.isCampoNullOrVazio(usuario.getEmail()))
      {
         Util.addMensagemErro("E-mail obrigatório");
         return false;
      }

      if (!Util.validarEmail(usuario.getEmail().trim()))
      {
         Util.addMensagemErro("E-mail inválido");
         return false;
      }

      if (existeUsuarioPorEmail(usuario.getEmail().trim()) && !usuario.getEmail().equalsIgnoreCase(emailOriginal.trim()))
      {
         Util.addMensagemErro("E-mail já cadastrado");
         return false;
      }

      if (Objects.isNull(usuario.getDataNascimento()))
      {
         Util.addMensagemErro("Data de nascimento obrigatória");
         return false;
      }

      if (usuario.getDataNascimento().after(new Date()))
      {
         Util.addMensagemErro("Data de nascimento não pode ser maior que hoje");
         return false;
      }

      return true;
   }
   
   private boolean validarSenha(Usuario usuarioOld)
   {
      if (Util.isCampoNullOrVazio(this.senhaAtual))
      {
         Util.addMensagemErro("A senha atual é obrigatoria!");
         return false;
      }
      
      if (!Util.gerarHashSenha(this.senhaAtual).equals(usuarioOld.getSenha()))
      {
         Util.addMensagemErro("Senha inválida!");
         return false;
      }
      
      if (this.senhaAtual.equals(usuario.getSenha()))
      {
         Util.addMensagemErro("A nova senha deve ser diferente da atual!");
         return false;
      }
      
      if (Util.isCampoNullOrVazio(usuario.getSenha()))
      {
         Util.addMensagemErro("Senha obrigatória");
         return false;
      }
      
         
      if (Util.isCampoNullOrVazio(getSenhaConfirmacao()))
      {
         Util.addMensagemErro("Senha confirmação obrigatória");
         return false;
      }

      if (!usuario.getSenha().trim().equals(getSenhaConfirmacao().trim()))
      {
         Util.addMensagemErro("Senha de confirmação diferente");
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

   public String getSenhaAtual()
   {
      return senhaAtual;
   }

   public void setSenhaAtual(String senhaAtual)
   {
      this.senhaAtual = senhaAtual;
   }

   public LoginBean getLoginBean()
   {
      return loginBean;
   }

   public void setLoginBean(LoginBean loginBean)
   {
      this.loginBean = loginBean;
   }
}
