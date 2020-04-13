package designdemo;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author wusd
 * @description 通过将方法改写成Lambda表达式的形式，可以实现命令式输出，自定义输出内容。
 * @createtime 2019/04/11 9:45
 */
public class Round {
    @Test
    public void test() throws IOException {
        String readFile = processFile((BufferedReader br) -> {
            try {
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public static String processFile(BufferReaderProcessor processor)throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
            return processor.process(br);
        }
    }
}


@FunctionalInterface
interface BufferReaderProcessor{
    String process(BufferedReader br);
}