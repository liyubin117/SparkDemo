package Kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by liyubin on 2018/7/2 0002.
 */
public class KafkaProducer implements Runnable{
    private Properties properties;
    private String topic;
    private Producer producer;

    public KafkaProducer(String topic){
        properties = new Properties();
        properties.put("metadata.broker.list",KafkaConfig.KAFKA);
        properties.put("serializer.class",KafkaConfig.SERIALIZER);
        properties.put("request.required.acks",KafkaConfig.ACKS);
        this.topic = topic;
        producer = new Producer<Integer,String>(new ProducerConfig(properties));
    }

    @Override
    public void run(){
        int i=0;
        while(true){
            String message = "message_"+i;
            producer.send(new KeyedMessage<Integer,String>(this.topic, message));
            System.out.println("sent: " + message);

            i++;

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
