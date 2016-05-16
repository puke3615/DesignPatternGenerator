package com.mogujie.pattern.impl.clone;

import java.io.*;

/**
 * @author zijiao
 * @version 16/5/16
 * @Mark
 */
public class Client implements Serializable {

    public static void main(String[] args) {
        Client client = new Client().deepClone();
        System.out.println(client);
    }

    private Client deepClone() {
        Client obj = null;
        try {
            //convert to stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            //perform clone
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = (Client) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
