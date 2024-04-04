package groupone.daos;

import groupone.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class DAO<T, K> implements IDAO<T, K> {

    Class<T> entityClass;
    public static EntityManagerFactory emf;

    public DAO(Class<T> tClass, boolean isTesting){
        if(isTesting){
            emf = HibernateConfig.getEntityManagerFactoryConfigForTesting();
        }else {
            emf = HibernateConfig.getEntityManagerFactoryConfig();
        }
        entityClass = tClass;
    }

    public List<T> getAll(){
        try(var em = emf.createEntityManager()){
            TypedQuery<T> query = em.createQuery("select h from "+entityClass.getSimpleName()+" h", entityClass);
            return query.getResultList();
        }
    }

    public T getById(K in){
        try(var em = emf.createEntityManager()){
            return em.find(entityClass, in);
        }
    }

    public T getById(K in, List<Consumer<T>> initializeList){
        try(var em = emf.createEntityManager()){
            T result = em.find(entityClass, in);
            for(Consumer<T> initialize: initializeList){
                initialize.accept(result);
            }
            return result;
        }
    }

    public void create(T in){
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(in);
            em.getTransaction().commit();
        }
    }

    public T update(T in, K id){
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            T found = em.find(entityClass, id);
            if(found != null){
                T merged = em.merge(in);
                em.getTransaction().commit();
                return merged;
            }
        }
        return null;
    }

    public T delete(K id){
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            T found = em.find(entityClass, id);
            if(found != null){
                em.remove(found);
                em.getTransaction().commit();
                return found;
            }
        }
        return null;
    }


}
