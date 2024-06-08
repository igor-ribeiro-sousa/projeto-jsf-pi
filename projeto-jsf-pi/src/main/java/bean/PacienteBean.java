package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import dao.PacienteDAO;
import entidade.Paciente;
import enuns.Sexo;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class PacienteBean
{
   private Paciente paciente;
   private String cpf;
   private String emailOriginal;
   private String nomePacientePesquisa;
   private List<Paciente> pacientes;
   private List<Paciente> listaResultado;
   private boolean exibirResultadosPesquisa;
   
   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;
   
   public PacienteBean()
   {
      this.paciente = new Paciente();
      this.pacientes = new ArrayList<Paciente>();
      this.listaResultado = new ArrayList<Paciente>();
      this.exibirResultadosPesquisa = false;
   }
   
   public void inserir()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            PacienteDAO.inserir(paciente);
            Util.addMensagemInfo("Paciente realizado com sucesso!");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar inserir o paciente. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar inserir o paciente.", e);
      }
   } 
   
   public void completarInserir()
   {
      this.paciente.setNome(this.paciente.getNome().toUpperCase().trim());
      this.paciente.setCpf(this.paciente.getCpf().trim());
      this.paciente.setEmail(this.paciente.getEmail().toUpperCase().trim());
      this.paciente.setFlagAtivo("S");
      this.paciente.setDataInclusao(new Date());
   }
   
   public void navegarParaPesquisar()
   {
      this.paciente = new Paciente();
      navegacaoBean.setCurrentPage("paciente-pesquisar.xhtml");
   }
   
   public void alterar()
   {
      try
      {
         if (validarAlterar())
         {
            completarAlterar();
            this.paciente = PacienteDAO.alterar(paciente);
            Util.addMensagemInfo("Paciente alterado com sucesso!.");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar alterar o paciente. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar alterar o paciente." , e);
      }
   }

   public void completarAlterar()
   {
      this.paciente.setNome(this.paciente.getNome().toUpperCase().trim());
      this.paciente.setCpf(this.paciente.getCpf().trim());
      this.paciente.setEmail(this.paciente.getEmail().toUpperCase().trim());   
   }
   
   public void pesquisarPorNome()
   {
      try
      {
         if (!Util.isCampoNullOrVazio(nomePacientePesquisa))
         {
            this.listaResultado = PacienteDAO.pesquisarPorPaciente(this.nomePacientePesquisa.toUpperCase().trim());
            this.exibirResultadosPesquisa = true;
         }
         else
         {
            this.listaResultado = PacienteDAO.pesquisar();
            this.exibirResultadosPesquisa = false;
         }

      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao pesquisar!. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao pesquisar!.", e);      
      }
   }
   
   public void editar(Paciente paciente)
   {
      try
      {
         this.paciente = paciente;
         this.cpf = paciente.getCpf();
         this.emailOriginal = paciente.getEmail();
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
         if (!PacienteDAO.possuiAgendamentosAgendados(id))
         {
            PacienteDAO.excluir(id);
            Util.addMensagemWarn("Paciente excluído com sucesso!");
         }
         else
         {
            Util.addMensagemErro("Este paciente possui agendamentos para realizar e não pode ser excluído. "
                  + "Por favor, remova todos os agendamentos associados a ele e tente novamente.");
         }
      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar excluir o paciente. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar excluir o paciente.", e);        }

   }
   
   public void navegarParaAlterar()
   {
      navegacaoBean.setCurrentPage("paciente-alterar.xhtml");
   }
   
   public boolean existePacientePorCpf(String cpf)
   {
      try
      {
         return PacienteDAO.existePacientePorCpf(cpf);
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro inesperado!");
         return false;
      }
   }
   
   public boolean existePacientePorEmail(String email)
   {
      try
      {
         return PacienteDAO.existePacientePorEmail(email.toUpperCase().trim());
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro inesperado!");
         return false;
      }
   }
   
   private boolean validarInserir()
   {
      if (Util.isCampoNullOrVazio(paciente.getNome()))
      {
         Util.addMensagemErro("Nome paciente obrigatório.");
         return false;
      }

      if (Util.isCampoNullOrVazio(paciente.getCpf()))
      {
         Util.addMensagemErro("CPF obrigatória.");
         return false;
      }
      
      if (!Util.validarCPF(paciente.getCpf()))
      {
         Util.addMensagemErro("CPF Inválido.");
         return false;
      }
      
      if (existePacientePorCpf(paciente.getCpf()))
      {
         Util.addMensagemErro("Paciente já cadastrado com esse CPF.");
         return false;
      }
      
      if (paciente.getSexo() == null)
      {
         Util.addMensagemErro("Sexo obrigátorio.");
         return false;
      }

      if (Util.isCampoNullOrVazio(paciente.getEmail()))
      {
         Util.addMensagemErro("E-mail obrigatório.");
         return false;
      }

      if (!Util.validarEmail(paciente.getEmail().trim()))
      {
         Util.addMensagemErro("E-mail inválido");
         return false;
      }
      
      if (existePacientePorEmail(paciente.getEmail().trim()))
      {
         Util.addMensagemErro("E-mail já cadastrado");
         return false;
      }

      if (Objects.isNull(paciente.getDataNascimento()))
      {
         Util.addMensagemErro("Data de nascimento obrigatória");
         return false;
      }

      if (paciente.getDataNascimento().after(new Date()))
      {
         Util.addMensagemErro("Data de nascimento não pode ser maior que hoje");
         return false;
      }

      return true;
   }
   
   private boolean validarAlterar()
   {
      if (Util.isCampoNullOrVazio(paciente.getNome()))
      {
         Util.addMensagemErro("Nome paciente obrigatório.");
         return false;
      }

      if (Util.isCampoNullOrVazio(paciente.getCpf()))
      {
         Util.addMensagemErro("CPF obrigatória.");
         return false;
      }
      
      if (!Util.validarCPF(paciente.getCpf()))
      {
         Util.addMensagemErro("CPF Inválido.");
         return false;
      }
      
      if (existePacientePorCpf(paciente.getCpf().trim()) && !paciente.getCpf().equalsIgnoreCase(this.cpf.trim()))
      {
         Util.addMensagemErro("CPF já cadastrado");
         return false;
      }

      if (Util.isCampoNullOrVazio(paciente.getEmail()))
      {
         Util.addMensagemErro("E-mail obrigatório.");
         return false;
      }

      if (!Util.validarEmail(paciente.getEmail().trim()))
      {
         Util.addMensagemErro("E-mail inválido");
         return false;
      }
      
      if (existePacientePorEmail(paciente.getEmail().trim()) && !paciente.getEmail().equalsIgnoreCase(this.emailOriginal.trim()))
      {
         Util.addMensagemErro("E-mail já cadastrado");
         return false;
      }

      if (Objects.isNull(paciente.getDataNascimento()))
      {
         Util.addMensagemErro("Data de nascimento obrigatória");
         return false;
      }

      if (paciente.getDataNascimento().after(new Date()))
      {
         Util.addMensagemErro("Data de nascimento não pode ser maior que hoje");
         return false;
      }

      return true;
   }
   
   public List<SelectItem> getSexo()
   {
      List<SelectItem> sexo = new ArrayList<>();
      for (Sexo item : Sexo.values())
      {
         sexo.add(new SelectItem(item, item.name()));
      }
      return sexo;
   }

   public Paciente getPaciente()
   {
      return paciente;
   }

   public void setPaciente(Paciente paciente)
   {
      this.paciente = paciente;
   }

   public String getNomePacientePesquisa()
   {
      return nomePacientePesquisa;
   }

   public void setNomePacientePesquisa(String nomePacientePesquisa)
   {
      this.nomePacientePesquisa = nomePacientePesquisa;
   }

   public List<Paciente> getPacientes()
   {
      if (exibirResultadosPesquisa)
      {
         return this.listaResultado;
      }
      else
      {
         if (pacientes != null)
         {
            pacientes = PacienteDAO.pesquisar();
         }
         return pacientes;
      }
   }

   public void setPacientes(List<Paciente> pacientes)
   {
      this.pacientes = pacientes;
   }

   public List<Paciente> getListaResultado()
   {
      return listaResultado;
   }

   public void setListaResultado(List<Paciente> listaResultado)
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
