package com.company;

import java.io.*;

public class Serialization<T> {
    private String filename;

    public Serialization(String filename) {
        this.filename = filename;
    }

    void serialize(Object o) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(o);
        objOut.close();
        fileOut.close();
    }

    T deserialize() throws Exception {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream objIn = new ObjectInputStream(fileIn);
        T o = (T) objIn.readObject();
        objIn.close();
        fileIn.close();
        return o;
    }
}
