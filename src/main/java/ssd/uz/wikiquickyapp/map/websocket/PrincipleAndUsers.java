package ssd.uz.wikiquickyapp.map.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PrincipleAndUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String principle;
    private Long workerId;

    public PrincipleAndUsers(Long userId, String principle, Long workerId) {
        this.userId = userId;
        this.principle = principle;
        this.workerId = workerId;
    }

}
