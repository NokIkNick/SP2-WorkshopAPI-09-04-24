package groupone.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.JOSEException;
import groupone.config.Mailer;
import groupone.daos.RequestDAO;
import groupone.daos.UserDAO;
import groupone.dtos.TokenDTO;
import groupone.dtos.UserDTO;
import groupone.exceptions.ApiException;
import groupone.exceptions.NotAuthorizedException;
import groupone.exceptions.ValidationException;
import groupone.model.ResetRequest;
import groupone.model.User;
import groupone.utils.TokenUtils;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class SecurityController {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static UserDAO userDAO;
    private static RequestDAO requestDAO;
    private static final TokenUtils tokenUtils = new TokenUtils();
    private static SecurityController instance;

    public static SecurityController getInstance(boolean isTesting){
        if(instance == null){
            instance = new SecurityController();
            userDAO = UserDAO.getInstance(isTesting);
            requestDAO = RequestDAO.getInstance(isTesting);
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

    /**
     * Called to email the user their reset link, given they haven't requested one within 24 hours that hasn't been resolved.
     */
    public Handler requestPasswordReset() {
        return (ctx) -> {
            // Get which email it's about.
            String email = ctx.queryParam("email");
            if(email==null || email.isEmpty()){
                email = ctx.pathParam("email");
            }
            if(email==null || email.isEmpty()){
                ctx.status(HttpStatus.BAD_REQUEST);
                return;
            }
            // Check if the email exists in the database.
            User user = userDAO.getById(email);
            if(user==null){
                ctx.status(HttpStatus.BAD_REQUEST);
                return;
            }
            // Reject spamming the user .
            ResetRequest request = requestDAO.getById(email);
            if(request!=null){ // Does a request exist in the database?
                if(request.isValid()){ /// Has it not expired yet?
                    ctx.status(HttpStatus.TOO_MANY_REQUESTS);
                    return;
                } else { // Expired, should be cleaned up
                    // cleanup all expired request.
                    requestDAO.requestCleanup();
                }
            }
            // Success!
            // Create a request and persist it.
            request = new ResetRequest(user); // reusing variable as that's more performant, yet correct naming.

            // Get the mails body also known as message.
            String msg = constructResetPasswordRequestMessage(ctx, request);

            // Tell the smtp server to send the email.
            boolean succeeded = Mailer.WriteEmail(email, "Password reset has been requested", msg);
            if(succeeded){
                ctx.status(HttpStatus.OK);
                requestDAO.create(request); //Persisting it as it worked
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };
    }

    public Handler resetPassword(){
        return (ctx) -> {
            // Get which email it's about.
            String email = ctx.queryParam("email");
            // Get guid to validate request.
            String guid = ctx.queryParam("guid");
            // Get user and if the user exists also the request.
            User user = userDAO.getById(email);
            ResetRequest request = null;
            if(user != null){
                request = requestDAO.getById(email);
            }
            // Checks if everything is in order, otherwise rejects request.
            if(user == null || request == null || !request.isValid() || !request.getGUID().equals(guid)){
                ctx.status(HttpStatus.BAD_REQUEST);
                return;
            }

            // Success!
            // Generates a new password randomly and sends it via email.
            String password = UUID.randomUUID().toString().replace("-", "");

            // Construct message.
            String msg = constructResetPasswordMessage(ctx, password);

            // Send the mail.
            boolean succeeded = Mailer.WriteEmail(email, "Your password has been changed", msg);
            if(succeeded){
                // Actually change the password.
                user.setPassword(password);
                userDAO.update(user, user.getEmail());
                requestDAO.delete(email);
            }
        };
    }

    @NotNull
    private static String constructResetPasswordRequestMessage(Context ctx, ResetRequest request) {
        // the port - append if it's not a standard port (80 or 443).
        int port = ctx.req().getServerPort();
        String portString = (port != 80 && port != 443 ? ":" + port : "");
        return "<html>" + // html start

               "<body style=\"align-content:middle\">" + // body start

               "<h1>A password request has been requested.</h1>" + // header to let the user know what this is about

               "<p>" + // start of information paragraph
               "If you haven't requested a reset, you can safely ignore this message.\n" + // ignore if not you.
               "You wouldn't receive any request for the next 24 hours, unless the link is used." + // 24 hours message.
               "</p>" + // end of information paragraph

               // start of the link that will reset the password
               "<a href=\"" + // start of the link tag
               ctx.req().getScheme() + "://" + // start websites url, http or https
               ctx.req().getServerName() + // the url
               portString + // the port of the url
               "/api/auth/reset/password" + // path to request
               "?guid=" + request.getGUID() + // random unique id
               "&email=" + request.getUser().getEmail() + // email
               "\">" + // end of the start link tag
               "Click here to reset your password." + // the content the user sees
               "</a>" + // end link tag
               // end of the link that will reset the password

               "</body>" + // end of body
               "</html>";
    }
    @NotNull
    private static String constructResetPasswordMessage(Context ctx, String password) {


        String sb = "<html>" + // html start

                    "<body style=\"align-content:middle\">" + // body start

                    // header to let the user know what this is about
                    "<h1>Your password has been changed to this temporary one.</h1>" +

                    // start of the link that will reset the password
                    "<p>Your temporary password: " +
                    password +
                    "</p>" +
                    // end of the link that will reset the password

                    "<p>" + // start of information paragraph
                    "If you haven't requested a reset, your email has been compromised.\n" + // ignore if not you.
                    "Change your emails password and request a new password change." + // 24 hours message.
                    "</p>" + // end of information paragraph

                    "</body>" + // end of body
                    "</html>"; // end of html

        return sb;
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
    }

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
