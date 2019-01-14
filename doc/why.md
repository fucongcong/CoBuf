#### 笔者和大家一样，打算从最简的开始入手，我们就只定义3种数据类型
- int 
- string 
- long 

#### 然后我们定义一个简单的数据结构，格式如下

```
struct Company      //结构体的名称
{
    0 need int id;  
    1 need long code;
    2 need string name;
    3 optional string addr;
}
```
##### 先简单解释一下
- 前面的数字（表示这个参数在此结构的tag标记位）
- need和optional（need表示参数一定会有，optional表示此参数为可选）

#### 目前web主流的数据传输方式
- json
数据格式如下: 
```
{"id":10,"code":1111,"name":"coco","addr":"aaaaaaaaaa"}
```
- xml
数据格式如下:
```
<?xml version="1.0" encoding="UTF-8"?>
<o>
     <addr type="string">aaaaaaaaaa</addr>
     <code type="number">1111</code>
     <id type="number">10</id>
     <name type="string">coco</name>
</o>
```

缺点很明显，在数据量比较大时，这样的数据传输大小会很大，如果一个接口被频繁的调用而且需要传输的数据量又比较大时，
既消耗服务器带宽又增加的接口的响应速度,磁盘的io也会受到影响。

#### 优化的办法
很多大公司都会定义，编写自己内部的数据序列化格式，数据的组包协议。而它就是用来优化这个问题。下面就举个简单的序列化的方式，
来重新序列化id=10,code=1111,name='coco',addr='aaaaaaaaaa'的这段数据。


##### 首先定义数据类型
```
class CoType
{
    const CO_INT = 1;
    const CO_LONG = 2;
    const CO_STRING = 3;
}
```
##### 使用tag标记替代我们的参数名
```
//使用 tag+type+length+content 序列化后应该像这样
01210 1241111 234coco 3310aaaaaaaaaa
```

- 01210可以依次拆分为 0 1 2 10，tag为0的即参数名为id，数据类型为1的即int类型，内容长度为2的，实际内容为10.最终可以解析得到:id=10
- 1241111可以依次拆分为 1 2 4 1111，tag为1的即参数名为code，数据类型为2的即long类型，内容长度为4的，实际内容为1111.最终可以解析得到:code=1111

##### 我们通过tag标记，将复杂的参数名进行优化，使得整个数据结构的长度得到缩小。

#### 代码实现
- [PHP](../demo/php/demo1.php)

#### 更进一步，再次优化，进入下一篇
- [标记tag字节优化](../doc/why-tag-opt.md)
