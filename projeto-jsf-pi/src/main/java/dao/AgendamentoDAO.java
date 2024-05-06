package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Agendamento;
import entidade.Medico;
import exception.JSFException;
import util.JPAUtilService;

public class AgendamentoDAO
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

   public static void inserir(Agendamento agendamento)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         inserir(agendamento, entityManager);
         transaction.commit();

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

   private static void inserir(Agendamento agendamento, EntityManager entityManager)
   {
      entityManager.persist(agendamento);
   }

   public static boolean existePacientePorEmail(String email)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT u FROM Agendamento u WHERE u.emailPaciente = :email");
         query.setParameter("email", email);

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

   public static boolean existeAgendamentoNaMesmaData(Date dataAgendamento, String nomeMedico)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery(
               "SELECT COUNT(a) FROM Agendamento a INNER JOIN a.medico m WHERE a.dataHoraAgendamento = :dataAgendamento AND m.nome = :nomeMedico");
         query.setParameter("dataAgendamento", dataAgendamento);
         query.setParameter("nomeMedico", nomeMedico);
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

   public static Agendamento alterar(Agendamento agendamento)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         agendamento = alterar(agendamento, entityManager);
         transaction.commit();
         return agendamento;
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

   private static Agendamento alterar(Agendamento agendamento, EntityManager entityManager)
   {
      return entityManager.merge(agendamento);
   }

   public static void excluir(Integer id)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         Agendamento agendamento = entityManager.find(Agendamento.class, id);
         if (agendamento != null)
         {
            agendamento = entityManager.merge(agendamento);
            excluir(agendamento, entityManager);
         }
         transaction.commit();

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao excluir a agendamento: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static void excluir(Agendamento agendamento, EntityManager entityManager)
   {
      entityManager.remove(agendamento);
   }
}
