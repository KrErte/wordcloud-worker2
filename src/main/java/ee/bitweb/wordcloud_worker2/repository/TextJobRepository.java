package ee.bitweb.wordcloud_worker2.repository;

import ee.bitweb.wordcloud_worker2.entity.TextJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TextJobRepository extends JpaRepository<TextJob, UUID> {
}
