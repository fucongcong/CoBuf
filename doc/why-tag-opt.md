#### 标记tag字节优化 
在上一章中，我们使用tag标记替代我们的参数名。如果看过我们的实现代码，会发现我们是用1个字节存放这个tag标记的。
但是真正在实际运用中，我们一个结构体中的参数往往不会很多，所以我们因此进行进一步优化。

#### 优化大小
在tag小于15时，我们其实可以用4个byte存放就够了，另外4个byte可以存放type。因为我们的type类型有限，目前只有3种而已。
当tag大于等于15时，我们在额外申请1字节去存放tag标记就行了，这样的情况应该不多~ 

#### 如何实现?
比如某个参数现在type类型为3，tag标记为1。
- 正常的2字节存放应该表示为 0000 0011 0000 0001 
- 如果用1个字节存放，我们将存放type的1个字节进行向左位移4位变为 0011 0000，然后和tag的1个字节进行合并，最终变成 0011 0001 

当tag标记大于等于15时，比如某个参数现在type类型为3，tag标记为16。
- 正常的2字节存放应该表示为 0000 0011 0001 0000 
- 如果用1个字节存放，我们将存放type的1个字节进行向左位移4位变为 0011 0000，但是tag因为大于等于15，我们将第一个字节的后面4个byte存放tag标记为15，变成 0011 1111
然后在申请1个字节，存放真正的标记位 0001 0000.最后变成 0011 1111 0001 0000 

##### 不难发现当tag小于15时，我们节省了1个字节来存放1个参数的tag和type。只有当tag标记大于等于15时，才申请2个字节。

#### 解包
我们如果拿到第一个字节0011 0001
- 先解析type，将前4位进行向右位移4位，得到 0000 0011转成十进制为3.即type类型为3。
- 解析后4位，用0011 0001 减去0011 0000 即可得到后四位 0000 0001，即tag标记为1。 

我们如果拿到第一个字节0011 1111
- 先解析type，将前4位进行向右位移4位，得到 0000 0011转成十进制为3.即type类型为3。
- 解析后4位，用0011 1111 减去0011 0000 即可得到后四位 0000 1111，即tag标记为15。说明此tag标记肯定大于等于15，继续向后读取1个字节。后面1个字节才是真正的tag标记位。 

#### 优化后的代码实现
- [PHP](../demo/php/demo2.php)
- [JAVA](../demo/java/src/main/java/struct)

#### 数据展示

```
1000 0000 0a21 0000 0000 0000 0457 3200
0000 0463 6f63 6f3f 1000 0000 0a61 6161
6161 6161 6161 61

10 => 0001 0000 => type + tag
00 00 00 0a => 10 
21 => 0010 0001 type + tag
00 00 00 00 00 00 04 57 => 1111 
32 => 0011 0010 type + tag
00 00 00 04 => 4 len
63 6f 63 6f => coco 
3f => 0011 1111   type + tag
10 => 1000 0000 实际的tag
00 00 00 0a => 10  len
61 => a
```

#### 更进一步，丰富类型，进入下一篇
- [引入结构体类型](../doc/struct.md)

