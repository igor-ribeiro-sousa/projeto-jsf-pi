package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Agendamento;
import enuns.StatusAgendamento;
import exception.JSFException;
import util.JPAUtilService;

public class AgendamentoDAO
{

   public static List<Agendamento> pesquisar()
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT agd FROM Agendamento agd");
         List<Agendamento> resutado = query.getResultList();
         return resutado;

      }
      catch (Exception e)
      {
         throw new JSFException("Erro ao pesquisar agendamento: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   public static List<Agendamento> pesquisarPorMedico(String nomeMedico)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT agd FROM Agendamento agd WHERE agd.medico.nome LIKE :nomeMedico");
         query.setParameter("nomeMedico", "%" + nomeMedico + "%");

         List<Agendamento> resultado = query.getResultList();
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
   
   public static List<Agendamento> pesquisarAgendamentosAgendados(String cpf)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery(
               "SELECT agd FROM Agendamento agd " +
               "INNER JOIN Paciente pac ON pac.id = agd.codigoPaciente " +
               "WHERE agd.status = :status AND pac.cpf = :cpf"
           );
           query.setParameter("status", StatusAgendamento.AGENDADO);
           query.setParameter("cpf", cpf);
         
         List<Agendamento> resultado = query.getResultList();
         return resultado;
      
      }
      catch (Exception e)
      {
         throw new JSFException("Erro ao pesquisar agendamento: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
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
         Query query = entityManager.createQuery("SELECT agd FROM Agendamento agd WHERE agd.emailPaciente = :email");
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
               "SELECT COUNT(agd) FROM Agendamento agd INNER JOIN agd.medico mdc " +
               "WHERE agd.dataHoraAgendamento = :dataAgendamento " +
               "AND mdc.nome = :nomeMedico AND agd.status = :status");
         
         query.setParameter("dataAgendamento", dataAgendamento);
         query.setParameter("nomeMedico", nomeMedico);
         query.setParameter("status", StatusAgendamento.AGENDADO);
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
         throw new JSFException("Erro ao alterar agendamento: " + e.getMessage());
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
