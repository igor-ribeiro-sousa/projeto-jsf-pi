package entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TBL_IMC")
public class IMC
{
   @Id
   @Column(name = "CD_IMC")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(name = "CD_PCT", unique = true, nullable = false)
   private Integer codigoPaciente;

   @OneToOne
   @JoinColumn(name = "CD_PCT", insertable = false, updatable = false)
   private Paciente paciente;

   @Column(name = "PS_PCT", nullable = false)
   private Double pesoPaciente;

   @Column(name = "AT_PCT", nullable = false)
   private Double alturaPaciente;

   @Column(name = "RT_IMC", nullable = false)
   private String resultado;

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

   public Double getPesoPaciente()
   {
      return pesoPaciente;
   }

   public void setPesoPaciente(Double pesoPaciente)
   {
      this.pesoPaciente = pesoPaciente;
   }

   public Double getAlturaPaciente()
   {
      return alturaPaciente;
   }

   public void setAlturaPaciente(Double alturaPaciente)
   {
      this.alturaPaciente = alturaPaciente;
   }

   public String getResultado()
   {
      return resultado;
   }

   public void setResultado(String resultado)
   {
      this.resultado = resultado;
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
