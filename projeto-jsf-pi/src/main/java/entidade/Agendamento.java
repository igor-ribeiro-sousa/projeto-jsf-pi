package entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import enuns.StatusAgendamento;

@Entity
@Table(name = "TBL_AGD")
public class Agendamento
{

   @Id
   @Column(name = "CD_AGD")
   @GeneratedValue
   private Integer id;

   @Column(name = "NM_PCT")
   private String nomePaciente;

   @Column(name = "NM_MDC")
   private String nomeMedico;

   @Column(name = "EM_PCT")
   private String emailPaciente;

   @Column(name = "ST_AGD")
   @Enumerated(EnumType.STRING)
   private StatusAgendamento status;

   @Column(name = "NM_CLN")
   private String clinica;

   @Column(name = "DT_AGD")
   @Temporal(TemporalType.TIMESTAMP)
   private Date dataHoraAgendamento;

   @Column(name = "DT_INC")
   @Temporal(TemporalType.TIMESTAMP)
   private Date dataInclusao;

   public Integer getId()
   {
      return id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   public String getNomePaciente()
   {
      return nomePaciente;
   }

   public void setNomePaciente(String nomePaciente)
   {
      this.nomePaciente = nomePaciente;
   }

   public String getNomeMedico()
   {
      return nomeMedico;
   }

   public void setNomeMedico(String nomeMedico)
   {
      this.nomeMedico = nomeMedico;
   }

   public String getEmailPaciente()
   {
      return emailPaciente;
   }

   public void setEmailPaciente(String emailPaciente)
   {
      this.emailPaciente = emailPaciente;
   }

   public StatusAgendamento getStatus()
   {
      return status;
   }

   public void setStatus(StatusAgendamento status)
   {
      this.status = status;
   }

   public String getClinica()
   {
      return clinica;
   }

   public void setClinica(String clinica)
   {
      this.clinica = clinica;
   }

   public Date getDataHoraAgendamento()
   {
      return dataHoraAgendamento;
   }

   public void setDataHoraAgendamento(Date dataHoraAgendamento)
   {
      this.dataHoraAgendamento = dataHoraAgendamento;
   }

   public Date getDataInclusao()
   {
      return dataInclusao;
   }

   public void setDataInclusao(Date dataInclusao)
   {
      this.dataInclusao = dataInclusao;
   }

}
