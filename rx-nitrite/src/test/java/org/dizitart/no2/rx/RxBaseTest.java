package org.dizitart.no2.rx;

import io.reactivex.Flowable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.index.annotations.Id;
import org.junit.Before;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * @author Anindya Chatterjee
 */
@Slf4j
public class RxBaseTest {
    List<Employee> testData;
    RxNitrite db;
    String dbPath = getRandomTempDbFile();

    @Before
    public void setUp() {
        db = RxNitrite.builder()
                .filePath(dbPath)
                .openOrCreate("test-user", "test-password");

        Employee e1 = new Employee("John Doe", 35);
        Employee e2 = new Employee("Jane Doe", 30);
        testData = Flowable.fromArray(e1, e2).toList().blockingGet();
    }

    private static String getRandomTempDbFile() {
        String dataDir = System.getProperty("java.io.tmpdir")
                + File.separator + "rx-nitrite" + File.separator + "data";
        File file = new File(dataDir);
        if (!file.exists()) {
            assertTrue(file.mkdirs());
        }
        return file.getPath() + File.separator + UUID.randomUUID().toString() + ".db";
    }

    @Data
    @AllArgsConstructor
    static class Employee {
        @Id
        private String name;
        private Integer age;

        public Employee() {}
    }

    static abstract class BaseSubscriber<T> implements Subscriber<T> {

        @Override
        public void onSubscribe(Subscription s) {
            log.info("Subscribed to {}", s);
        }

        @Override
        public void onError(Throwable t) {
            log.error("Error in subscriber", t);
        }

        @Override
        public void onComplete() {
            log.info("Subscriber completed");
        }
    }
}
