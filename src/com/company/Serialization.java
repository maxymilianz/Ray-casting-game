package com.company;

import java.io.*;

public class Serialization {
    private String filename;

    private Class c;

    public Serialization(String filename, Class c) {
        this.filename = filename;
        this.c = c;
    }

    void serialize(Object o) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(o);
        objOut.close();
        fileOut.close();
    }

    Object deserialize() throws Exception {
        File file = new File(filename);

        if (!file.exists()) {
            Object object = c.newInstance();
            serialize(object);
            return object;
        }

        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream objIn = new ObjectInputStream(fileIn);
        Object object = (Class.forName(c.getName())).cast(objIn.readObject());
        objIn.close();
        fileIn.close();
        return object;
    }
}
