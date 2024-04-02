package groupone.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.JOSEException;
import groupone.daos.UserDAO;
import groupone.dtos.TokenDTO;
import groupone.dtos.UserDTO;
import groupone.exceptions.ApiException;
import groupone.exceptions.NotAuthorizedException;
import groupone.exceptions.ValidationException;
import groupone.model.User;
import groupone.utils.TokenUtils;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.text.ParseException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class SecurityController {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static UserDAO userDAO;
    private static TokenUtils tokenUtils = new TokenUtils();
    private static SecurityController instance;

    public static SecurityController getInstance(boolean isTesting){
        if(instance == null){
            instance = new SecurityController();
            userDAO = userDAO.getInstance(isTesting);
        }
        return instance;
    }

    public Handler register() {
        return (ctx) -> {
            ObjectNode returnObject = objectMapper.createObjectNode();
            try{
                UserDTO userInput = ctx.bodyAsClass(UserDTO.class);
                User toPersist = new User(userInput);
                User created = userDAO.createUser(toPersist.getEmail(), toPersist.getPassword(), toPersist.getName(), toPersist.getPhoneNumber());


                String token = tokenUtils.createToken(new UserDTO(created));
                ctx.status(HttpStatus.CREATED).json(new TokenDTO(token, userInput.getEmail()));
            }catch(EntityExistsException | ApiException e){
                ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
                ctx.json(returnObject.put("msg", "User already exists"));
            }
        };
    }

    public Handler login(){
        return (ctx) -> {
            ObjectNode returnObject = objectMapper.createObjectNode();
            try{
                UserDTO user = ctx.bodyAsClass(UserDTO.class);
                System.out.println("USER IN LOGIN: "+ user);

                User verifiedUserEntity = userDAO.getVerifiedUser(user.getEmail(), user.getPassword());
                String token = tokenUtils.createToken(new UserDTO(verifiedUserEntity));
                ctx.status(200).json(new TokenDTO(token, user.getEmail()));

            }catch(EntityNotFoundException | ValidationException | ApiException e){
                ctx.status(401);
                System.out.println(e.getMessage());
                ctx.json(returnObject.put("msg", e.getMessage()));
            }
        };
    }

    public Handler authenticate(){
        // To check the users roles against the allowed roles for the endpoint (managed by javalins accessManager)
        // Checked in 'before filter' -> Check for Authorization header to find token.
        // Find user inside the token, forward the ctx object with userDTO on attribute
        // When ctx hits the endpoint it will have the user on the attribute to check for roles (ApplicationConfig -> accessManager)
        ObjectNode returnObject = objectMapper.createObjectNode();
        return ctx -> {
            if(ctx.method().toString().equals("OPTIONS")){
                ctx.status(200);
                return;
            }
            String header = ctx.header("Authorization");
            if(header == null){
                ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg", "Authorization header missing"));
                return;
            }
            String token = header.split(" ")[1];
            if(token == null){
                ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg","Authorization header malformed"));
                return;
            }
            UserDTO verifiedTokenUser;
            try {
                verifiedTokenUser = verifyToken(token);
                if(verifiedTokenUser == null){
                    ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg","Invalid User or Token"));
                }
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
            System.out.println("USER IN AUTHENTICATE: "+ verifiedTokenUser);
            ctx.attribute("user", verifiedTokenUser);
        };
    }

    public UserDTO verifyToken(String token) throws ApiException {
        boolean IS_DEPLOYED = (System.getenv("DEPLOYED") != null);
        //String SECRET = IS_DEPLOYED ? System.getenv("SECRET_KEY") : Utils.getPropertyValue("SECRET_KEY", "config.properties");
        String SECRET = "ghjyhtgrfeghjyhtgrfeghjyhtgrfeghjyhtgrfeghjyhtgrfeghjyhtgrfeghjyhtgrfeghjyhtgr";
        try {
            if(tokenUtils.tokenIsValid(token, SECRET) && tokenUtils.tokenNotExpired(token)){
                return tokenUtils.getUserWithRolesFromToken(token);
            }else {
                throw new NotAuthorizedException(403, "Token is not valid");
            }
        } catch(ParseException | JOSEException | NotAuthorizedException e){
            e.printStackTrace();
            throw new ApiException(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized. Could not verify token");
        }
    };

    public boolean authorize(UserDTO user, Set<String> allowedRoles) {
        // Called from the ApplicationConfig.setSecurityRoles

        AtomicBoolean hasAccess = new AtomicBoolean(false); // Since we update this in a lambda expression, we need to use an AtomicBoolean
        if (user != null) {
            user.getRoles().stream().forEach(role -> {
                if (allowedRoles.contains(role.toUpperCase())) {
                    hasAccess.set(true);
                }
            });
        }
        return hasAccess.get();
    }

}
