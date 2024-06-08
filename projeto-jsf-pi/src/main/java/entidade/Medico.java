package entidade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TBL_MDC")
public class Medico
{
   @Id
   @Column(name = "CD_MDC")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(name = "NM_MDC", nullable = false)
   private String nome;
   
   @Column(name = "CD_CRM", unique = true, nullable = false)
   private String crm;

   @Column(name = "FG_ATV", nullable = false)
   private String flagAtivo;

   @Column(name = "DT_INC", nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   private Date dataInclusao;
   
   @OneToMany(mappedBy = "medico", cascade = CascadeType.REMOVE)
   private List<Agendamento> agendamentos = new ArrayList<Agendamento>();

   public Integer getId()
   {
      return id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   public String getNome()
   {
      return nome;
   }

   public void setNome(String nome)
   {
      this.nome = nome;
   }
   
   public String getCrm()
   {
      return crm;
   }

   public void setCrm(String crm)
   {
      this.crm = crm;
   }

   public String getFlagAtivo()
   {
      return flagAtivo;
   }

   public void setFlagAtivo(String flagAtivo)
   {
      this.flagAtivo = flagAtivo;
   }

   public Date getDataInclusao()
   {
      return dataInclusao;
   }

   public void setDataInclusao(Date dataInclusao)
   {
      this.dataInclusao = dataInclusao;
   }

   public List<Agendamento> getAgendamentos()
   {
      return agendamentos;
   }

   public void setAgendamentos(List<Agendamento> agendamentos)
   {
      this.agendamentos = agendamentos;
   }
}
