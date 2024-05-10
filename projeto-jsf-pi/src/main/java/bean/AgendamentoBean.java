package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import dao.AgendamentoDAO;
import dao.MedicoDAO;
import entidade.Agendamento;
import entidade.Medico;
import enuns.Clinica;
import enuns.StatusAgendamento;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class AgendamentoBean
{
   private Agendamento agendamento;
   private String nomeMedicoPesquisa;
   private List<Medico> medicosCadastrados;
   private List<Agendamento> agendamentos;
   private List<Agendamento> listaResultado;
   private boolean exibirResultadosPesquisa;
   
   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;

   public AgendamentoBean()
   {
      this.agendamento = new Agendamento();
      this.agendamento.setMedico(new Medico());
      this.agendamentos = new ArrayList<Agendamento>();
      this.listaResultado = new ArrayList<Agendamento>();
      this.exibirResultadosPesquisa = false;
   }

   public String agendar()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            AgendamentoDAO.inserir(agendamento);
            Util.addMensagemInfo("Agendamento realizado com sucesso!. Número do agendamento: " + this.agendamento.getId());
            atualizaAgendamento();
            return "index";
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar agendar o exame. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar agendar o exame.", e);
      }
      return null;
   }
   
   public void agendarLogado()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            AgendamentoDAO.inserir(agendamento);
            Util.addMensagemInfo("Agendamento realizado com sucesso!. Número do agendamento: " + this.agendamento.getId());
            atualizaAgendamento();
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar agendar o exame. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar agendar o exame.", e);
      }
   }
   
   public void navegarParaPesquisar()
   {
      this.agendamento = new Agendamento();
      this.agendamento.setMedico(new Medico());      
      navegacaoBean.setCurrentPage("agendamento-pesquisar.xhtml");
   }
   
   private void atualizaAgendamento()
   {
      this.agendamento = new Agendamento();
      this.agendamento.setMedico(new Medico());      
   }

   public void completarInserir()
   {
      this.agendamento.setNomePaciente(this.agendamento.getNomePaciente().toUpperCase().trim());
      this.agendamento.setEmailPaciente(this.agendamento.getEmailPaciente().toUpperCase().trim());
      completarMedico();
      this.agendamento.setDataInclusao(new Date());
      this.agendamento.setStatus(StatusAgendamento.AGENDADO);
   }
   
   private void completarMedico() 
   {
      List<Medico> medicos = MedicoDAO.pesquisarPorMedico(this.agendamento.getMedico().getNome().toUpperCase().trim());
      if (!medicos.isEmpty()) 
      {
         this.agendamento.setCodigoMedico(medicos.get(0).getId());
         this.agendamento.setMedico(null);
      }
   }
   
   public void pesquisarPorNome()
   {
      try
      {
         if (!Util.isCampoNullOrVazio(nomeMedicoPesquisa))
         {
            this.listaResultado = AgendamentoDAO.pesquisarPorMedico(this.nomeMedicoPesquisa.toUpperCase().trim());
            this.exibirResultadosPesquisa = true;
         }
         else
         {
            this.listaResultado = AgendamentoDAO.pesquisar();
         }

      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao pesquisar!. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao pesquisar!.", e);      
      }
   }

   public boolean existePacientePorEmail(String email)
   {
      try
      {
         return AgendamentoDAO.existePacientePorEmail(email.toUpperCase().trim());
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao verificar a existência do paciente. Por favor, tente novamente mais tarde.");
         return false;
      }
   }
   
   private boolean verificarDisponibilidadeAgendamento(Date dataHoraAgendamento)
   {
      try 
      {
         return AgendamentoDAO.existeAgendamentoNaMesmaData(dataHoraAgendamento, this.agendamento.getMedico().getNome());
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao verificar a disponibilidade do agendamento. Por favor, tente novamente mais tarde.");
         return false;
      }
   }
   
   public List<SelectItem> getClinicas()
   {
      List<SelectItem> clinica = new ArrayList<>();
      for (Clinica item : Clinica.values())
      {
         String label = item.name().replace("_", " ");
         clinica.add(new SelectItem(item, label));
      }
      return clinica;
   }
   
   private boolean validarInserir()
   {
      if (Util.isCampoNullOrVazio(agendamento.getNomePaciente()))
      {
         Util.addMensagemErro("Nome do paciente obrigatório.");
         return false;
      }

      if (Util.isCampoNullOrVazio(agendamento.getEmailPaciente()))
      {
         Util.addMensagemErro("E-mail obrigatório.");
         return false;
      }

      if (!Util.validarEmail(agendamento.getEmailPaciente().trim()))
      {
         Util.addMensagemErro("E-mail inválido.");
         return false;
      }

      if (Objects.isNull(agendamento.getClinica()))
      {
         Util.addMensagemErro("Nome da clínica obrigatório.");
         return false;
      }
      
      if (Objects.isNull(agendamento.getMedico().getNome()))
      {
         Util.addMensagemErro("Nome do médico obrigatório.");
         return false;
      }
      
      if (Objects.isNull(agendamento.getDataHoraAgendamento()))
      {
         Util.addMensagemErro("Data do agendamento obrigatória.");
         return false;
      }
      
      if (verificarDisponibilidadeAgendamento(agendamento.getDataHoraAgendamento())) 
      {
         Util.addMensagemErro("Data do agendamento indisponível.");
         return false;
      }

      if (agendamento.getDataHoraAgendamento().before(new Date()))
      {
         Util.addMensagemErro("Data do agendamento não pode ser no passado.");
         return false;
      }

      return true;
   }
   
   public Agendamento getAgendamento()
   {
      return agendamento;
   }

   public void setAgendamento(Agendamento agendamento)
   {
      this.agendamento = agendamento;
   }
   
   public String getNomeMedicoPesquisa()
   {
      return nomeMedicoPesquisa;
   }

   public void setNomeMedicoPesquisa(String nomeMedicoPesquisa)
   {
      this.nomeMedicoPesquisa = nomeMedicoPesquisa;
   }

   public List<Medico> getMedicosCadastrados()
   {
      medicosCadastrados = MedicoDAO.pesquisar();
      return medicosCadastrados;
   }

   public List<Agendamento> getAgendamentos()
   {
      if (exibirResultadosPesquisa)
      {
         return this.listaResultado;
      }
      else
      {
         if (agendamentos != null)
         {
            agendamentos = AgendamentoDAO.pesquisar();
         }
         return agendamentos;
      }
   }

   public void setAgendamentos(List<Agendamento> agendamentos)
   {
      this.agendamentos = agendamentos;
   }

   public List<Agendamento> getListaResultado()
   {
      return listaResultado;
   }

   public void setListaResultado(List<Agendamento> listaResultado)
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