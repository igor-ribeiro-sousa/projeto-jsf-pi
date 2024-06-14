package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Consulta;
import entidade.Paciente;
import exception.JSFException;
import util.JPAUtilService;

public class ConsultaDAO
{

   public static List<Consulta> pesquisar()
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT cst FROM Consulta cst");
         List<Consulta> resutado = query.getResultList();
         return resutado;

      }
      catch (Exception e)
      {
         throw new JSFException("Erro ao pesquisar consulta: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }
   
   public static List<Consulta> pesquisarPorPaciente(String nome)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT cst FROM Consulta cst JOIN cst.paciente pct WHERE pct.nome LIKE :nome");
         query.setParameter("nome", "%" + nome + "%");

         List<Consulta> resultado = query.getResultList();
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

   public static Consulta inserir(Consulta consulta)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         consulta = inserir(consulta, entityManager);
         transaction.commit();
         return consulta;

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao realizar consulta: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static Consulta inserir(Consulta consulta, EntityManager entityManager)
   {
      entityManager.persist(consulta);
      return consulta;
   }


   public static Consulta alterar(Consulta consulta)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         consulta = alterar(consulta, entityManager);
         transaction.commit();
         return consulta;
      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao alterar consulta: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null && entityManager.isOpen())
         {
            entityManager.close();
         }
      }
   }

   private static Consulta alterar(Consulta consulta, EntityManager entityManager)
   {
      return entityManager.merge(consulta);
   }

   public static void excluir(Integer id)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         Consulta consulta = entityManager.find(Consulta.class, id);
         if (consulta != null)
         {
            consulta = entityManager.merge(consulta);
            excluir(consulta, entityManager);
         }
         transaction.commit();

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao excluir consulta: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static void excluir(Consulta consulta, EntityManager entityManager)
   {
      entityManager.remove(consulta);
   }
}
