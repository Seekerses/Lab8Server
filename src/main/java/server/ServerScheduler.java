package server;

import clientserverdata.Reply;
import clientserverdata.Request;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ServerScheduler implements Runnable{

    ConcurrentLinkedQueue<DatagramChannel> availableChannels;
    private volatile ConcurrentLinkedQueue<InetSocketAddress> clients;
    private volatile ConcurrentLinkedQueue<Request> requests;
    private volatile ConcurrentLinkedQueue<Reply> replays;
    private ReentrantLock channelLock;
    private ReentrantLock collectionLock;

    private int listenersCount;
    private int handlerCount;

    public ServerScheduler(int listenersCount, int handlerCount){
        this.listenersCount = listenersCount;
        this.handlerCount = handlerCount;
        availableChannels = new ConcurrentLinkedQueue<>();
        clients = new ConcurrentLinkedQueue<>();
        requests = new ConcurrentLinkedQueue<>();
        replays = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {

        ExecutorService listeners = Executors.newFixedThreadPool(listenersCount);
        ExecutorService handlers = Executors.newFixedThreadPool(handlerCount);
        channelLock = new ReentrantLock();
        collectionLock = new ReentrantLock();

        for (int i = 0; i < listenersCount; i++){
            try {
                DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)
                        .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                        .bind(new InetSocketAddress("localhost",ServerController.getPort()+1+i));
                channel.configureBlocking(false);
                availableChannels.add(channel);
            }
            catch (IOException e){
                System.out.println("Failed to create a Datagram channel.");
            }
        }

        new Thread(() -> {
            while (true) {
                if (clients.size() != 0 && availableChannels.size() != 0) {
                    channelLock.lock();
                    DatagramChannel channel = availableChannels.poll();
                    channelLock.unlock();
                    InetSocketAddress client = clients.poll();
                    listeners.submit(new Receiver(client,channel));
                }
                if (requests.size() != 0) {
                    handlers.submit(new RequestHandler(requests.poll()));
                }
                if (replays.size() != 0 && availableChannels.size() != 0) {
                    channelLock.lock();
                    DatagramChannel channel = availableChannels.poll();
                    channelLock.unlock();
                    new Thread(new Sender(replays.poll(),channel)).start();
                }
            }
        }).start();
    }

    ConcurrentLinkedQueue<DatagramChannel> getAvailableChannels() {
        return availableChannels;
    }

    ConcurrentLinkedQueue<InetSocketAddress> getClients() {
        return clients;
    }

    ConcurrentLinkedQueue<Request> getRequests() {
        return requests;
    }

    ConcurrentLinkedQueue<Reply> getReplays() {
        return replays;
    }

    public ReentrantLock getCollectionLock() {
        return collectionLock;
    }
}
