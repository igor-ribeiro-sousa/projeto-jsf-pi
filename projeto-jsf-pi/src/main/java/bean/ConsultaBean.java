package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import dao.AgendamentoDAO;
import dao.ConsultaDAO;
import dao.PacienteDAO;
import entidade.Agendamento;
import entidade.Consulta;
import entidade.Paciente;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class ConsultaBean
{
   private Consulta consulta;
   private String nomePacientePesquisa;
   private List<Paciente> pacientesCadastrados;
   private List<Agendamento> agendamentos;
   private List<Consulta> consultas;
   private List<Consulta> listaResultado;
   private boolean exibirResultadosPesquisa;

   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;

   public ConsultaBean()
   {
      this.consulta = new Consulta();
      this.consulta.setPaciente(new Paciente());
      this.consulta.setAgendamento(new Agendamento());
      this.consultas = new ArrayList<Consulta>();
      this.listaResultado = new ArrayList<Consulta>();
      this.exibirResultadosPesquisa = false;
   }

   public void atualizarAgendamentos()
   {
      if (consulta.getPaciente() != null && consulta.getPaciente().getCpf() != null)
      {
         agendamentos = AgendamentoDAO.pesquisarAgendamentosAgendados(consulta.getPaciente().getCpf());
      }
      else
      {
         agendamentos = new ArrayList<>();
      }
   }
   
   public void inserir()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            ConsultaDAO.inserir(consulta);
            Util.addMensagemInfo("Consulta realizada com sucesso!");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar inserir o IMC. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar inserir o IMC.", e);
      }
   }

   public void completarInserir()
   {
      this.consulta.setQueixaPrincipal(this.consulta.getQueixaPrincipal().toUpperCase().trim());
      this.consulta.setDataInclusao(new Date());
      this.consulta.setCodigoAgendamento(this.consulta.getAgendamento().getId());
      this.consulta.setAgendamento(null);
      completarPaciente();
   }

   private void completarPaciente()
   {
      Paciente pacientes = PacienteDAO.pesquisarPorCpfPaciente(this.consulta.getPaciente().getCpf());
      if (Objects.nonNull(pacientes))
      {
         this.consulta.setCodigoPaciente(pacientes.getId());
         this.consulta.setPaciente(null);
      }
   }
   
   public void pesquisarPorNome()
   {
      try
      {
         if (!Util.isCampoNullOrVazio(nomePacientePesquisa))
         {
            this.listaResultado = ConsultaDAO.pesquisarPorPaciente(this.nomePacientePesquisa.toUpperCase().trim());
            this.exibirResultadosPesquisa = true;
         }
         else
         {
            this.listaResultado = ConsultaDAO.pesquisar();
            this.exibirResultadosPesquisa = false;
         }

      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao pesquisar!. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao pesquisar!.", e);      
      }
   }
   
   public void alterar()
   {
      try
      {
         if (validarAlterar())
         {
            completarAlterar();
            this.consulta = ConsultaDAO.alterar(consulta);
            Util.addMensagemInfo("Consulta alterado com sucesso!.");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar alterar a consulta. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar alterar a consulta." , e);
      }
   }

   public void completarAlterar()
   {
      this.consulta.setQueixaPrincipal(this.consulta.getQueixaPrincipal().toUpperCase().trim());
      this.consulta.setCodigoAgendamento(this.consulta.getAgendamento().getId());
      this.consulta.setAgendamento(null);
      completarPaciente();
   }
   
   public void editar(Consulta consulta)
   {
      try
      {
         this.consulta = consulta;
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
         ConsultaDAO.excluir(id);
         Util.addMensagemWarn("Consulta excluída !");
      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar excluir consulta. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar excluir consulta", e);        
      }
   }
   
   
   public void navegarParaAlterar()
   {
      navegacaoBean.setCurrentPage("consulta-alterar.xhtml");
   }

   public void navegarParaPesquisar()
   {
      this.consulta = new Consulta();
      this.consulta.setPaciente(new Paciente());
      this.consulta.setAgendamento(new Agendamento());
      navegacaoBean.setCurrentPage("consulta-pesquisar.xhtml");
   }

   private boolean validarInserir()
   {
      if (Util.isCampoNullOrVazio(this.consulta.getPaciente().getCpf()))
      {
         Util.addMensagemErro("Paciente obrigatório.");
         return false;
      }

      if (Objects.isNull(this.consulta.getAgendamento().getId()))
      {
         Util.addMensagemErro("Agendamento obrigatório.");
         return false;
      }
      
      if (Util.isCampoNullOrVazio(this.consulta.getQueixaPrincipal()))
      {
         Util.addMensagemErro("Descrição da queixa obrigatória.");
         return false;
      }
      return true;
   }
   
   private boolean validarAlterar()
   {
      if (Util.isCampoNullOrVazio(this.consulta.getPaciente().getCpf()))
      {
         Util.addMensagemErro("Paciente obrigatório.");
         return false;
      }

      if (Objects.isNull(this.consulta.getAgendamento().getId()))
      {
         Util.addMensagemErro("Agendamento obrigatório.");
         return false;
      }
      
      if (Util.isCampoNullOrVazio(this.consulta.getQueixaPrincipal()))
      {
         Util.addMensagemErro("Descrição da queixa obrigatória.");
         return false;
      }
      return true;
   }

   public Consulta getConsulta()
   {
      return consulta;
   }

   public void setConsulta(Consulta consulta)
   {
      this.consulta = consulta;
   }

   public String getNomePacientePesquisa()
   {
      return nomePacientePesquisa;
   }

   public void setNomePacientePesquisa(String nomePacientePesquisa)
   {
      this.nomePacientePesquisa = nomePacientePesquisa;
   }

   public List<Paciente> getPacientesCadastrados()
   {
      pacientesCadastrados = PacienteDAO.pesquisar();
      atualizarAgendamentos();
      return pacientesCadastrados;
   }

   public void setPacientesCadastrados(List<Paciente> pacientesCadastrados)
   {
      this.pacientesCadastrados = pacientesCadastrados;
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
      if (exibirResultadosPesquisa)
      {
         return this.listaResultado;
      }
      else
      {
         if (consultas != null)
         {
            consultas = ConsultaDAO.pesquisar();
         }
         return consultas;
      }
   }

   public void setConsultas(List<Consulta> consultas)
   {
      this.consultas = consultas;
   }

   public List<Consulta> getListaResultado()
   {
      return listaResultado;
   }

   public void setListaResultado(List<Consulta> listaResultado)
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
