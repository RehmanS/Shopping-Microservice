package com.eshop.order.kafka;

import com.eshop.order.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

@Slf4j
public class NotificationPartitioner implements Partitioner {
    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {

        String orderNumber = ((OrderPlacedEvent) o1).getOrderNumber();

        int chosenPartition = Math.abs(orderNumber.hashCode()) % 2;
        int numPartitions = cluster.partitionCountForTopic("NOTIFICATION_TOPIC");
        return chosenPartition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
