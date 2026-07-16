package bd.pro.saumik.shrnkit.domain.analytics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "click_events",
        indexes = {
                @Index(name = "idx_click_url", columnList = "short_url_id"),
                @Index(name = "idx_click_time", columnList = "clicked_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "short_url_id", nullable = false)
    private UUID shortUrlId;

    @Column(name = "clicked_at", nullable = false, updatable = false)
    private Instant clickedAt;

    @PrePersist
    void onCreate() {
        clickedAt = Instant.now();
    }
}