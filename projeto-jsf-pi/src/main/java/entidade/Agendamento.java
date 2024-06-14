package entidade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import enuns.Clinica;
import enuns.StatusAgendamento;

@Entity
@Table(name = "TBL_AGD")
public class Agendamento
{

   @Id
   @Column(name = "CD_AGD")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(name = "CD_PCT", nullable = false)
   private Integer codigoPaciente;

   @OneToOne
   @JoinColumn(name = "CD_PCT", insertable = false, updatable = false)
   private Paciente paciente;

   @Column(name = "CD_MDC", nullable = false)
   private Integer codigoMedico;

   @ManyToOne
   @JoinColumn(name = "CD_MDC", insertable = false, updatable = false)
   private Medico medico;

   @Column(name = "ST_AGD", nullable = false)
   @Enumerated(EnumType.STRING)
   private StatusAgendamento status;

   @Column(name = "NM_CLN", nullable = false)
   @Enumerated(EnumType.STRING)
   private Clinica clinica;

   @Column(name = "DT_AGD", nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   private Date dataHoraAgendamento;

   @Column(name = "DT_INC", nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   private Date dataInclusao;
   
   @OneToMany(mappedBy = "agendamento", cascade = CascadeType.REMOVE)
   private List<Consulta> consultas = new ArrayList<Consulta>();

   public Integer getId()
   {
      return id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   public Integer getCodigoPaciente()
   {
      return codigoPaciente;
   }

   public void setCodigoPaciente(Integer codigoPaciente)
   {
      this.codigoPaciente = codigoPaciente;
   }

   public Paciente getPaciente()
   {
      return paciente;
   }

   public void setPaciente(Paciente paciente)
   {
      this.paciente = paciente;
   }

   public Integer getCodigoMedico()
   {
      return codigoMedico;
   }

   public void setCodigoMedico(Integer codigoMedico)
   {
      this.codigoMedico = codigoMedico;
   }

   public Medico getMedico()
   {
      return medico;
   }

   public void setMedico(Medico medico)
   {
      this.medico = medico;
   }

   public StatusAgendamento getStatus()
   {
      return status;
   }

   public void setStatus(StatusAgendamento status)
   {
      this.status = status;
   }

   public Clinica getClinica()
   {
      return clinica;
   }

   public void setClinica(Clinica clinica)
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

   public List<Consulta> getConsultas()
   {
      return consultas;
   }

   public void setConsultas(List<Consulta> consultas)
   {
      this.consultas = consultas;
   }

}
