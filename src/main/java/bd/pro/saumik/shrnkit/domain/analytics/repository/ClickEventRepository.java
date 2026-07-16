package bd.pro.saumik.shrnkit.domain.analytics.repository;

import bd.pro.saumik.shrnkit.domain.analytics.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClickEventRepository
        extends JpaRepository<ClickEvent, UUID> {

    long countByShortUrlId(UUID shortUrlId);

}