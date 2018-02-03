package com.zou.count;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Count {


    /**
     * 不使用原子变量实现计数
     * 运行多次，发现一直是小于1500的，这就是因为count ++并不是一个原子操作。
     */

    public int count = 0;

    //开启一个线程
    static class Job implements Runnable {

        private CountDownLatch countDownLatch;
        private Count count;

        public Job(Count count, CountDownLatch countDownLatch) {
            this.count = count;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            //count ++并不是一个原子操作
            count.count++;
            countDownLatch.countDown();

        }
    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1500);
        Count count = new Count();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1500; i++) {
            executorService.execute(new Job(count, countDownLatch));
        }
        countDownLatch.await();
        System.out.println(count.count);
        executorService.shutdown();

    }


}
