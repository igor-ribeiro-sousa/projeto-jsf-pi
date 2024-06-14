package config;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dao.AgendamentoDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import dao.UsuarioDAO;
import entidade.Agendamento;
import entidade.Medico;
import entidade.Paciente;
import entidade.Usuario;
import enuns.Clinica;
import enuns.Sexo;
import enuns.StatusAgendamento;
import util.Util;

public class Inicializacao implements ServletContextListener
{

   @Override
   public void contextInitialized(ServletContextEvent sce)
   {
      System.out.println("Tomcat está iniciando... Executando script SQL...");
         try
         {
            System.out.println("Inserindo um usuário...");
               UsuarioDAO.inserir(setUsuario(
                     null, "SEU ZEZINHO", 
                     "IGORRIBEIROCONS@GMAIL.COM", 
                     "123", 
                     new Date(),
                     new SimpleDateFormat("dd/MM/yyyy").parse("17/08/1996"), 
                     "S"));
            System.out.println("Usuário inserido com sucesso.");
   
            
            System.out.println("Inserindo médicos...");
               Medico medico1 = MedicoDAO.inserir(setMedico(
                     null, 
                     "DR RITA DE CASSIA",
                     "12dsf3",
                     "S", 
                     new Date()));
               
               Medico medico2 = MedicoDAO.inserir(setMedico(
                     null, 
                     "DR CARLOS EDUARDO",
                     "1fgdf23",
                     "S", 
                     new Date()));
               
               Medico medico3 = MedicoDAO.inserir(setMedico(
                     null, 
                     "DR EDUARDA COSTA",
                     "1644",
                     "S", 
                     new Date()));

               Medico medico4 = MedicoDAO.inserir(setMedico(
                     null, 
                     "DR ZEZE",
                     "123",
                     "S", 
                     new Date()));
            System.out.println("Médicos inseridos com sucesso.");
            
            System.out.println("Inserindo pacientes...");
            Paciente paciente1 = PacienteDAO.inserir(setPaciente(
                  null, 
                  "078.158.630-51", 
                  "IGOR RIBEIRO", 
                  "IGORRIBEIROCONS@GMAIL.COM",
                  Sexo.MASCULINO,
                  "S",
                  new SimpleDateFormat("dd/MM/yyyy").parse("17/08/1996"), 
                  new Date()));
            
            Paciente paciente2 = PacienteDAO.inserir(setPaciente(
                  null, 
                  "319.647.550-69", 
                  "PETRUS CARLOS", 
                  "PETRUS@GMAIL.COM",
                  Sexo.MASCULINO,
                  "S",
                  new SimpleDateFormat("dd/MM/yyyy").parse("21/02/1999"), 
                  new Date()));
            
            Paciente paciente3 = PacienteDAO.inserir(setPaciente(
                  null, 
                  "651.692.910-60", 
                  "CLAUDIO MARTINS", 
                  "CLAUDIO@GMAIL.COM",
                  Sexo.MASCULINO,
                  "S",
                  new SimpleDateFormat("dd/MM/yyyy").parse("21/02/1999"), 
                  new Date()));
            
            Paciente paciente4 = PacienteDAO.inserir(setPaciente(
                  null, 
                  "598.458.950-85", 
                  "FERNANDA LIMA", 
                  "FER@GMAIL.COM",
                  Sexo.FEMININO,
                  "S",
                  new SimpleDateFormat("dd/MM/yyyy").parse("21/02/1999"), 
                  new Date()));
            
            
            System.out.println("Pacientes inseridos com sucesso.");
            
            
            System.out.println("Inserindo agendamentos...");
            AgendamentoDAO.inserir(setAgendamento(
                  null, 
                  paciente1.getId(), 
                  medico1.getId(), 
                  StatusAgendamento.AGENDADO, 
                  Clinica.HOSPITAL_OTOCLINICA, 
                  new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("20/07/2024 15:00"), 
                  new Date()));
            
            AgendamentoDAO.inserir(setAgendamento(
                  null, 
                  paciente2.getId(), 
                  medico3.getId(), 
                  StatusAgendamento.AGENDADO, 
                  Clinica.HOSPITAL_OTOCLINICA, 
                  new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("22/07/2024 10:00"), 
                  new Date()));
            
            AgendamentoDAO.inserir(setAgendamento(
                  null, 
                  paciente3.getId(), 
                  medico2.getId(), 
                  StatusAgendamento.CANCELADO, 
                  Clinica.HOSPITAL_SAO_MATEUS, 
                  new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("14/05/2024 13:00"), 
                  new Date()));
            
            AgendamentoDAO.inserir(setAgendamento(
                  null, 
                  paciente4.getId(), 
                  medico2.getId(), 
                  StatusAgendamento.REALIZADO, 
                  Clinica.HOSPITAL_SAO_RAIMUNDO, 
                  new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("10/05/2024 14:00"),
                  new Date()));
            System.out.println("Agendamento inseridos com sucesso.");
         
         System.out.println("Script SQL executado com sucesso.");

         }
         catch (Exception e)
         {
            System.out.println("Script SQL não executado.");
            e.printStackTrace();
         }
   }

   private Usuario setUsuario(Integer id, String nome, String email, String senha, Date dataInclusao, Date dataNascimento, String flagAtivo)
   {
      Usuario usuario = new Usuario();
      usuario.setId(id);
      usuario.setNome(nome);
      usuario.setEmail(email);
      usuario.setSenha(Util.gerarHashSenha(senha));
      usuario.setDataInclusao(dataInclusao);
      usuario.setDataNascimento(dataNascimento);
      usuario.setFlagAtivo(flagAtivo);
      return usuario;
   }

   private Medico setMedico(Integer id, String nome,String crm, String flagAtivo, Date dataInclusao)
   {
      Medico medico = new Medico();
      medico.setId(id);
      medico.setNome(nome);
      medico.setCrm(crm);
      medico.setFlagAtivo(flagAtivo);
      medico.setDataInclusao(dataInclusao);
      return medico;
   }
   
   private Paciente setPaciente(Integer id, String cpf, String nome, String email, Sexo sexo, String flagAtivo, Date dataNascimento, Date dataInclusao)
   {
      Paciente paciente = new Paciente();
      paciente.setId(id);
      paciente.setCpf(cpf);
      paciente.setNome(nome);
      paciente.setEmail(email);
      paciente.setSexo(sexo);
      paciente.setFlagAtivo(flagAtivo);
      paciente.setDataNascimento(dataNascimento);
      paciente.setDataInclusao(dataInclusao);
      return paciente;
   }

   private Agendamento setAgendamento(Integer id, Integer codPaciente, Integer codMedico, 
         StatusAgendamento status, Clinica clinica, Date dataHoraAgendamento, Date dataInclusao) 
   {
      Agendamento agendamento = new Agendamento();
      agendamento.setId(id);
      agendamento.setCodigoPaciente(codPaciente);;
      agendamento.setCodigoMedico(codMedico);
      agendamento.setStatus(status);
      agendamento.setClinica(clinica);
      agendamento.setDataHoraAgendamento(dataHoraAgendamento);
      agendamento.setDataInclusao(dataInclusao);
      return agendamento;
   }

   @Override
   public void contextDestroyed(ServletContextEvent arg0)
   {
      // TODO Auto-generated method stub
   }
}