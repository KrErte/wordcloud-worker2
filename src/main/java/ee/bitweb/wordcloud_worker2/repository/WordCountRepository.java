package ee.bitweb.wordcloud_worker2.repository;

import ee.bitweb.wordcloud_worker2.entity.WordCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordCountRepository extends JpaRepository<WordCount, Long> {
}
