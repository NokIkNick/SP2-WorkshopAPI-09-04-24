package groupone.daos;

import groupone.exceptions.ValidationException;
import groupone.model.Role;
import groupone.model.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class UserDAO extends DAO<User, String> {

    private static UserDAO instance;

    private UserDAO(boolean isTesting) {
        super(User.class, isTesting);
    }

    public static UserDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new UserDAO(isTesting);
        }
        return instance;
    }

    public User createUser(String username, String password, String name, Integer phonenumber){
        User user = new User();
        user.setEmail(username);
        user.setPassword(password);
        user.setName(name);
        user.setPhoneNumber(phonenumber);
        try(var em = emf.createEntityManager()){
            Role role = em.find(Role.class, "STUDENT");
            user.addRole(role);
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
        return user;
    }


    public User getVerifiedUser(String username, String password) throws ValidationException {
        try(var em = emf.createEntityManager()){
            List<User> users = em.createQuery("select u from users u", User.class).getResultList();
            users.stream().forEach(user -> System.out.println(user.getEmail()+" "+user.getPassword()));
            User user = em.find(User.class, username);
            if(user == null){
                throw new EntityNotFoundException("No user found with username: "+username);
            }
            user.getRoles().size();
            if(!user.verifyPassword(password)){
                throw new ValidationException("Error while logging in, invalid credentials");
            }
            return user;
        }
    }

}
