package bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import entidade.Agendamento;
import enuns.StatusAgendamento;

@ManagedBean
@SessionScoped
public class AgendamentoBean
{
   private Agendamento agendamento;

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

   public void agendar()
   {
      agendamento = new Agendamento();
   }

   public Agendamento getAgendamento()
   {
      return agendamento;
   }

   public void setAgendamento(Agendamento agendamento)
   {
      this.agendamento = agendamento;
   }

}