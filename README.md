# CoBuf
Co Buffer 编写自己的数据交换格式

#### 写在开头
首先并不是为了重复造轮子，我希望大家可以从这个项目中学习到为什么在rpc/socket数据传输是需要进行数据序列化。
如何做到压缩数据大小的？为什么数据包会比json/xml等等序列化更快更小？

#### 从简开始
* [why CoBuf?](doc/why.md)
* [标记tag字节优化](doc/why-tag-opt.md)
* [引入结构体类型](doc/struct.md)
* [引入Bool类型](doc/bool.md)
* [引入List与Map类型](../doc/list-map.md)

#### 数据类型
类型|值|字节|php|java
---|:---:|:---:|:---:|:---:|
int|1|4|int|int
long|2|8|int|long
string|3|len|string|string
struct|4|len|obj|obj
list|5|len|array|list
bool|6|1|bool|bool
map|7|len|array|map

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