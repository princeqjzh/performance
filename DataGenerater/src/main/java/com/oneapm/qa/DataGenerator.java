package com.oneapm.qa;

import java.util.StringTokenizer;
import java.io.*;

/**
 * 用途：读入内存、CPU原始数据，转化成json格式数据
 * 要求：数据采集延时5分钟（可变）
 *       数据采集间隔1秒（不可变）
 *       输入参数 argus[0]:原始数据文件 argus[1]:保存目标数据文件 argus[2]:监控文件的扩展名
 * Created by michael on 2015/9/30.
 */
public class DataGenerator {
    public static void main(String [] argus){
        int count = countLines(argus[0],argus[2]);
        String content = readFileByLines(argus[0],count);
        writeFileByString(content,argus[1]);
    }

    /**
     * @param
     * @param extName 计算文件扩展名
     * 计算当前目录下所有（*.extName）文件的最少行数
     * @return int 数据文件中行数最小的行数值
     */
    public static int countLines(String readFileName,String extName){
        int totalLines = 0;

        File file = new File(readFileName);
        String filePath = null; //当前路径
        String fileName = null; //readFileName文件名
        try {
            filePath = file.getCanonicalPath();
            fileName = file.getName();
            filePath = filePath.replace(fileName,"");
            File path = new File(filePath);
            if(path.isDirectory()){
                File[] dirFiles = path.listFiles();
                for(File f : dirFiles){
                    if(!f.isDirectory()){
                        String fName = f.getName();
                        String fExt = fName.substring(fName.lastIndexOf(".") + 1);
                        if(fExt.equalsIgnoreCase(extName)){
                            //读入文件，数行数
                            FileReader in = new FileReader(f);
                            LineNumberReader reader = new LineNumberReader(in);
                            String strLine = reader.readLine();
                            int tempTTLine = 0;
                            while(strLine != null){
                                tempTTLine++;
                                strLine = reader.readLine();
                            }
                            reader.close();
                            in.close();
//                            System.out.println(fName + " &&& fExt = " + fExt + " &&& " + f.getAbsolutePath() + " &&& lineNumber: " + tempTTLine);
                            if(totalLines > 0){
                                if(tempTTLine < totalLines){
                                    totalLines = tempTTLine;
                                }
                            }else{
                                totalLines = tempTTLine;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("totalLines = " + totalLines);
        return totalLines;
    }

    /**
     * 读入原始数据文件，将数据文件中的字符格式化成JSON字符串。
     * @param fileName 文件名
     * @param totalLines 期望生成的数据单元个数
     * @return String JSON字符串
     */
    public static String readFileByLines(String fileName, int totalLines){
        String output = "{";
        File file = new File(fileName);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            //一次读一行，读入null的时候文件结束
            while((tempString = reader.readLine()) != null && line <= totalLines){
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

    /**
     * 写文件
     * @param content 内容
     * @param fileName 文件名
     */
    public static void writeFileByString(String content,String fileName){
        FileWriter fw = null;
        File file = new File(fileName);
        try{
            if(!file.exists()){
                file.createNewFile();
            }

            fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(content,0,content.length());
            out.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
