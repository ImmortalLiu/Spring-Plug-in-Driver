# Spring-Plug-in-Driver #
构建基于spring容器的插件化编程驱动，主要是为了方便软件的迭代更新和减少开发人员重复性编码，实现插件复用

### 目前规划 ###
1.深入了解spring容器

2.阅读一些框架源码，储备技术

3.完善想法，搭建大致架构

![image](https://github.com/ImmortalLiu/Spring-Plug-in-Driver/blob/master/images/Plug-In-Driver.png)

#### 联系方式 ####
微信：18229747674

有兴趣的可以联系我啊

```flow
  st=>start: index
  op=>operation: 申请
  op2=>operation: 结果页
  op3=>operation: 查询本地
  i1=>inputoutput: bid入库
  i2=>inputoutput: 填写个人信息
  c1=>condition: 检查登录
  c2=>condition: 登录
  c3=>condition: 查询本地记录
  c4=>condition: 检测状态
  c5=>operation: 风控审核
  e=>end

  st->op->c1()
  c1(no)->c2(yes)->op()
  c1(yes)->c3(no)->i1(right)->i2(right)->c5()->op2->e
  c1(yes)->c3(yes)->c4(no)->i2
  c1(yes)->c3(yes)->c4(yes)->op3->op2
  c3()->e
```
