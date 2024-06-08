package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Agendamento;
import entidade.Medico;
import entidade.Usuario;
import enuns.StatusAgendamento;
import exception.JSFException;
import util.JPAUtilService;

public class MedicoDAO
{

   public static List<Medico> pesquisar()
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT mdc FROM Medico mdc");
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
         Query query = entityManager.createQuery("SELECT mdc FROM Medico mdc WHERE mdc.nome LIKE :nomeMedico");
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
   
   public static boolean existeMedicoPorCrm(String crm)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT mdc FROM Medico mdc WHERE mdc.crm = :crm");
         query.setParameter("crm", crm);

         List<Agendamento> resultados = query.getResultList();
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

   public static boolean possuiAgendamentosAgendados(Integer idMedico)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      try
      {
         Query query = entityManager.createQuery("SELECT COUNT(agd) FROM Agendamento agd WHERE agd.medico.id = :idMedico AND agd.status = :statusAgendado");
         query.setParameter("idMedico", idMedico);
         query.setParameter("statusAgendado", StatusAgendamento.AGENDADO);
         Long count = (Long) query.getSingleResult();
         return count > 0;
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
