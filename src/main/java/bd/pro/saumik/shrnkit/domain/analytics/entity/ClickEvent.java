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
                @Index(name = "idx_click_visitor", columnList = "visitor_id"),
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

    @Column(name = "visitor_id", nullable = false)
    private UUID visitorId;

    @Column(nullable = false, length = 50)
    private String browser;

    @Column(nullable = false, length = 50)
    private String operatingSystem;

    @Column(nullable = false, length = 20)
    private String deviceType;

    @Column(nullable = false, length = 100)
    private String referrerSource;

    @Column(name = "clicked_at", nullable = false, updatable = false)
    private Instant clickedAt;

    @PrePersist
    void onCreate() {
        clickedAt = Instant.now();
    }
}