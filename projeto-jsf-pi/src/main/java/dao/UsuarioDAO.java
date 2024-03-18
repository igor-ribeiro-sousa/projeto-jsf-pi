package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import entidade.Usuario;
import exception.JSFException;
import util.JPAUtilService;

public class UsuarioDAO
{

   public static List<Usuario> pesquisar()
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("select j from Usuario j");
         List<Usuario> resutado = query.getResultList();
         return resutado;

      }
      catch (Exception e)
      {
         throw new JSFException("Erro ao pesquisar usuários: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   public static boolean existeUsuarioPorEmail(String email)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
         query.setParameter("email", email);

         List<Usuario> resultados = query.getResultList();
         return !resultados.isEmpty();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
      finally
      {
         if (entityManager != null && entityManager.isOpen())
         {
            entityManager.close();
         }
      }
   }

   public static String pesquisarUsuarioPorEmail(String email)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      String nomeUsuariosuarioEncontrado = null;

      try
      {
         Query query = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
         query.setParameter("email", email);

         List<Usuario> resultados = query.getResultList();
         if (!resultados.isEmpty())
         {
            nomeUsuariosuarioEncontrado = resultados.get(0).getNome();
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         if (entityManager != null && entityManager.isOpen())
         {
            entityManager.close();
         }
      }

      return nomeUsuariosuarioEncontrado;
   }

   public static List<Usuario> pesquisarPorUsuario(String nomeUsuario)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT u FROM Usuario u WHERE " + "(u.nome LIKE :nomeUsuario)");

         query.setParameter("nomeUsuario", "%" + nomeUsuario + "%");

         List<Usuario> resultado = query.getResultList();
         return resultado;
      }
      catch (Exception e)
      {
         return null;
      }
      finally
      {
         if (entityManager != null && entityManager.isOpen())
         {
            entityManager.close();
         }
      }
   }

   public static Usuario getUsuario(Integer id)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         return entityManager.find(Usuario.class, id);

      }
      catch (Exception e)
      {
         throw new JSFException("Erro ao obter a usuário: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   public static void inserir(Usuario usuario)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         inserir(usuario, entityManager);
         transaction.commit();

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao salvar a usuário: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static void inserir(Usuario usuairo, EntityManager entityManager)
   {
      entityManager.persist(usuairo);
   }

   public static void alterar(Usuario usuairo)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         alterar(usuairo, entityManager);
         transaction.commit();

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao alterar a usuario: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static void alterar(Usuario usuario, EntityManager entityManager)
   {
      entityManager.merge(usuario);
   }

   public static boolean logar(String email, String senha)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      try
      {
         Query query = entityManager
               .createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha AND u.flagAtivo = 'S'");
         query.setParameter("email", email);
         query.setParameter("senha", senha);

         List<Usuario> resultList = query.getResultList();
         return !resultList.isEmpty();
      }
      catch (NoResultException e)
      {
         return false;
      }
      finally
      {
         if (entityManager != null && entityManager.isOpen())
         {
            entityManager.close();
         }
      }
   }

   public static void excluir(Integer id)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         Usuario usuario = entityManager.find(Usuario.class, id);
         if (usuario != null)
         {
            excluir(usuario, entityManager);
         }
         transaction.commit();

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao excluir a usuario: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static void excluir(Usuario usuario, EntityManager entityManager)
   {
      entityManager.remove(usuario);
   }
}
