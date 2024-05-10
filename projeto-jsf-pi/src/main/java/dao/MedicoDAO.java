package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Medico;
import entidade.Usuario;
import exception.JSFException;
import util.JPAUtilService;

public class MedicoDAO
{

   public static List<Medico> pesquisar()
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("select j from Medico j");
         List<Medico> resutado = query.getResultList();
         return resutado;

      }
      catch (Exception e)
      {
         throw new JSFException("Erro ao pesquisar médicos: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   public static List<Medico> pesquisarPorMedico(String nomeMedico)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT u FROM Medico u WHERE " + "(u.nome LIKE :nomeMedico)");

         query.setParameter("nomeMedico", "%" + nomeMedico + "%");

         List<Medico> resultado = query.getResultList();
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

   public static Medico inserir(Medico medico)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         medico = inserir(medico, entityManager);
         transaction.commit();
         return medico;

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao salvar a médico: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static Medico inserir(Medico medico, EntityManager entityManager)
   {
      entityManager.persist(medico);
      return medico;
   }

   public static Medico alterar(Medico medico)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         medico = alterar(medico, entityManager);
         transaction.commit();
         return medico;
      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao alterar o médico: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null && entityManager.isOpen())
         {
            entityManager.close();
         }
      }
   }

   private static Medico alterar(Medico medico, EntityManager entityManager)
   {
      return entityManager.merge(medico);
   }


   public static void excluir(Integer id)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         Medico medico = entityManager.find(Medico.class, id);
         if (medico != null)
         {
            medico = entityManager.merge(medico);
            excluir(medico, entityManager);
         }
         transaction.commit();

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao excluir a médico: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static void excluir(Medico medico, EntityManager entityManager)
   {
      entityManager.remove(medico);
   }
}
