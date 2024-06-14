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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import enuns.Sexo;

@Entity
@Table(name = "TBL_PCT")
public class Paciente
{
   @Id
   @Column(name = "CD_PCT")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(name = "DC_PCT", unique = true, nullable = false)
   private String cpf;

   @Column(name = "NM_PCT", nullable = false)
   private String nome;
   
   @Column(name = "EM_PCT", unique = true, nullable = false)
   private String email;
   
   @Column(name = "SX_PCT", nullable = false)
   @Enumerated(EnumType.STRING)
   private Sexo sexo;

   @Column(name = "FG_ATV", nullable = false)
   private String flagAtivo;

   @Column(name = "DT_NSC", nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   private Date dataNascimento;

   @Column(name = "DT_INC", nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   private Date dataInclusao;
   
   @OneToMany(mappedBy = "paciente", cascade = CascadeType.REMOVE)
   private List<IMC> imcs = new ArrayList<IMC>();
   
   @OneToMany(mappedBy = "paciente", cascade = CascadeType.REMOVE)
   private List<Agendamento> agendamentos = new ArrayList<Agendamento>();
   
   @OneToMany(mappedBy = "paciente", cascade = CascadeType.REMOVE)
   private List<Consulta> consultas = new ArrayList<Consulta>();

   public Integer getId()
   {
      return id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   public String getCpf()
   {
      return cpf;
   }

   public void setCpf(String cpf)
   {
      this.cpf = cpf;
   }

   public String getNome()
   {
      return nome;
   }

   public void setNome(String nome)
   {
      this.nome = nome;
   }
   
   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public Sexo getSexo()
   {
      return sexo;
   }

   public void setSexo(Sexo sexo)
   {
      this.sexo = sexo;
   }

   public String getFlagAtivo()
   {
      return flagAtivo;
   }

   public void setFlagAtivo(String flagAtivo)
   {
      this.flagAtivo = flagAtivo;
   }

   public Date getDataNascimento()
   {
      return dataNascimento;
   }

   public void setDataNascimento(Date dataNascimento)
   {
      this.dataNascimento = dataNascimento;
   }

   public Date getDataInclusao()
   {
      return dataInclusao;
   }

   public void setDataInclusao(Date dataInclusao)
   {
      this.dataInclusao = dataInclusao;
   }

   public List<IMC> getImcs()
   {
      return imcs;
   }

   public void setImcs(List<IMC> imcs)
   {
      this.imcs = imcs;
   }

   public List<Agendamento> getAgendamentos()
   {
      return agendamentos;
   }

   public void setAgendamentos(List<Agendamento> agendamentos)
   {
      this.agendamentos = agendamentos;
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
