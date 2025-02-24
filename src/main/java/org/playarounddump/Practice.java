//package org.prac;
//
///*
//
//We want to read data from a database (RDS)
//Table (With Primary Key) -> RealTime 0.5M RPM
//Post that we want to sink that data to a Kafka Topic
//
//
//
//*/
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadPoolExecutor;
//
//// singleton to inatiate same object
//class Jdbc{
//    private String url;
//    private String user;
//    private String password;
//    private int poolSize;
//
//    private Jdbc(){
//    }
//
//    public Jdbc getInstance(String url, String user, String password, int poolSize){
//        return new Jdbc(url, user, password, poolSize);
//    }
//
//}
//
//
//class FetchQuery {
//
//
//    private int batchSize;
//    private String primaryKey;
//    private String timeStampKey;
//
//    ExecutorService executorService = Executors.newFixedThreadPool();
//
////    Thread thread = new Thread(new Runnable() {
////        @Override
////        public void run() {
////
////        }
////    });
////
////    thread.start();
////}
//
//
//    executor.shutdown();
//    executr.awaitTermination();
//
//
///*
//
//table;Tablename
//IncrementalKey: id;
//Timestamp:createdAt;
//jdbcUrl: url
//pollInterval:30
//BatchSize:10000
//Kafka:brokerUrl
//topicname:topic1
//onFlyrequest:10
//flush: enable
//deliverySemantic:atleastOnce
//maxBytesSize:
//
//Broker:
//compute + disk closely coupled
//vertical scale number cpu -> vcore and network bandhwith (n type instance)
//nvme disk
//ackmowl: ack, 1, none
//parition:
//
//3 node cluster -> 40 partions  1 -> 40 paritin
//each node: 13 parition of that topic
//one 14 parition ->
//round robin ->
//1 st paritin node
//1st copy would kept in node 2/node 3
//
// */
// */
// */
// */
//
// */
// */
// */
// */
// */
// */
// */
// */
// */
// */
//
// */
//
//1 partion -> 1 core (of that node/broker)
//10 nodes
//10 parition in each node
// */
// */
// */
//
// */
// */
// */
// */
// */
// */
// */
// */
// */
// */
// */
// */
// */
//
// */
// */
// */
// */
//
// */
//
//
//
// */
//
// */
// */
//
//
//
// */
//
//
//
//
//
//
//}
//
//
//public class Practice {
//
//
//
//
//
//
//}
