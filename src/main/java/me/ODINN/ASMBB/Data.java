package me.ODINN.ASMBB;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Data<T> implements PersistentDataType<byte[],T> {


    private Class<T> dataClass;

    public Data(Class<T> dataClass){
        this.dataClass = dataClass;
    }




    @Override
    public Class<T> getComplexType() {
        return dataClass;
    }

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public byte[] toPrimitive(T arg0, PersistentDataAdapterContext arg1) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(byteOut);
            bukkitOut.writeObject(arg0);
            bukkitOut.flush();

            return byteOut.toByteArray();

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public T fromPrimitive(byte[] arg0, PersistentDataAdapterContext arg1) {
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(arg0);
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(byteIn);

            return (T) bukkitIn.readObject();
        }catch(Exception e) {
            return null;
        }

    }
}
