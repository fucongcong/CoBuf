##### 继续在上篇的结构中加入Bool类型

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
        2 optional bool working;
    }
```

#### bool类型我们在传输中用0或1表示，用1个字节存储

#### 代码实现
- [JAVA](../demo/java/src/main/java/bool/BoolDemo.java)

#### 下一篇继续完善基础类型
- [引入List与Map类型](../doc/list-map.md)