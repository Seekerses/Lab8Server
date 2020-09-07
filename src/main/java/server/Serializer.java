package server;

import clientserverdata.Request;

import java.io.*;
import java.util.Arrays;

class Serializer {

    static <T> byte[] serialize(T obj){
        byte[] buff;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            buff = baos.toByteArray();
            return buff;
        }
        catch (IOException e){
            System.out.print("Some class is unserializable");
            return null;
        }
    }

    static Request deserialize(byte[] data){
        try{
            if (data != null) {
                ObjectInput ois = new ObjectInputStream(new ByteArrayInputStream(data));
                return (Request) ois.readObject();
            }
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
