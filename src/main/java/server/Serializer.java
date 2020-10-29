package server;

import clientserverdata.Reply;
import clientserverdata.Request;

import java.io.*;

class Serializer {

    static <T> byte[] serialize(T obj){
        byte[] buff;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            buff = baos.toByteArray();
            objectOutputStream.close();
            return buff;

        }
        catch (IOException e){
            e.printStackTrace();
            System.out.print("Some class is unserializable");
            return null;
        }
    }

    static Request deserialize(byte[] data){
        try(ObjectInput ois = new ObjectInputStream(new ByteArrayInputStream(data))){
            return (Request) ois.readObject();
        }
        catch (IOException ioException) {
            System.out.println("Oh no, some IO exception occurs.");
            ioException.printStackTrace();
        }
        catch (ClassNotFoundException classNotFoundException){
            System.out.println("An unknown format response was received from the server, " +
                    "please change the connection to the correct server.");
        }
        return null;
    }

}
