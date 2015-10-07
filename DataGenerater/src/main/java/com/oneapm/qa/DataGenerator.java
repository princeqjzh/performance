package com.oneapm.qa;

import java.util.StringTokenizer;
import java.io.*;

/**
 * 用途：读入内存、CPU原始数据，转化成json格式数据
 * 要求：数据采集延时5分钟（可变）
 *       数据采集间隔1秒（不可变）
 *       输入参数 agrus1:原始数据文件 agrus2:保存目标数据文件
 * Created by michael on 2015/9/30.
 */
public class DataGenerator {
    public static void main(String [] argus){
        String content = readFileByLines(argus[0]);
        writeFileByString(content,argus[1]);
    }

    public static String readFileByLines(String fileName){
        String output = "{";
        File file = new File(fileName);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            //一次读一行，读入null的时候文件结束
            while((tempString = reader.readLine()) != null){
                StringTokenizer st = new StringTokenizer(tempString,","); //劈字符串

                //让开秒数
                st.nextToken();

                //获取内存数据
                String mem = st.nextToken();

                //获取CPU数据
                String cpu = st.nextToken();

                //拼 数据单元
                String subData = line + ": {'memory': '" + mem + "', 'cpu': '" + cpu + "'}";

                if(line == 1){
                    output += subData;
                }else {
                    output += ", " + subData;
                }
                line++;
            }
            output += "}";
            reader.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            if (reader != null){
                try{
                    reader.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        System.out.println(output);
        return output;
    }

    public static void writeFileByString(String content,String fileName){
        FileWriter fw = null;
        File file = new File(fileName);
        try{
            if(!file.exists()){
                file.createNewFile();
            }

            fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(content,0,content.length()-1);
            out.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
