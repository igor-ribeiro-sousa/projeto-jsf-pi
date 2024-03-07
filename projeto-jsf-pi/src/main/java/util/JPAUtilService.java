package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtilService
{

   private static EntityManagerFactory entityManager = Persistence.createEntityManagerFactory("jsf-pi");

   public static EntityManager fabricarEntityManager()
   {
      return entityManager.createEntityManager();
   }
}
