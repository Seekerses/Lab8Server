package server;

import java.util.Arrays;

class PacketUtils {

    static boolean checkHash(byte[] data){
        int hashCode = Arrays.hashCode(Arrays.copyOfRange(data,0,1012));
        int code = uniteIntoInt(Arrays.copyOfRange(data,1013,1024));
        return (hashCode == code * data[1012]);
    }

    static byte[] merge(byte[] a1, byte[] a2){
        byte[] result = Arrays.copyOf(a1,a1.length + a2.length);
        int i = 0;
        for (byte b : a2){
            result[a1.length + i] = b;
            i++;
        }
        return result;
    }

    private static byte[] sepOnBytes(int code){
        byte[] res = new byte[11];
        int i = 0;
        while( code / 10 > 0 ){
            res[i] = (byte) (code % 10);
            code = code / 10;
            i++;
        }
        res[i] = (byte) (code);
        res[i+1] = 111;
        return res;
    }

    private static int uniteIntoInt(byte[] data){
        int res = 0;
        int i = 0;
        for (byte b: data) {
            if (b == (byte) 111) break;
            res += b*Math.pow(10,i);
            i++;
        }
        return res;
    }

    static byte[] formatData(byte[] data){
        int hashCode  = Arrays.hashCode(data);
        byte[] result = Arrays.copyOf(data,data.length + 12);
        if (hashCode > 0 )
            result[data.length] = 1;
        else
            result[data.length] = -1;
        byte[] arrCode = sepOnBytes(Math.abs(hashCode));
        int i = 1;
        for (byte b: arrCode){
            result[data.length+i] = b;
            i++;
        }
        return result;
    }
}
