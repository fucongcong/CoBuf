##### 继续在上篇的结构中加入Bool类型

```
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
    2 optional bool working;
}

struct Members
{
    0 need list<string> names;
    1 need map<string, string> sexs;
    2 need optional bool work;
}

```
#### 代码实现
- [PHP](../demo/php/example.php)
- [JAVA](../demo/java/src/main/java/maplist/MapListDemo.java)

