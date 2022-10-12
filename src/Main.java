import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static final String IN_FILE_TXT = "inFile.txt";
    public static final String OUT_FILE_TXT = "outFile.txt";
    public static final String PATH_TO_MUSIC = "music";

    public static void main(String[] args) {
        String Url;
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT))) {
            while ((Url = inFile.readLine()) != null) {
                URL url = new URL(Url);

                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }
                //МУЗЫКА:
                //https://muzrecord.com/populjarnaja-muzyka/ (в файл ссылка)
                //https://s.muzrecord.com/files/24kgoldn-mistakes.mp3 (для неё регулярка)

                //КАРТИНКИ:
                //h//avatars.mds.yandex.net/i?id=1d7c8ca92d0f1da064e68e97cbd80680-5133450-images-thumbs&amp;n=13 (пример картинки из юрл)
                //https://fonwall.ru/ (название сайта в файл ссылка)
                Pattern email_pattern = Pattern.compile("h\\/\\/avatars.mds.yandex.net\\/(.+).jpg");
               // Pattern email_pattern = Pattern.compile("https:\\/\\/s.muzrecord.com\\/files\\/(.+).mp3");
                Matcher matcher = email_pattern.matcher(result);
                while (matcher.find()) {
                    outFile.write(matcher.group() + "\r\n");

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILE_TXT))) {
            String music;
            int count = 1;
            try {
                while ((music = musicFile.readLine()) != null) {
                    downloadUsingNIO(music, PATH_TO_MUSIC + count + ".jpg");
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