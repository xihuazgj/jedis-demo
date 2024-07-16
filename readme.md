# redis



## 基础命令及数据结构

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



#### Set类型

Redis的Set结构与Java中的HashSet类似，可以看做是一个value为nul1的HashMap。因为也是一个hash表，因此具备与
HashSet类似的特征：

- 无序
- 元素不可重复
- 查找快
- 支持交集、并集、差集等功能

Set类型的常见命令

SADD key member.,:向set中添加一个或多个元素

SREM key member..:移除set中的指定元素

SCARD key:返回set中元素的个数

SISMEMBER key member:判断一个元素是否存在于set中

SMEMBERS:获取set中的所有元素

SINTER key1key2...:求keyl与key2的交集

SDIFF key1key2..:求keyl与key2的差集

SUNION key1 key2..:求key1和key2的并集



#### SortedSet类型

Redis的SortedSet:是一个可排序的set集合，与Java中的TreeSet有些类似，但底层数据结构却差别很大。SortedSet中
的每一个元素都带有一个score属性，可以基于scorel属性对元素排序，底层的实现是一个跳表(SkipList)加hash表
SortedSet具备下列特性：
可排序
元素不重复
查询速度快
因为SortedSet的可排序特性，经常被用来实现排行榜这样的功能。



SortedSet类型的常见命令

- SortedSet的常见命令有：
- ZADD key score member:添加一个或多个元素到sorted set,如果已经存在则更新其score值
- ZREM key member:册别除sorted set中的一个指定元素
- ZSC0 RE key member:获取sorted set中的指定元素的score值
- ZRANK key member:获取sorted set中的指定元素的排名
- ZCARD key:获取sorted set中的元素个数
- ZCOUNT key min max:统计score值在给定范围内的所有元素的个数
- ZINCRBY key increment member:让sorted set中的指定元素自增，步长为指定的increment值
- ZRANGE key min max:按照score排序后，获取指定排名范围内的元素
- ZRANGEBYSCORE key min max:按照score?排序后，获取指定score范围内的元素
- ZDIFF、ZINTER、ZUNION:求差集、交集、并集
- 注意：所有的排名默认都是升序，如果要降序则在命令的Z后面添加EV即可



## Jedis与springdataredis



### jedis的使用



jedis其实是线程不安全的，所以我们在使用时通常与连接池一起使用



1.引入依赖

```
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>3.7.0</version>
</dependency>
```

2.创建jedis连接池工厂

```
public class JedisConnectionFactory {

    private static final JedisPool jedisPool;

    static {
        //配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8); //连接池允许的jedis的最大连接数
        poolConfig.setMaxIdle(8);//连接池允许的jedis的最大空连接数，不大于setMaxTotal
        poolConfig.setMinIdle(8);//空连接的可用数
        poolConfig.setMaxWaitMillis(1000);//连接请求的最大等待时间
        //创建连接池对象
        jedisPool = new JedisPool(poolConfig,
                "192.168.32.131",6379,1000);

    }
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
}
```

3.测试jedis的使用

```
public class JedisTest {

    private Jedis jedis;

    @BeforeEach
    void setUp(){
        //1.建立链接
//        jedis = new Jedis("192.168.32.131",6379);
        jedis = JedisConnectionFactory.getJedis();
        //2.选择库
        jedis.select(0);
    }

    @Test
    void testString(){

        //1.存入数据
        String result = jedis.set("name","guojunzhang");
        System.out.println("result = "+ result);
        //2.获取数据
        String name = jedis.get("name");
        System.out.println("name = " + name);
    }
    @Test
    void testHash(){
        //1.插入hash数据
        jedis.hset("user:1","name","Jack");
        jedis.hset("user:1","age","21");
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name","guojunzhang");
        hashMap.put("age","22");
        hashMap.put("address","四川内江");
        jedis.hmset("usermap:1",hashMap);
        //2.获取hash数据
        Map<String,String> map = jedis.hgetAll("user:1");
        System.out.println(map);
        System.out.println("-------------------------");
        Map<String,String> hm = jedis.hgetAll("usermap:1");
        System.out.println(hm);
        List<String> list = hm.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.toList());
        System.out.println(list);
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }
    @AfterEach
    void testDown(){
        if (jedis != null){
            jedis.close();
        }
    }
}
```

























