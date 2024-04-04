package groupone.config;

import groupone.dtos.EmailCredentialsDTO;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Mailer {
    /**
     * Using the jakarta mailing library and a separate SMTP server, we send an email.
     * @param to All the people you want to receive the email.
     * @param subject what the message is about.
     * @param msg Content of the email.
     * @return if it succeeded.
     */
    public static boolean WriteEmail(String to, String subject, String msg) {
        try {
            // Creating a Message object from companies template
            Message message = startMessageFromServer();

            // Adding a receiver for the message.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Adding what the message is about.
            message.setSubject(subject);

            // Making a html body for the email
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-16");

            // Making it part of a multipart in case we later want to add attachments
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            // Adding it to the message
            message.setContent(multipart);

            // finally sending it via the jakarta.mail.Service library
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static Message startMessageFromServer() throws MessagingException {
        EmailCredentialsDTO credentials = getCredentials();
        assert credentials != null;
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", credentials.getHost());
        prop.put("mail.smtp.port", credentials.getPort());
        prop.put("mail.smtp.ssl.trust", credentials.getHost());
        MimeMessage message = new MimeMessage(
                Session.getInstance(prop, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(credentials.getUsername(), credentials.getPassword());
                    }
                })
        );
        message.setFrom(credentials.getEmail());
        return message;
    }

    private static final String credsFile = "SP2-WorkshopAPI/.creds.ser";

    public static EmailCredentialsDTO getCredentials() {
        if(System.getenv("DEPLOYED")!=null) {
            EmailCredentialsDTO creds = new EmailCredentialsDTO();
            creds.setPort(Integer.parseInt(System.getenv("SMTP_PORT")));
            creds.setHost(System.getenv("SMTP_HOST"));
            creds.setUsername(System.getenv("SMTP_USERNAME"));
            creds.setPassword(System.getenv("SMTP_PASSWORD"));
            creds.setEmail(System.getenv("SMTP_FROM_EMAIL"));
            return creds;
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(credsFile))) {
                Object obj = ois.readObject();
                if (obj instanceof EmailCredentialsDTO) {
                    return (EmailCredentialsDTO) obj;
                } else {
                    throw new IllegalArgumentException("The deserialized object is not of the expected type.");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * a main function to generate a credential for the smtp server. (email service)
     */
    public static void main(String[] args) {
        EmailCredentialsDTO credentials = getEmailCredentialsWithPrompt();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(credsFile))) {
            oos.writeObject(credentials);
            System.out.println("Credentials successfully created at: " + credsFile);
        } catch (IOException e) {
            System.err.println("ERROR couldn't serialise");
            e.printStackTrace();
            System.err.println("ERROR couldn't serialise");
        }
    }

    /**
     * a function to construct a EmailCredentialDTO for credentials via the commandPrompt
     * @return the constructed EmailCredentialsDTO
     */
    @NotNull
    private static EmailCredentialsDTO getEmailCredentialsWithPrompt() {
        EmailCredentialsDTO credentials = new EmailCredentialsDTO();
        Scanner sc = new Scanner(System.in);
        ArrayList<Supplier<Integer>> stages = new ArrayList<>();
        //// Atomic use is so the lambda functions can change the variable as that's very important here.
        int stage = 0;
        stages.add(() -> {
            System.out.print("port: ");
            String input = sc.nextLine();
            int port = 0;
            try {
                port = Integer.parseInt(input);
            } catch (NumberFormatException ignore){
            }
            if (port > 0) {
                credentials.setPort(port);
                return 1;
            } else {
                System.out.println("port must be positive and above 0.");
            }
            return 0;
        });
        stages.add(() -> consumeText(sc, "Host", credentials::setHost));
        stages.add(() -> consumeText(sc, "Email", credentials::setEmail));
        stages.add(() -> consumeText(sc, "Username", credentials::setUsername));
        stages.add(() -> consumeText(sc, "Password", credentials::setPassword));
        while (stage < stages.size()) {
            stage += stages.get(stage).get();
        }
        return credentials;
    }

    /**
     * uses the scanner to consume a line of text
     *
     * @param sc          Scanner that reads the input
     * @param title       a Label that is shown to the prompt
     * @param setConsumer a String consumer that is applied when the text is not empty.
     * @return -1 on an empty string, 1 on a success.
     */
    private static int consumeText(Scanner sc, String title, Consumer<String> setConsumer) {
        System.out.print(title + ": ");
        String text = sc.nextLine();
        if (text.isEmpty()) {
            return -1;
        } else {
            setConsumer.accept(text);
            return 1;
        }
    }
}
