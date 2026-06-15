package ee.bitweb.wordcloud_worker2.service;

import ee.bitweb.wordcloud_worker2.entity.JobStatus;
import ee.bitweb.wordcloud_worker2.entity.TextJob;
import ee.bitweb.wordcloud_worker2.entity.WordCount;
import ee.bitweb.wordcloud_worker2.repository.TextJobRepository;
import ee.bitweb.wordcloud_worker2.repository.WordCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WordProcessingService {

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "but", "in", "on", "at", "to",
            "for", "of", "with", "by", "from", "is", "it", "as", "be",
            "was", "are", "were", "that", "this", "he", "she", "they",
            "we", "you", "i", "have", "has", "had", "do", "did", "not",
            "so", "if", "its", "than", "then"
    );

    private final TextJobRepository textJobRepository;
    private final WordCountRepository wordCountRepository;

    public WordProcessingService(TextJobRepository textJobRepository,
                                 WordCountRepository wordCountRepository) {
        this.textJobRepository = textJobRepository;
        this.wordCountRepository = wordCountRepository;
    }

    @Transactional
    public void process(UUID jobId, String content) {
        TextJob job = textJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));

        job.setStatus(JobStatus.IN_PROGRESS);
        textJobRepository.save(job);

        Map<String, Long> wordFrequencies = Arrays.stream(content.toLowerCase().split("[^a-zA-Z]+"))
                .filter(w -> !w.isBlank() && w.length() > 1 && !STOP_WORDS.contains(w))
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        List<WordCount> wordCounts = wordFrequencies.entrySet().stream()
                .map(e -> {
                    WordCount wc = new WordCount();
                    wc.setJobId(jobId);
                    wc.setWord(e.getKey());
                    wc.setCount(e.getValue().intValue());
                    return wc;
                })
                .toList();

        wordCountRepository.saveAll(wordCounts);

        job.setStatus(JobStatus.COMPLETE);
        textJobRepository.save(job);
    }
}
