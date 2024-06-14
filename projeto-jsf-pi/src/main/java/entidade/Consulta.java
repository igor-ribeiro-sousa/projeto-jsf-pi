package entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TBL_CST")
public class Consulta
{
   @Id
   @Column(name = "CD_CST")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(name = "CD_PCT", nullable = false)
   private Integer codigoPaciente;

   @OneToOne
   @JoinColumn(name = "CD_PCT", insertable = false, updatable = false)
   private Paciente paciente;
   
   @Column(name = "CD_AGD", nullable = false)
   private Integer codigoAgendamento;

   @OneToOne
   @JoinColumn(name = "CD_AGD", insertable = false, updatable = false)
   private Agendamento agendamento;

   @Column(name = "QX_PRC", nullable = false)
   private String queixaPrincipal;

   @Column(name = "DT_INC", nullable = false)
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

   public Integer getCodigoAgendamento()
   {
      return codigoAgendamento;
   }

   public void setCodigoAgendamento(Integer codigoAgendamento)
   {
      this.codigoAgendamento = codigoAgendamento;
   }

   public Agendamento getAgendamento()
   {
      return agendamento;
   }

   public void setAgendamento(Agendamento agendamento)
   {
      this.agendamento = agendamento;
   }

   public String getQueixaPrincipal()
   {
      return queixaPrincipal;
   }

   public void setQueixaPrincipal(String queixaPrincipal)
   {
      this.queixaPrincipal = queixaPrincipal;
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
