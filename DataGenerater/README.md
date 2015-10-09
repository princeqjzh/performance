DataGenerator用途
1. 读入原始的CPU 内存数据信息
2. 将这些信息输出为JSON格式
   {1: {'memory': '78.1627', 'cpu': '0.0'}, 2: {'memory': '26.1982', 'cpu': '99.53'},.....,n: {'memory': '26.1982', 'cpu': '9.53'}}
3. 将JSON格式数据保存为文件，以供python tornado函数生成图形化表格
4. 读入原始文件路径 与 生成JSON格式文件路径 由参数传入
5. 使用mvn clean package 生成jar包
6. 生成jar包位置： target\DataGenerater-1.0.jar