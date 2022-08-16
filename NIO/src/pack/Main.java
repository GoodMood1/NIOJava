package pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String str = "Hello";
        Path path = Paths.get("file.txt");
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try (FileChannel channel = FileChannel.open(path, CREATE, WRITE, READ)) {
            buffer.put(str.getBytes());
            buffer.flip();
            channel.write(buffer);

            buffer.clear();
            channel.read(buffer);
            buffer.flip();

            str = new String(buffer.array(), buffer.position(), buffer.limit(), "cp1251");
            buffer.clear();

            FileChannel create_new = FileChannel.open(path, CREATE_NEW, WRITE);
            buffer.put(str.getBytes());
            buffer.flip();
            create_new.write(buffer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}