package config;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dao.MedicoDAO;
import dao.UsuarioDAO;
import entidade.Medico;
import entidade.Usuario;
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
         
         UsuarioDAO.inserir(setUsuario(null, "SEU ZEZINHO", "IGORRIBEIROCONS@GMAIL.COM", "123", new Date(),
               new SimpleDateFormat("dd/MM/yyyy").parse("17/08/1996"), "S"));
         
         System.out.println("Usuário inserido com sucesso.");

         System.out.println("Inserindo médicoS...");
         
         MedicoDAO.inserir(setMedico(null, "DR RITA DE CASSIA", "S", new Date()));
         MedicoDAO.inserir(setMedico(null, "DR CARLOS EDUARDO", "S", new Date()));
         MedicoDAO.inserir(setMedico(null, "DR IRANILDO MESQUITA", "S", new Date()));
         
         System.out.println("Médico inserido com sucesso.");

         System.out.println("Script SQL executado com sucesso.");

      }
      catch (Exception e)
      {
         System.out.println("Script SQL não executado.");
         e.printStackTrace();
      }
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

   @Override
   public void contextDestroyed(ServletContextEvent arg0)
   {
      // TODO Auto-generated method stub
   }
}