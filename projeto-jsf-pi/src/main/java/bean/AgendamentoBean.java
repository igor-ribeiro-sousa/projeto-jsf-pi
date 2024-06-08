package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dao.AgendamentoDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import entidade.Agendamento;
import entidade.Medico;
import entidade.Paciente;
import enuns.Clinica;
import enuns.StatusAgendamento;
import exception.JSFException;
import util.Util;

@ManagedBean
@SessionScoped
public class AgendamentoBean
{
   private Agendamento agendamento;
   private String nomeMedicoPesquisa;
   private Date dataAgendamentoAtual;
   private List<Paciente> pacientesCadastrados;
   private List<Medico> medicosCadastrados;
   private List<Agendamento> agendamentos;
   private List<Agendamento> listaResultado;
   private boolean exibirResultadosPesquisa;
   
   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;

   public AgendamentoBean()
   {
      this.agendamento = new Agendamento();
      this.agendamento.setMedico(new Medico());
      this.agendamento.setPaciente(new Paciente());
      this.agendamentos = new ArrayList<Agendamento>();
      this.listaResultado = new ArrayList<Agendamento>();
      this.exibirResultadosPesquisa = false;
   }

   public String agendar()
   {
      try
      {
         this.agendamento.setPaciente(PacienteDAO.pesquisarPorCpfPaciente(this.agendamento.getPaciente().getCpf()));
         if (validarInserir())
         {
            completarInserir();
            AgendamentoDAO.inserir(agendamento);
            Util.addMensagemInfo("Agendamento realizado com sucesso!. Número do agendamento: " + this.agendamento.getId());
            atualizaAgendamento();
            return "index";
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar agendar o exame. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar agendar o exame.", e);
      }
      return null;
   }
   
   public void agendarLogado()
   {
      try {
         this.agendamento.setPaciente(PacienteDAO.pesquisarPorCpfPaciente(this.agendamento.getPaciente().getCpf()));
         if (validarInserir())
         {
            completarInserir();
            AgendamentoDAO.inserir(agendamento);
            Util.addMensagemInfo("Agendamento realizado com sucesso!. Número do agendamento: " + this.agendamento.getId());
            atualizaAgendamento();
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar agendar o exame. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar agendar o exame.", e);
      }
   }
   
   public void completarInserir()
   {
      completarPaciente();
      completarMedico();
      this.agendamento.setDataInclusao(new Date());
      this.agendamento.setStatus(StatusAgendamento.AGENDADO);
   }
   
   public void alterar()
   {
      try
      {
         if (validarAlterar())
         {
            completarAlterar();
            this.agendamento = AgendamentoDAO.alterar(agendamento);
            Util.addMensagemInfo("Agendamento alterado com sucesso!");
            navegarParaPesquisar();
         }

      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar agendar consulta. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar agendar consulta.", e);
      }
   }

   public void completarAlterar()
   {
      this.agendamento.setStatus(StatusAgendamento.AGENDADO);
      completarPaciente();
      completarMedico();
   }
   
   private void completarPaciente()
   {
      Paciente pacientes = PacienteDAO.pesquisarPorCpfPaciente(this.agendamento.getPaciente().getCpf());
      if (Objects.nonNull(pacientes))
      {
         this.agendamento.setCodigoPaciente(pacientes.getId());
         this.agendamento.setPaciente(null);
      }
   }
   
   private void completarMedico() 
   {
      List<Medico> medicos = MedicoDAO.pesquisarPorMedico(this.agendamento.getMedico().getNome().toUpperCase().trim());
      if (!medicos.isEmpty()) 
      {
         this.agendamento.setCodigoMedico(medicos.get(0).getId());
         this.agendamento.setMedico(null);
      }
   }
   
   public void pesquisarPorNome()
   {
      try
      {
         if (!Util.isCampoNullOrVazio(nomeMedicoPesquisa))
         {
            this.listaResultado = AgendamentoDAO.pesquisarPorMedico(this.nomeMedicoPesquisa.toUpperCase().trim());
            this.exibirResultadosPesquisa = true;
         }
         else
         {
            this.listaResultado = AgendamentoDAO.pesquisar();
            this.exibirResultadosPesquisa = false;
         }

      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao pesquisar!. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao pesquisar!.", e);      
      }
   }

   public boolean existePacientePorEmail(String email)
   {
      try
      {
         return AgendamentoDAO.existePacientePorEmail(email.toUpperCase().trim());
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao verificar a existência do paciente. Por favor, tente novamente mais tarde.");
         return false;
      }
   }
   
   private boolean verificarDisponibilidadeAgendamento(Date dataHoraAgendamento)
   {
      try 
      {
         return AgendamentoDAO.existeAgendamentoNaMesmaData(dataHoraAgendamento, this.agendamento.getMedico().getNome());
      }
      catch (JSFException e)
      {
         Util.addMensagemErro("Erro ao verificar a disponibilidade do agendamento. Por favor, tente novamente mais tarde.");
         return false;
      }
   }
   
   public void editar(Agendamento agendamento)
   {
      try
      {
         this.agendamento = agendamento;
         this.dataAgendamentoAtual = agendamento.getDataHoraAgendamento();
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
         AgendamentoDAO.excluir(id);
         Util.addMensagemWarn("Agendamento excluído !");
      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar exluir agendamento. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar exluir agendamento", e);        
      }
   }
   
   public void cancelar(Agendamento agendamento)
   {
      try
      {
         agendamento.setStatus(StatusAgendamento.CANCELADO);
         AgendamentoDAO.alterar(agendamento);
         new Thread(() -> enviarEmailCancelamento(agendamento)).start();
         Util.addMensagemWarn("Agendamento cancelado com sucesso!");
      }
      catch (Exception e)
      {
         Util.addMensagemErro("Erro ao tentar cancelar agendamento. Por favor, tente novamente mais tarde.");
         throw new JSFException("Erro ao tentar cancelar agendamento", e);        
      }
   }
   
   private boolean enviarEmailCancelamento(Agendamento agendamento)
   {
      final String remetente = "antonio.sousa09@aluno.unifametro.edu.br";
      final String senhaRemetente = "wwgu vpaf uvre fnrq";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");

      Session session = Session.getInstance(props, new javax.mail.Authenticator()
      {
         protected javax.mail.PasswordAuthentication getPasswordAuthentication()
         {
            return new javax.mail.PasswordAuthentication(remetente, senhaRemetente);
         }
      });
      
      try
      {
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(remetente));
         message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(agendamento.getPaciente().getEmail()));
         message.setSubject("Cancelamento de agendamento");
         String corpoEmail = "OLÁ SR./SRA. " + agendamento.getPaciente().getNome() + ",\n\n"
               + "SEU AGENDAMENTO DE NÚMERO " + agendamento.getId() + "\n"
               + "NA CLÍNICA: " + agendamento.getClinica() + "\n"
               + "PARA O DIA " + new SimpleDateFormat("dd/MM/yyyy").format(agendamento.getDataHoraAgendamento()) + "\n"
               + "ÀS " + new SimpleDateFormat("HH:mm").format(agendamento.getDataHoraAgendamento()) + " HORAS\n"
               + "FOI CANCELADO.";

         message.setText(corpoEmail);

         Transport.send(message);
         System.out.println("Email enviado com sucesso!");
         return true;
      }
      catch (MessagingException e)
      {
         e.printStackTrace();
         System.err.println("Erro ao enviar o email: " + e.getMessage());
         return false;
      }
   }
   
   public void navegarParaPesquisar()
   {
      atualizaAgendamento();
      navegacaoBean.setCurrentPage("agendamento-pesquisar.xhtml");
   }
   
   private void atualizaAgendamento()
   {
      this.agendamento = new Agendamento();
      this.agendamento.setMedico(new Medico());   
      this.agendamento.setPaciente(new Paciente());
   }
   
   public void navegarParaAlterar()
   {
      navegacaoBean.setCurrentPage("agendamento-alterar.xhtml");
   }
   
   public List<SelectItem> getClinicas()
   {
      List<SelectItem> clinica = new ArrayList<>();
      for (Clinica item : Clinica.values())
      {
         String label = item.name().replace("_", " ");
         clinica.add(new SelectItem(item, label));
      }
      return clinica;
   }
   
   private boolean validarInserir()
   {
      if (Util.isCampoNullOrVazio(agendamento.getPaciente().getNome()))
      {
         Util.addMensagemErro("Nome do paciente obrigatório.");
         return false;
      }

      if (Util.isCampoNullOrVazio(agendamento.getPaciente().getEmail()))
      {
         Util.addMensagemErro("E-mail obrigatório.");
         return false;
      }

      if (!Util.validarEmail(agendamento.getPaciente().getEmail().trim()))
      {
         Util.addMensagemErro("E-mail inválido.");
         return false;
      }

      if (Objects.isNull(agendamento.getClinica()))
      {
         Util.addMensagemErro("Nome da clínica obrigatório.");
         return false;
      }
      
      if (Objects.isNull(agendamento.getMedico().getNome()))
      {
         Util.addMensagemErro("Nome do médico obrigatório.");
         return false;
      }
      
      if (Objects.isNull(agendamento.getDataHoraAgendamento()))
      {
         Util.addMensagemErro("Data do agendamento obrigatória.");
         return false;
      }
      
      if (verificarDisponibilidadeAgendamento(agendamento.getDataHoraAgendamento())) 
      {
         Util.addMensagemErro("Data do agendamento indisponível.");
         return false;
      }

      if (agendamento.getDataHoraAgendamento().before(new Date()))
      {
         Util.addMensagemErro("Data do agendamento não pode ser no passado.");
         return false;
      }

      return true;
   }
   
   private boolean validarAlterar()
   {
      if (Util.isCampoNullOrVazio(agendamento.getPaciente().getEmail()))
      {
         Util.addMensagemErro("Nome do paciente obrigatório.");
         return false;
      }

      if (Util.isCampoNullOrVazio(agendamento.getPaciente().getEmail()))
      {
         Util.addMensagemErro("E-mail obrigatório.");
         return false;
      }

      if (!Util.validarEmail(agendamento.getPaciente().getEmail().trim()))
      {
         Util.addMensagemErro("E-mail inválido.");
         return false;
      }

      if (Objects.isNull(agendamento.getClinica()))
      {
         Util.addMensagemErro("Nome da clínica obrigatório.");
         return false;
      }
      
      if (Objects.isNull(agendamento.getMedico().getNome()))
      {
         Util.addMensagemErro("Nome do médico obrigatório.");
         return false;
      }
      
      if (Objects.isNull(agendamento.getDataHoraAgendamento()))
      {
         Util.addMensagemErro("Data do agendamento obrigatória.");
         return false;
      }
      
      if (!agendamento.getDataHoraAgendamento().equals(this.dataAgendamentoAtual))
      {
         if (verificarDisponibilidadeAgendamento(agendamento.getDataHoraAgendamento())) 
         {
            Util.addMensagemErro("Data do agendamento indisponível.");
            return false;
         }
      }

      if (agendamento.getDataHoraAgendamento().before(new Date()))
      {
         Util.addMensagemErro("Data do agendamento não pode ser no passado.");
         return false;
      }

      return true;
   }
   
   public Agendamento getAgendamento()
   {
      return agendamento;
   }

   public void setAgendamento(Agendamento agendamento)
   {
      this.agendamento = agendamento;
   }
   
   public String getNomeMedicoPesquisa()
   {
      return nomeMedicoPesquisa;
   }
   
   public void setNomeMedicoPesquisa(String nomeMedicoPesquisa)
   {
      this.nomeMedicoPesquisa = nomeMedicoPesquisa;
   }
   
   public List<Paciente> getPacientesCadastrados()
   {
      pacientesCadastrados = PacienteDAO.pesquisar();
      return pacientesCadastrados;
   }

   public List<Medico> getMedicosCadastrados()
   {
      medicosCadastrados = MedicoDAO.pesquisar();
      return medicosCadastrados;
   }

   public List<Agendamento> getAgendamentos()
   {
      if (exibirResultadosPesquisa)
      {
         return this.listaResultado;
      }
      else
      {
         if (agendamentos != null)
         {
            agendamentos = AgendamentoDAO.pesquisar();
         }
         for (Agendamento item : agendamentos) 
         {
            if (item.getDataHoraAgendamento().before(new Date()) && item.getStatus().equals(StatusAgendamento.AGENDADO)) 
            {
               item.setStatus(StatusAgendamento.REALIZADO);
               AgendamentoDAO.alterar(item);
            }
         }
         return agendamentos;
      }
   }

   public void setAgendamentos(List<Agendamento> agendamentos)
   {
      this.agendamentos = agendamentos;
   }

   public List<Agendamento> getListaResultado()
   {
      return listaResultado;
   }

   public void setListaResultado(List<Agendamento> listaResultado)
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

   public Date getDataAgendamentoAtual()
   {
      return dataAgendamentoAtual;
   }

   public void setDataAgendamentoAtual(Date dataAgendamentoAtual)
   {
      this.dataAgendamentoAtual = dataAgendamentoAtual;
   }
}