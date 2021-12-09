package ssd.uz.wikiquickyapp.collection;

public interface WorkerBalanceChangeCol {
    Long getId();

    Double getBalance();

    String getDiff();

    String getBonus();

    Long getOrderId();

    Long getWorkerId();
}
