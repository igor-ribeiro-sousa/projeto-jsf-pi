package config;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dao.AgendamentoDAO;
import dao.MedicoDAO;
import dao.UsuarioDAO;
import entidade.Agendamento;
import entidade.Medico;
import entidade.Usuario;
import enuns.Clinica;
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
                     "S", 
                     new Date()));
               
               Medico medico2 = MedicoDAO.inserir(setMedico(
                     null, 
                     "DR CARLOS EDUARDO", 
                     "S", 
                     new Date()));
            System.out.println("Médicos inseridos com sucesso.");
            
            
            System.out.println("Inserindo agendamentos...");
            AgendamentoDAO.inserir(setAgendamento(
                  null, 
                  "IGOR", 
                  medico1.getId(), 
                  "IGORRIBEIROSDEV@GMAIL.COM", 
                  StatusAgendamento.AGENDADO, 
                  Clinica.HOSPITAL_OTOCLINICA, 
                  new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("15/05/2024 15:00"), 
                  new Date()));
            
            AgendamentoDAO.inserir(setAgendamento(
                  null, 
                  "GERALDO", 
                  medico2.getId(), 
                  "GERALDO@GMAIL.COM", 
                  StatusAgendamento.CANCELADO, 
                  Clinica.HOSPITAL_SAO_MATEUS, 
                  new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("14/05/2024 13:00"), 
                  new Date()));
            
            AgendamentoDAO.inserir(setAgendamento(
                  null, 
                  "PETRUS", 
                  medico2.getId(), 
                  "PETRUS@GMAIL.COM", 
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

   private Medico setMedico(Integer id, String nome, String flagAtivo, Date dataInclusao)
   {
      Medico medico = new Medico();
      medico.setId(id);
      medico.setNome(nome);
      medico.setFlagAtivo(flagAtivo);
      medico.setDataInclusao(dataInclusao);
      return medico;
   }

   private Agendamento setAgendamento(Integer id, String nomePaciente, Integer codMedico, String emailPaciente, 
         StatusAgendamento status, Clinica clinica, Date dataHoraAgendamento, Date dataInclusao) 
   {
      Agendamento agendamento = new Agendamento();
      agendamento.setId(id);
      agendamento.setNomePaciente(nomePaciente);
      agendamento.setCodigoMedico(codMedico);
      agendamento.setEmailPaciente(emailPaciente);
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