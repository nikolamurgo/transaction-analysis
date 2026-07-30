package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class ParallelLineReader {

    private static final String POISON_PILL = new String("__END__");

    // reads filePath sequentially on one producer thread and hands each line to
    // workerCount consumer threads via a shared queue, each running lineHandler
    public static void process(String filePath, int workerCount, boolean skipHeader, Consumer<String> lineHandler)
            throws InterruptedException {

        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10_000);

        Thread producer = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath), 65536)) {
                if (skipHeader) br.readLine();

                String line;
                while ((line = br.readLine()) != null) {
                    queue.put(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                for (int i = 0; i < workerCount; i++) {
                    try {
                        queue.put(POISON_PILL);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        Thread[] consumers = new Thread[workerCount];
        for (int i = 0; i < workerCount; i++) {
            consumers[i] = new Thread(() -> {
                try {
                    String line;
                    while ((line = queue.take()) != POISON_PILL) {
                        lineHandler.accept(line);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        producer.start();
        for (Thread consumer : consumers) consumer.start();

        producer.join();
        for (Thread consumer : consumers) consumer.join();
    }
}
