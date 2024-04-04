package groupone.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.id.GUIDGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ResetRequest {
    @Id
    @Setter(AccessLevel.NONE)
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    private String GUID;
    private LocalDateTime expires;

    /**
     * Generates a unique ID for the GUID which means it is practically unable to be guessed.
     * @param user
     */
    public ResetRequest(User user){
        this.user = user;
        GUID = UUID.randomUUID().toString();
    }

    @PrePersist
    private void onPersist(){
        expires = LocalDateTime.now().plusHours(24);
    }

    public boolean isValid(){
        return expires.isAfter(LocalDateTime.now());
    }
}
