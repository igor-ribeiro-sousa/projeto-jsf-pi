package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Paciente;
import enuns.StatusAgendamento;
import exception.JSFException;
import util.JPAUtilService;

public class PacienteDAO
{

   public static List<Paciente> pesquisar()
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT pct FROM Paciente pct");
         List<Paciente> resutado = query.getResultList();
         return resutado;

      }
      catch (Exception e)
      {
         throw new JSFException("Erro ao pesquisar paciente: " + e.getMessage());

      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   public static List<Paciente> pesquisarPorPaciente(String nome)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT pct FROM Paciente pct WHERE nome LIKE :nome");
         query.setParameter("nome", "%" + nome + "%");

         List<Paciente> resultado = query.getResultList();
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

   public static Paciente pesquisarPorCpfPaciente(String cpf)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT pct FROM Paciente pct WHERE pct.cpf = :cpf");
         query.setParameter("cpf", cpf);

         Paciente resultado = (Paciente) query.getSingleResult();
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
   
   public static Paciente inserir(Paciente paciente)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         paciente = inserir(paciente, entityManager);
         transaction.commit();
         return paciente;

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao salvar a paciente: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static Paciente inserir(Paciente paciente, EntityManager entityManager)
   {
      entityManager.persist(paciente);
      return paciente;
   }
   
   public static boolean possuiAgendamentosAgendados(Integer idPaciente)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      try
      {
         Query query = entityManager.createQuery("SELECT COUNT(agd) FROM Agendamento agd WHERE agd.paciente.id = :idPaciente AND agd.status = :statusAgendado");
         query.setParameter("idPaciente", idPaciente);
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

   public static boolean existePacientePorCpf(String cpf)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT pct FROM Paciente pct WHERE pct.cpf = :cpf");
         query.setParameter("cpf", cpf);

         List<Paciente> resultados = query.getResultList();
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
   
   public static boolean existePacientePorEmail(String email)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();

      try
      {
         Query query = entityManager.createQuery("SELECT pct FROM Paciente pct WHERE pct.email = :email");
         query.setParameter("email", email);

         List<Paciente> resultados = query.getResultList();
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

   public static Paciente alterar(Paciente paciente)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         paciente = alterar(paciente, entityManager);
         transaction.commit();
         return paciente;
      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao alterar paciente: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null && entityManager.isOpen())
         {
            entityManager.close();
         }
      }
   }

   private static Paciente alterar(Paciente paciente, EntityManager entityManager)
   {
      return entityManager.merge(paciente);
   }

   public static void excluir(Integer id)
   {
      EntityManager entityManager = JPAUtilService.fabricarEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();

      try
      {
         transaction.begin();
         Paciente paciente = entityManager.find(Paciente.class, id);
         if (paciente != null)
         {
            paciente = entityManager.merge(paciente);
            excluir(paciente, entityManager);
         }
         transaction.commit();

      }
      catch (Exception e)
      {
         if (transaction != null && transaction.isActive())
         {
            transaction.rollback();
         }
         throw new JSFException("Erro ao excluir a paciente: " + e.getMessage());
      }
      finally
      {
         if (entityManager != null)
         {
            entityManager.close();
         }
      }
   }

   private static void excluir(Paciente paciente, EntityManager entityManager)
   {
      entityManager.remove(paciente);
   }
}
