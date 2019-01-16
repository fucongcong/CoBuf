# CoBuf
Co Buffer 编写自己的数据交换格式

#### 写在开头
首先并不是为了重复造轮子，我希望大家可以从这个项目中学习到为什么在rpc/socket数据传输是需要进行数据序列化。
如何做到压缩数据大小的？为什么数据包会比json/xml等等序列化更快更小？

#### 从简开始
* [why CoBuf?](doc/why.md)
* [标记tag字节优化](doc/why-tag-opt.md)
* [引入结构体类型](doc/struct.md)

#### 数据类型
类型|值
---|:---:|
int|1|
long|2|
string|3|
struct|4|
list|5|
bool|6|
map|7|

#### 示例
```
struct Members
{
    0 need list<string> names;
    1 need map<string, string> sexs;
    2 need optional work;
}

struct Company
{
    0 need int id;
    1 need long code;
    2 need string name;
    16 optional string addr;
    4 optional Members members;
    5 optional list<Members> memberLists;
    6 optional map<string, Members> memberMaps;
}

struct User
{
    0 need int id;
    1 optional Company company;
}
```

#### 各语言使用
* [PHP](demo/php/example.php)