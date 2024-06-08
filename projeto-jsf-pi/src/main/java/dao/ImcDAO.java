package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.IMC;
import entidade.Paciente;
import exception.JSFException;
import util.JPAUtilService;

public class ImcDAO
{

   public static List<IMC> pesquisar()
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT imc FROM IMC imc");
         List<IMC> resutado = query.getResultList();
         return resutado;

      }
      catch (Exception e)
      {
         throw new JSFException("Erro ao pesquisar IMC: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   public static List<IMC> pesquisarImcPorNomePaciente(String nome)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT imc FROM IMC imc INNER JOIN imc.paciente pct WHERE pct.nome LIKE :nome");
         query.setParameter("nome", "%" + nome + "%");

         List<IMC> resultado = query.getResultList();
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

   public static IMC inserir(IMC imc)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         imc = inserir(imc, entityManager);
         transaction.commit();
         return imc;

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao salvar imc: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static IMC inserir(IMC imc, EntityManager entityManager)
   {
      entityManager.persist(imc);
      return imc;
   }


   public static IMC alterar(IMC imc)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         imc = alterar(imc, entityManager);
         transaction.commit();
         return imc;
      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao alterar imc: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null && entityManager.isOpen())
         {
            entityManager.close();
         }
      }
   }

   private static IMC alterar(IMC imc, EntityManager entityManager)
   {
      return entityManager.merge(imc);
   }

   public static void excluir(Integer id)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         IMC imc = entityManager.find(IMC.class, id);
         if (imc != null)
         {
            imc = entityManager.merge(imc);
            excluir(imc, entityManager);
         }
         transaction.commit();

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao excluir IMC: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static void excluir(IMC imc, EntityManager entityManager)
   {
      entityManager.remove(imc);
   }
}
