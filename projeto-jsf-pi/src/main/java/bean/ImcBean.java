package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import dao.ImcDAO;
import dao.PacienteDAO;
import entidade.IMC;
import entidade.Paciente;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class ImcBean
{
   private IMC imc;

   private String nomePacientePesquisa;

   private List<Paciente> pacientesCadastrados;

   private List<IMC> imcs;

   private List<IMC> listaResultado;

   private boolean exibirResultadosPesquisa;

   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;

   public ImcBean()
   {
      this.imc = new IMC();
      this.imc.setPaciente(new Paciente());
      this.pacientesCadastrados = new ArrayList<Paciente>();
      this.imcs = new ArrayList<IMC>();
      this.listaResultado = new ArrayList<IMC>();
      this.exibirResultadosPesquisa = false;
   }

   public void inserir()
   {
      try
      {
         if (validarInserir())
         {
            completarInserir();
            calcularImc();
            ImcDAO.inserir(imc);
            Util.addMensagemInfo("IMC calculado com sucesso!");
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
      this.imc.setDataInclusao(new Date());
      completarPaciente();
   }

   private void completarPaciente()
   {
      List<Paciente> pacientes = PacienteDAO.pesquisarPorPaciente(this.imc.getPaciente().getNome().toUpperCase().trim());
      if (!pacientes.isEmpty())
      {
         this.imc.setCodigoPaciente(pacientes.get(0).getId());
         this.imc.setPaciente(null);
      }
   }

   private void calcularImc()
   {
      double peso = this.imc.getPesoPaciente();
      double altura = this.imc.getAlturaPaciente();

      double resultadoImc = peso / Math.pow(altura, 2);
      String resultadoImcFormatado = String.format(Locale.US, "%.2f", resultadoImc);

      String classificacao;
      if (resultadoImc >= 40)
      {
         classificacao = "OBESIDADE GRAU 3";
      }
      else if (resultadoImc >= 35)
      {
         classificacao = "OBESIDADE GRAU 2";
      }
      else if (resultadoImc >= 30)
      {
         classificacao = "OBESIDADE GRAU 1";
      }
      else if (resultadoImc >= 25)
      {
         classificacao = "ACIMA DO PESO";
      }
      else if (resultadoImc >= 18.5)
      {
         classificacao = "PESO NORMAL";
      }
      else if (resultadoImc >= 17)
      {
         classificacao = "ABAIXO DO PESO";
      }
      else
      {
         classificacao = "MUITO ABAIXO DO PESO";
      }
      try
      {
         double resultadoImcDouble = Double.parseDouble(resultadoImcFormatado.replace(",", "."));
         this.imc.setResultadoImc(resultadoImcDouble);
      }
      catch (NumberFormatException e)
      {
         System.err.println("Erro ao converter o resultado do IMC para Double: " + e.getMessage());
         this.imc.setResultadoImc(0.0);
      }

      this.imc.setClassificacao(classificacao);
   }

   public void navegarParaPesquisar()
   {
      this.imc = new IMC();
      this.imc.setPaciente(new Paciente());
      navegacaoBean.setCurrentPage("imc-pesquisar.xhtml");
   }

   private boolean validarInserir()
   {
      if (Util.isCampoNullOrVazio(imc.getPaciente().getNome()))
      {
         Util.addMensagemErro("Nome paciente obrigatório.");
         return false;
      }

      if (Objects.isNull(imc.getPesoPaciente()))
      {
         Util.addMensagemErro("Peso do paciente obrigatória.");
         return false;
      }

      if (Objects.isNull(imc.getAlturaPaciente()))
      {
         Util.addMensagemErro("Altura do paciente obrigatória.");
         return false;
      }

      return true;
   }

   public IMC getImc()
   {
      return imc;
   }

   public void setImc(IMC imc)
   {
      this.imc = imc;
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
      this.pacientesCadastrados = PacienteDAO.pesquisar();
      return this.pacientesCadastrados;
   }

   public void setPacientesCadastrados(List<Paciente> pacientesCadastrados)
   {
      this.pacientesCadastrados = pacientesCadastrados;
   }

   public List<IMC> getImcs()
   {
      if (exibirResultadosPesquisa)
      {
         return this.listaResultado;
      }
      else
      {
         if (imcs != null)
         {
            imcs = ImcDAO.pesquisar();
         }
         return imcs;
      }
   }

   public void setImcs(List<IMC> imcs)
   {
      this.imcs = imcs;
   }

   public List<IMC> getListaResultado()
   {
      return listaResultado;
   }

   public void setListaResultado(List<IMC> listaResultado)
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
