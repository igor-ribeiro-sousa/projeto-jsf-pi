package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import dao.AgendamentoDAO;
import dao.MedicoDAO;
import entidade.Agendamento;
import entidade.Medico;
import enuns.StatusAgendamento;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class AgendamentoBean
{
   private Agendamento agendamento;
   private List<Medico> medicosCadastrados;

   public AgendamentoBean()
   {
      this.agendamento = new Agendamento();
   }

   public List<SelectItem> getStatus()
   {
      List<SelectItem> status = new ArrayList<>();
      for (StatusAgendamento item : StatusAgendamento.values())
      {
         status.add(new SelectItem(item, item.name()));
      }
      return status;
   }

   public String agendar()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            AgendamentoDAO.inserir(agendamento);
            Util.addMensagemInfo("Agendamento realizado com sucesso!");
            return "index";
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro inesperado!");
         throw new JSFException(e.getMessage());
      }
      return null;
   }
   
   public void completarInserir()
   {
      this.agendamento.setNomePaciente(this.agendamento.getNomePaciente().toUpperCase().trim());
      this.agendamento.setEmailPaciente(this.agendamento.getEmailPaciente().toUpperCase().trim());
      this.agendamento.setClinica(this.agendamento.getClinica().toUpperCase().trim());
      this.agendamento.setNomeMedico(this.agendamento.getNomeMedico().toUpperCase().trim());
      this.agendamento.setDataInclusao(new Date());
      this.agendamento.setStatus(StatusAgendamento.AGENDADO);
   }

   public Agendamento getAgendamento()
   {
      return agendamento;
   }
   
   public boolean existePacientePorEmail(String email)
   {
      if (Util.isCampoNullOrVazio(email))
      {
         return false;
      }

      try
      {
         return AgendamentoDAO.existePacientePorEmail(email.toUpperCase().trim());
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro inesperado!");
         return false;
      }
   }
   
   private boolean validarInserir()
   {
      if (Util.isCampoNullOrVazio(agendamento.getNomePaciente()))
      {
         Util.addMensagemErro("Nome do paciente obrigatório");
         return false;
      }

      if (Util.isCampoNullOrVazio(agendamento.getEmailPaciente()))
      {
         Util.addMensagemErro("E-mail obrigatório");
         return false;
      }

      if (!Util.validarEmail(agendamento.getEmailPaciente().trim()))
      {
         Util.addMensagemErro("E-mail inválido");
         return false;
      }

      if (existePacientePorEmail(agendamento.getEmailPaciente().trim()))
      {
         Util.addMensagemErro("E-mail já cadastrado");
         return false;
      }

      if (Objects.isNull(agendamento.getClinica()))
      {
         Util.addMensagemErro("Nome da clínica obrigatório");
         return false;
      }
      
      if (Objects.isNull(agendamento.getNomeMedico()))
      {
         Util.addMensagemErro("Nome do médico obrigatório");
         return false;
      }

      if (agendamento.getDataHoraAgendamento().before(new Date()))
      {
         Util.addMensagemErro("Data do agendamento não pode ser no passado");
         return false;
      }

      return true;
   }

   public void setAgendamento(Agendamento agendamento)
   {
      this.agendamento = agendamento;
   }

   public List<Medico> getMedicosCadastrados()
   {
      medicosCadastrados = MedicoDAO.pesquisar();
      return medicosCadastrados;
   }

}