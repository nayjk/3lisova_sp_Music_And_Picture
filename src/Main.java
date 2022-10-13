
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static final String IN_FILE_TXT = "inFile.txt";

    public static final String OUT_FILEMUSIC_TXT = "outFile.txt";
    public static final String PATH_TO_MUSIC = "music/";
    public static final String OUT_FILEPICTURE_TXT = "outFilePicture.txt";
    public static final String PATH_TO_PICTURE = "picture/";
    public static void main(String[] args) {
        //MUSIC
        String Url;
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILEMUSIC_TXT))) {
            while ((Url = inFile.readLine()) != null) {
                URL url = new URL(Url);

                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }
                //МУЗЫКА:
                //https://muzrecord.com (в файл ссылка)
                //https://s.muzrecord.com/files/24kgoldn-mistakes.mp3 (для неё регулярка)

                Pattern email_pattern = Pattern.compile("https:\\/\\/s.muzrecord.com\\/files\\/(.+).mp3");
                Matcher matcher = email_pattern.matcher(result);
                int i = 0;
                while (matcher.find() & i < 3) {
                    outFile.write(matcher.group() + "\r\n");
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILEMUSIC_TXT))) {
            String music;
            int count = 1;
            try {
                while ((music = musicFile.readLine()) != null) {
                    downloadUsingNIO(music, PATH_TO_MUSIC + count + ".mp3");
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //PICTURE
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFilePicture = new BufferedWriter(new FileWriter(OUT_FILEPICTURE_TXT))) {
            while ((Url = inFile.readLine()) != null) {
                URL url = new URL(Url);

                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }


                //КАРТИНКИ:
                //https://muzrecord.com/uploads/posts/2022-10/1665098333_muzrecord_com.png

                Pattern email_pattern = Pattern.compile("https:\\/\\/muzrecord.com\\/uploads\\/posts\\/(.+)muzrecord.png");
                Matcher matcher = email_pattern.matcher(result);
                while (matcher.find()) {
                    outFilePicture.write(matcher.group() + "\r\n");

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILEPICTURE_TXT))) {
            String music;
            int count = 1;
            try {
                while ((music = musicFile.readLine()) != null) {
                    downloadUsingNIO(music, PATH_TO_PICTURE + count + ".png");
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream stream = new FileOutputStream(file);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        stream.close();
        byteChannel.close();
    }
}



