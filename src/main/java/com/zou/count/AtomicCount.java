package com.zou.count;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCount {

    /**
     * 使用Java提供的原子类实现count操作.
     */

    public AtomicInteger count = new AtomicInteger(0);

    static class Job implements Runnable {

        private AtomicCount count;
        private CountDownLatch countDownLatch;

        public Job(AtomicCount count, CountDownLatch countDownLatch) {
            this.count = count;
            this.countDownLatch = countDownLatch;
        }


        @Override
        public void run() {

            boolean isSuccess = false;
            while (!isSuccess) {
                int countValue = count.count.get();
                isSuccess = count.count.compareAndSet(countValue, countValue + 1);
            }
            countDownLatch.countDown();

        }
    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1500);
        AtomicCount count = new AtomicCount();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1500; i++) {
            executorService.execute(new Job(count, countDownLatch));
        }

        countDownLatch.await();
        System.out.println(count.count.get());
        executorService.shutdown();
    }
}
