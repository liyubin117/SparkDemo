package Kafka;

/**
 * Created by liyubin on 2018/7/2 0002.
 */
public class KafkaConfig {
    public static final String HOSTNAME="spark";
    public static final String ZOOKEEPER=HOSTNAME+":2181";
    public static final String KAFKA=HOSTNAME+":9092";
    public static final String SERIALIZER = "kafka.serializer.StringEncoder";
    public static final String ACKS = "1";
    public static final String GROUP_ID="test_group";

}
