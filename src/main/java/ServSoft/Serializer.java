package ServSoft;

import cliser.Request;

import java.io.*;
import java.util.Arrays;

public class Serializer {

    protected static <T> byte[] serialize(T obj){
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

    public static Request deserialize(byte[] data){
        try{
            if (data != null) {
                ObjectInput ois = new ObjectInputStream(new ByteArrayInputStream(data));
                return (Request) ois.readObject();
            }
            assert false;
            ObjectInput objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
            while (bufferedReader.ready()) {

            }
            objectInputStream.close();
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
