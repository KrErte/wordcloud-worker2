package ee.bitweb.wordcloud_worker2.messaging;

import java.util.UUID;

public record TextChunkMessage(UUID jobId, String filename, String content) {
}
