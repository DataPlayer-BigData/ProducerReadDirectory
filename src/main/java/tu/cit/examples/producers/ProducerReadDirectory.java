package tu.cit.examples.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import java.nio.file.*;
import java.util.List;
import java.util.Properties;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class ProducerReadDirectory {
    private static final Logger logger = LogManager.getLogger();

    public static boolean sendMessage(Path fileName) throws IOException {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);

        KafkaProducer<String,StudentModel> producer = new KafkaProducer<String, StudentModel>(props);
        logger.info("Producer is created...");

        ReadCSV readCSV = new ReadCSV("data/source/"+fileName);
        List studentList = readCSV.ReadCSVFile();

        for(Object studentObject: studentList){
            StudentModel stdRecord = (StudentModel) studentObject;

            ProducerRecord<String,StudentModel> record = new ProducerRecord<String, StudentModel>("student-details",stdRecord.getDept(),stdRecord);

            producer.send(record);
        }

        producer.close();

        Path temp = Files.move(Paths.get("data/source/"+fileName),Paths.get("data/completed/"+fileName));
        if(temp != null){
            System.out.println(fileName + "has been read and sent all records to Kafka...");
            return true;
        }else{
            System.out.println("Failed...");
            return false;
        }

    }
    public static void main(String[] args) {
     try{
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get("data/source/");
            dir.register(watcher,ENTRY_CREATE);
            System.out.println("Watcher service is registered for dir : " + dir.getFileName());
            while(true){
                WatchKey key;
                try{
                    key = watcher.take();
                }catch(InterruptedException ex){
                    return ;
                }

                for(WatchEvent<?> event : key.pollEvents()){
                    WatchEvent.Kind<?> kind = event.kind();

                    //Get the file name
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    if(kind == ENTRY_CREATE){
                        if(ProducerReadDirectory.sendMessage(fileName)){
                            logger.info("All records from " + fileName + " has been send to Kafka");
                        }
                    }
                }

                boolean valid = key.reset();
                if(!valid){
                    break;
                }
            }

        }catch(IOException ex){
            ex.printStackTrace();
        }


    }
}
