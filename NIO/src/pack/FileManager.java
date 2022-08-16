package pack;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.*;

public class FileManager {
    private String path_name;

    FileManager(String path_name) {
        this.path_name = path_name + ".txt";
    }

    public String getPath_name() {
        return path_name;
    }

    public void setPath_name(String path_name) {
        this.path_name = path_name + ".txt";
    }

    public String makeNote(String text) {
        if (this.path_name == "") {
            return "Path can not be null.";
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        Path path = Paths.get(this.path_name);
        String note = dtf.format(LocalDateTime.now()) + " " + text + "\n";

        if (Files.exists(path)) {
            try (FileChannel channel = FileChannel.open(path, APPEND, WRITE)) {
                buffer.put(note.getBytes("cp1251"));
                buffer.flip();
                channel.write(buffer);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return "Note successfully created.";
        }

        try (FileChannel channel = FileChannel.open(path, CREATE, WRITE, READ)) {
            String str = new String(note.getBytes(), "cp1251");
            buffer.put(str.getBytes());
            buffer.flip();
            channel.write(buffer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "File and note was created.";

    }

    public void getNotes() {
        Path path = Paths.get(this.path_name);

        if (Files.exists(path)) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);

            try (FileChannel channel = FileChannel.open(path, READ)) {
                channel.read(buffer);
                buffer.flip();

                String str = new String(buffer.array(), buffer.position(), buffer.limit(), "cp1251");
                System.out.println(str);
                buffer.clear();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FileManager file = new FileManager("");
        Scanner sc = new Scanner(System. in );
        String path_name = "";
        boolean loop = true;

        while (loop) {
            System.out.print("\033[H\033[2J");
            System.out.println("Enter file name:");
            path_name = sc.nextLine();

            while (path_name.trim().length() > 0 && loop) {
                file.setPath_name(path_name);

                String choose = "";
                System.out.println("Choose what you want:");
                System.out.println("1 - add note");
                System.out.println("/r - read all notes");
                System.out.println("0 - exit");

                choose = sc.nextLine();

                switch (choose) {
                    case "1": {
                        String note = "";
                        System.out.println("Enter note:");
                        note = sc.nextLine();

                        if (note.trim().length() <= 0) {
                            System.out.println("The note text can not be null.");
                            break;
                        }

                        file.makeNote(note);
                        break;
                    }
                    case "/r": {
                        System.out.println(123);
                        file.getNotes();
                        break;
                    }
                    case "0": {
                        loop = false;
                        break;
                    }
                }
            }
        }
//        file.setPath_name("test");
//        file.makeNote("Some note");
//        file.makeNote("Some1 note");
//        file.makeNote("Some2 note");
//
//        file.getNotes();
    }
}
