package com.scheduler.articleservice.infra.common;

import com.scheduler.articleservice.testSet.IntegrationTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class SnowflakeTest {
    Snowflake snowflake = new Snowflake();

    @Test
    void nextIdTest() throws ExecutionException, InterruptedException {
        // given
        //10개의 쓰레드 풀
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<List<Long>>> futures = new ArrayList<>();

        //1000번
        int repeatCount = 1000;

        //1000개의 아이디
        int idCount = 1000;

        // when
        for (int i = 0; i < repeatCount; i++) {
            futures.add(executorService.submit(() -> generateIdList(snowflake, idCount)));
        }

        // then
        List<Long> result = new ArrayList<>();
        for (Future<List<Long>> future : futures) {
            List<Long> idList = future.get();
            for (int i = 1; i < idList.size(); i++) {
                //오름차순인가
                assertThat(idList.get(i)).isGreaterThan(idList.get(i - 1));
            }
            result.addAll(idList);
        }

        //중복이 있는가. 100만개 기준
        assertThat(result.stream().distinct().count()).isEqualTo(repeatCount * idCount);

        executorService.shutdown();
    }

    List<Long> generateIdList(Snowflake snowflake, int count) {
        List<Long> idList = new ArrayList<>();
        while (count-- > 0) {
            idList.add(snowflake.nextId());
        }
        return idList;
    }

    @Test
    void nextIdPerformanceTest() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        int repeatCount = 1000;
        int idCount = 1000;
        CountDownLatch latch = new CountDownLatch(repeatCount);

        // when
        long start = System.nanoTime();
        for (int i = 0; i < repeatCount; i++) {
            executorService.submit(() -> {
                generateIdList(snowflake, idCount);
                latch.countDown();
            });
        }

        latch.await();

        long end = System.nanoTime();
        System.out.printf("times = %s ms%n", (end - start) / 1_000_000);

        executorService.shutdown();
    }

}