package config;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dao.UsuarioDAO;
import entidade.Usuario;
import util.Util;

public class Inicializacao implements ServletContextListener {

   @Override
   public void contextInitialized(ServletContextEvent sce) {
       System.out.println("Tomcat está iniciando... Executando script SQL...");
       try {
           Usuario usuario = new Usuario();
           usuario.setNome("SEU ZEZINHO");
           usuario.setEmail("ADMIN@MAIL.COM");
           usuario.setSenha(Util.gerarHashSenha("123"));
           usuario.setDataInclusao(new Date());
           usuario.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse("17/08/1996"));
           usuario.setFlagAtivo("S");

           UsuarioDAO.inserir(usuario);

           System.out.println("Script SQL executado com sucesso.");

       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   @Override
   public void contextDestroyed(ServletContextEvent sce) {
   }
}