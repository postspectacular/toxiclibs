package toxi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class EnvTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("=========env=========");
        Map<String, String> env = System.getenv();
        for (String id : env.keySet()) {
            System.out.println(id + "=" + env.get(id));
        }
        System.out.println("=========props");
        System.getProperties().list(System.out);
        // process
        ProcessBuilder pb = new ProcessBuilder("dot", "-V");
        pb.redirectErrorStream(true);
        Process p;
        try {
            p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
