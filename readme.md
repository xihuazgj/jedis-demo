# redis



Redis:通用命令：

- 通用指令是部分数据类型的，都可以使用的指令，常见的有：
- KEYS:查看符合模板的所有key,**不建议在生产环境设备上使用**
- DEL:删除一个指定的key
- EXISTS:判断key是否存在
- EXPIRE：给一个key设置有效期，有效期到期时该key会被自动删除
- TTL:查看一个KEY的剩余有效期
- 通过help[commandl可以查看一个命令的具体用法



#### String类型

String类型，也就是字符串类型，是Redis中最简单的存储类型。
其value是字符串，不过根据字符串的格式不同，又可以分为3类：
●string:普通字符串
●int:整数类型，可以做自增、自减操作
●float:浮点类型，可以做自增、自减操作
不管是哪种格式，底层都是字节数组形式存储，只不过是编码方式不同。字符串类型的最大空间不能超过512m.
KEY   	VALUE
msg  	hello world
num		10
score             92.5



**String类型的常见命令**

- SET:添加或者修改已经存在的一个String类型的键值对
- GET:根据key获取String类型的value
- MSET:批量添加多个String类型的键值对
- MGET:根据多个key获取多个String类型的value
- INCR:让一个整型的key自增1
- INCRBY:让一个整型的key自增并指定步长，例如：incrby num2让num值自增2
- INCRBYFLOAT:让一个浮点类型的数字自增并指定步长
- **SETNX:添加一个String类型的键值对，前提是这个key不存在，否则不执行**(可用作分布式锁)
- **SETEX:添加一个String类型的键值对，并且指定有效期**





Redis没有类似MySQL中的Table的概念，我们该如何区分不同类型的key呢？
例如，
需要存储用户、商品信息到redis,有一个用户
id是1，有一个商品id恰好也是1

**redis的方案**

key的结构
Redis的key允许有多个单词形成层级结构，多个单词之间用'：'隔开，格式如下：
项目名：业务名：类型：id
这个格式并非固定，也可以根据自己的需求来删除或添加词条。
例如我们的项目名称叫heima,有user和producti两种不同类型的数据，我们可以这样定义key:
◆user相关的key:
heima:user:1
product相关的key:
heima:product:1



如果Value是一个ava对象，例如一个User对象，则可以将对象序列化为SON字符串后存储：
KEY						VALUE
heima:user:1		{"name":"Jack","id":1,"age":21}
heima:product:1	["id":1,"name":"小米11"，"price":4999}





#### Hash类型

Hash类型，也叫散列，其value是一个无序字典，类似于引ava中的HashMap结构。
String结构是将对象序列化为S0N字符串后存储，当需要修改对象某个字段时很不方便：
KEY
VALUE
heima:user:1
{name:"Jack",age:21]
heima:user:2
{"name":"Rose","age":18}
Hash结构可以将对象中的每个字段独立存储，可以针对单个字段做CRUD:
					 VALUE
KEY			  field 	value

heima:user:1	name	 Jack

Hash类型的常见命令
Hash的常见命令有：

- HSET key field value:添加或者修改hash类型key的field的值
- HGET key field:获取一个hash类型key的field的值
- HMSET:批量添加多个hash类型key的field的值
- HMGET:批量获取多个hash类型key的field的值
- HGETALL:获取一个hash类型的key中的所有的field和value
- HKEYS:获取一个hash类型的key中的所有的field
- HVALS:获取一个hash类型的key中的所有的value
- HINCRBY:让一个hash类型key的字段值自增并指定步长
- HSETNX:添加一个hash类型的key的field值，前提是这个field.不存在，否则不执行



#### List类型

Redis中的List类型与java中的LinkedList类似，可以看做是一个双向链表结构。既可以支持正向检索和也可以支持反向
检索。
特征也与LinkedList类似：

- 有序
- 元素可以重复
- 插入和删除快
- 查询速度一般

常用来存储一个有序数据，例如：朋友圈点赞列表，评论列表等。



- List类型的常见命令
  List的常见命令有：
- PUSH key element..:向列表左侧插入一个或多个元素
- LPOP key:移除并返回列表左侧的第一个元素，没有则返回nil
- RPUSH key element..:向列表右侧插入一个或多个元素
- RPOP key:移除并返回列表右侧的第一个元素
- LRANGE key star end:返回一段角标范围内的所有元素
- BLPOP和BRPOP:与LPOP和RPOP类似，只不过在没有元素时等待指定时间，而不是直接返回nil



如何利用List结构模拟一个栈？
·入口和出口在同一边：LPUSH进LPOP出
如何利用Lst结构模拟一个队列？
·入口和出口在不同边：LPUSH进RPOP出
如何利用Lst结构模拟一个阻塞队列？
·入口和出口在不同边
出队时采用BLPOP或BRPOP

