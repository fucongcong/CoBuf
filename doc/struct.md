#### 在章节开始我们定义了一个简单的数据结构，格式如下

```
struct Company      //结构体的名称
{
    0 need int id;  
    1 need long code;
    2 need string name;
    3 optional string addr;
}
```

##### 但是在实际运用中，真正的数据类型肯定不止这么几种，我们这章就引入结构体的复用

```
    struct Company
    {
        0 need int id;
        1 need long code;
        2 need string name;
        3 optional string addr;
    }

    struct User
    {
        0 need int id;
        1 optional Company company;
    }
```

#### 结构体复用的编码原则其实与单个结构体编码一致，只是做了一层嵌套罢了。

#### 下一篇继续完善基础类型
- [引入Bool类型](../doc/bool.md)