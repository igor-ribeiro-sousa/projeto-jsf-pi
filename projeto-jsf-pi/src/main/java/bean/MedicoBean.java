package bean;

import java.util.ArrayList;
import java.util.Date;
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
   private String crm;
   private List<Medico> medicos;
   private List<Medico> listaResultado;
   private boolean exibirResultadosPesquisa;
   
   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;

   public MedicoBean()
   {
      this.medico = new Medico();
      this.medicos = new ArrayList<Medico>();
      this.listaResultado = new ArrayList<Medico>();
      this.exibirResultadosPesquisa = false;
   }
   
   public void inserir()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            MedicoDAO.inserir(medico);
            Util.addMensagemInfo("Médico inserido com sucesso!.");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar inserir o médico. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar inserir o médico." , e);
      }
   }
   
   public void completarInserir()
   {
      this.medico.setNome(this.medico.getNome().toUpperCase().trim());
      this.medico.setDataInclusao(new Date());
      this.medico.setFlagAtivo("S");
   }
   
   public void alterar()
   {
      try
      {
         if (validarAlterar())
         {
            completarAlterar();
            this.medico = MedicoDAO.alterar(medico);
            Util.addMensagemInfo("Médico alterado com sucesso!.");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar alterar o médico. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar alterar o médico." , e);
      }
   }

   public void completarAlterar()
   {
      this.medico.setNome(this.medico.getNome().toUpperCase().trim());
   }
   
   public void editar(Medico medico)
   {
      try
      {
         this.medico = medico;
         this.crm = medico.getCrm();
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
         if (!MedicoDAO.possuiAgendamentosAgendados(id))
         {
            MedicoDAO.excluir(id);
            Util.addMensagemWarn("Médico excluído com sucesso!");
         }
         else
         {
            Util.addMensagemErro("Este médico possui agendamentos para realizar e não pode ser excluído. "
                  + "Por favor, remova todos os agendamentos associados a ele e tente novamente.");
         }
      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar excluir o médico. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar excluir o médico.", e);        }

   }
   
   public boolean existeMedicoPorCrm(String crm)
   {
      try
      {
         return MedicoDAO.existeMedicoPorCrm(crm.toUpperCase().trim());
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao verificar a existência do médico. Por favor, tente novamente mais tarde.");
         return false;
      }
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
            this.exibirResultadosPesquisa = false;
         }

      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao pesquisar!. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao pesquisar!.", e);      
      }
   }
   
   public void navegarParaPesquisar()
   {
      this.medico = new Medico();
      navegacaoBean.setCurrentPage("medico-pesquisar.xhtml");
   }
   
   public void navegarParaAlterar()
   {
      navegacaoBean.setCurrentPage("medico-alterar.xhtml");
   }
   
   private boolean validarInserir()
   {
      if (Util.isCampoNullOrVazio(medico.getNome()))
      {
         Util.addMensagemErro("Nome do médico é obrigatório");
         return false;
      }
      
      if (Util.isCampoNullOrVazio(medico.getCrm()))
      {
         Util.addMensagemErro("CRM do médico é obrigatório");
         return false;
      }
      
      if (existeMedicoPorCrm(medico.getCrm()))
      {
         Util.addMensagemErro("Médico já cadastrado com esse CRM.");
         return false;
      }
      return true;
   }
   
   private boolean validarAlterar()
   {
      if (Util.isCampoNullOrVazio(medico.getNome()))
      {
         Util.addMensagemErro("Nome do médico é obrigatório");
         return false;
      }
      if (Util.isCampoNullOrVazio(medico.getCrm()))
      {
         Util.addMensagemErro("CRM do médico é obrigatório");
         return false;
      }
      
      if (existeMedicoPorCrm(medico.getCrm().trim()) && !medico.getCrm().equalsIgnoreCase(this.crm.trim()))
      {
         Util.addMensagemErro("Médico já cadastrado com esse CRM.");
         return false;
      }
      return true;
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