package bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import dao.MedicoDAO;
import entidade.Medico;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class MedicoBean
{
   private Medico medico;
   private String nomeMedicoPesquisa;
   private List<Medico> medicos = new ArrayList<Medico>();
   private List<Medico> listaResultado = new ArrayList<Medico>();
   
   private boolean exibirResultadosPesquisa = false;
   
   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;

   public MedicoBean()
   {
      this.medico = new Medico();
   }
   
   public void editar(Medico medico)
   {
      try
      {
         this.medico = medico;
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
         MedicoDAO.excluir(id);
         Util.addMensagemWarn("Médico excluído!");
      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro inesperado!");
         throw new JSFException(e.getMessage());        }

   }
   
   public void pesquisarPorNome()
   {
      try
      {
         if (!Util.isCampoNullOrVazio(nomeMedicoPesquisa))
         {
            this.listaResultado = MedicoDAO.pesquisarPorMedico(this.nomeMedicoPesquisa.toUpperCase().trim());
            this.exibirResultadosPesquisa = true;
         }
         else
         {
            this.listaResultado = MedicoDAO.pesquisar();
         }

      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro inesperado!");
         throw new JSFException(e.getMessage());      
      }
   }
   
   public void navegarParaAlterar()
   {
      navegacaoBean.setCurrentPage("usuario-alterar.xhtml");
   }

   public Medico getMedico()
   {
      return medico;
   }

   public void setMedico(Medico medico)
   {
      this.medico = medico;
   }

   public String getNomeMedicoPesquisa()
   {
      return nomeMedicoPesquisa;
   }

   public void setNomeMedicoPesquisa(String nomeMedicoPesquisa)
   {
      this.nomeMedicoPesquisa = nomeMedicoPesquisa;
   }

   public List<Medico> getMedicos()
   {
      if (exibirResultadosPesquisa)
      {
         return this.listaResultado;
      }
      else
      {
         if (medicos != null)
         {
            medicos = MedicoDAO.pesquisar();
         }
         return medicos;
      }
   }

   public void setMedicos(List<Medico> medicos)
   {
      this.medicos = medicos;
   }

   public List<Medico> getListaResultado()
   {
      return listaResultado;
   }

   public void setListaResultado(List<Medico> listaResultado)
   {
      this.listaResultado = listaResultado;
   }

   public boolean isExibirResultadosPesquisa()
   {
      return exibirResultadosPesquisa;
   }

   public void setExibirResultadosPesquisa(boolean exibirResultadosPesquisa)
   {
      this.exibirResultadosPesquisa = exibirResultadosPesquisa;
   }

   public NavegacaoBean getNavegacaoBean()
   {
      return navegacaoBean;
   }

   public void setNavegacaoBean(NavegacaoBean navegacaoBean)
   {
      this.navegacaoBean = navegacaoBean;
   }
}