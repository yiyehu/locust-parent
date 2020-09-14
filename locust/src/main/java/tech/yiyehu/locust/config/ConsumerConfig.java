package tech.yiyehu.locust.config;

public class ConsumerConfig {

    // consumer thread pool type: cached, fixed, limit, eager
    private String threadPool;

    // consumer thread pool core thread size
    private Integer coreThreads;

    // consumer thread pool thread size
    private Integer threads;

    public String getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(String threadPool) {
        this.threadPool = threadPool;
    }

    public Integer getCoreThreads() {
        return coreThreads;
    }

    // consumer threadpool queue size
    private Integer queues;

    public void setCoreThreads(Integer coreThreads) {
        this.coreThreads = coreThreads;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getQueues() {
        return queues;
    }

    public void setQueues(Integer queues) {
        this.queues = queues;
    }
}
