#服务端代码  
Project Repository 项目迭代
---

作者：Public  
时间：2019-06-17  
说明：除ide自身插件外可使用[Typora](https://www.typora.io/)或更多第三方工具编辑md文件  
语法：  
　　　参考官方 [GitHub Flavored Markdown Spec](https://github.github.com/gfm/)说明  
　　　或国内社区说明[Markdown编辑器语法指南](https://segmentfault.com/markdown/)及第三方说明  


## Branches 分支

Protocol所有分支均需在master分支的[readme](/readme.md)文件中说明用途及必要信息  
  
1. name     :名称  
2. address  :路径  
3. version  :版本  
4. date     :日期  
5. author   :作者  
6. function :用途  
7. describe :说明  


| name |      address     | version |    date    | author | function | describe | 
| ---- | ---------------- | ------- | ---------- |------- | -------- | -------- |
| master | origin/master  |  1.0.0  | 2019/05/25 | 赵李洋 | 主分支   | 初始分支及主分支 |
| master | origin/develop |  1.0.0  | 2019/05/25 | 赵李洋 | 开发分支 | 开发分支        |



## Packages 分包说明

当前文档内容为所有分包说明  
  
1. name  : 名称  
2. author: 作者  
3. function : 功用  
4. describe : 备注  
  

| name | author | function | describe |
| ------------ | ---------- | ------ | ------ |
| conf            | public |  配置集  | 自用配置建议置于工程外不参与vcs |
| game-all        | public |  启动包  | Startup->调用server包中的GameBootstrap启动|
| game-api        | public | 对外接口 | 平台充值的sdk,php后台中需要的玩家数据和对游戏服的操作（例如：发送邮件，发送公告等） |
| game-basic      | public | 基础工具 | 基础的常量、工具定义 |
| game-center     | public | 中心服务 | 礼包码的处理,邀请服务等 |
| game-cfg        | public | 配置msg | 配置和msg相关的处理 |
| game-cross      | public | 跨服相关 | 跨服的玩法业务 |
| game-merge      | public | 合服脚本 | 合服逻辑处理 |
| game-pay        | public | 充值相关 | 充值总入口，并根据渠道id和表中数据进行转发到对应api接口 |
| game-pay-convert| public | 发货转换 | 特殊渠道使用，对于不方便获取sid的充值请求进行二次转发 |
| game-robot      | public | 压测相关 | 机器人，用于压测 |
| game-scene      | public | 场景相关 | 场景相关的业务处理 |
| game-script     | public | 功能脚本 | 功能性的业务逻辑，可热更 |
| game-server     | public | 系统功能 | 系统功能实现，实体类、监听类、计算器类等定义  |
| message-util    | public | 消息工具 | 生成消息和handler |


## Special 注意事项 
   1. BagManager中不要随便加新方法,要先征得同意
   2. protoBuf对数组支持有问题, 实体类中不要用int[]
   3. @Tag一定不能重复,不然会出重大错误
   4. 所有pojo类必须遵行:如果有 有参数构造函数, 则得手动加一个无参的构造函数。不然会有空指针慢常
   5. int[] byte[] long[] short[] double[] float[] ....  基本类型可以用数组  初始化为0 String 不支持
   6. List不会保存中间的数据,序列化再反序列化之后null会被清除.顺序会变
   例如
   list.add(obj)
   list.add(null)
   list.add(obj)
   
   7. 变量就直接放player身上,下线的时候统一从player身上拷一份到role身上入库
   8. 如果把技能或者经验之类的东西,要判断ItemChange是否改变,没改变过的消息不需要同步给客户端
   9. 实体内不能用内部类,不然会引死循环造成内存溢出

#### Setting 设置
1.idea的统一设置:File→Settings→Editor→Code style→Java，再点击右边的Imports,
  把Class count to use import with '*'和Names count to use import with '*'的数量,
  都设置为99.  
2.自用conf包中配置建议存储于项目工程文件夹外不参与VCS.  

3.idea统一安装插件:Alibaba Java Coding Guidelines.
选择 File - Settings - Plugins - Browse repositories 后，
输入 alibaba 选中 Alibaba Java Coding Guidelines，点击 Install.


#### Log 日志
1.文件[LogAction.java](game-basic/src/main/java/com/sh/game/constant/LogAction.java)中枚举id无需顺延上文，
  规则更新为以所属模块[group](#groups-)的 groupId乘一千再顺延做id
  
  
  
#各种消息池

##消息注册相关接口

* MessagePool
* HandlerPool
* [MsgAndHandlerPool](game-basic/src/main/java/com/sh/game/server/MsgAndHandlerPool.java)

##消息池实现类 MapMessagePool

MapMessagePool注册需要放进地图队列执行的请求消息，该pool主要用于两个地方：

1. 游戏服的[GameMessagePool](game-server/src/main/java/com/sh/game/server/GameMessagePool.java)
包含MapMessagePool，以实现本地地图模式


    public class GameMessagePool extends MsgAndHandlerPool {
        public GameMessagePool() {
            //合并Map的消息
            register(new MapMessagePool());
            //其他消息
        }
    }

2. 地图服务器的[MapTransformMessagePool](game-scene/src/main/java/com/sh/game/map/standlone/server/MapTransformMessagePool.java)
包含MapMessagePool，以实现游戏服到地图的消息转发解码


    public class MapTransformMessagePool extends MsgAndHandlerPool {
        public MapTransformMessagePool() {
            register(new MapMessagePool());
            register(new ResChatMessage(), null);
            register(new ResAnnounceMessage(), null);
        }
    }
  
##游戏服相关消息 

1.[GameMessagePool](game-server/src/main/java/com/sh/game/server/GameMessagePool.java)
---
GameMessagePool注册所有逻辑服务器需要处理的请求消息，逻辑服启动的时候需要需要实例化该Pool，传递给Netty解码器

* 本地地图模式需要包含MapMessagePool
* 所有需要地图队列执行的消息不能直接注册到该Pool，只能通过MapMessagePool引入

2.[BackMessagePool](game-server/src/main/java/com/sh/game/back/BackMessagePool.java)
---
BackMessagePool注册和后台相关的请求，包括充值回调、运营后台、关服、刷脚本、刷配置等请求
BackServer启动的时候需要实例化该Pool

##地图服务器相关消息

1.[MapServerMessagePool](game-scene/src/main/java/com/sh/game/map/standlone/server/MapServerMessagePool.java)
---
MapServerMessagePool注册地图服务器处理的消息

* 逻辑服和游戏的RPC通信，包括Notice和Message的transform
* 逻辑服和游戏服的数据交换，包括玩家信息同步、一些玩法的数据同步

2.[MapTransformMessagePool](game-scene/src/main/java/com/sh/game/map/standlone/server/MapTransformMessagePool.java)
---
MapTransformMessagePool注册逻辑服转发到地图服的消息

* 包含MapMessagePool，因为玩家请求地图服服务器的消息都是通过逻辑服转发的
* 包含一些特别特殊的消息，比如说聊天和公告

3.[RemoteClientMessagePool](game-scene/src/main/java/com/sh/game/map/standlone/client/RemoteClientMessagePool.java)
---
RemoteClientMessagePool注册地图服的客户端（主要指逻辑服）接受到地图服服务器端发过来的消息

* 和MapServerMessagePool配套
* 逻辑服和游戏的RPC通信，包括Notice和Message的transform
* 逻辑服和游戏服的数据交换，包括玩家信息同步、一些玩法的数据同步

4.[RemoteTransformMessagePool](game-server/src/main/java/com/sh/game/remote/RemoteTransformMessagePool.java)
---
RemoteTransformMessagePool注册地图服需要发给玩家的消息，因为地图服本身不和玩家直连，需要通过逻辑服转发给玩家

* 该Pool用于地图服的连接客户端换
* 该Pool只需要注册Message，不需要Handler（因为不会处理，纯转发）
* 该Pool也被Cross那边使用

##独立的跨服相关消息（地图服务器的翻版，如果没有独立跨服，可以没有这些）

1.[CrossMessagePool](game-cross/src/main/java/com/sh/game/cross/server/CrossMessagePool.java)
---
CrossMessagePool包含MapServerMessagePool

* 用于特殊的地图服务器消息注册，
* CrossServer启动的时候将会实例化该Pool
    
2.[CrossClientMessagePool](game-server/src/main/java/com/sh/game/system/cross/client/CrossClientMessagePool.java)
---
类似RemoteClientMessagePool，只是转为某种地图服务期写的客户端,

* 和CrossMessagePool配套
* 该消息池可能包括特殊玩法，而RemoteClientMessagePool是通用的地图服
* 如果跨服玩法多样性，那么类似这样的pool会很多，因为有不同的地图服
    
3.[CrossTransformMessagePool](game-cross/src/main/java/com/sh/game/cross/server/CrossTransformMessagePool.java)
---
* 类似MapTransformMessagePool
* 为了减少重复代码，此Pool直接包含类似MapTransformMessagePool

##Cross消息池总结

* Cross开头的消息，其实都是复刻的Remote*Message，因为相当于自定义了一个地图服务器，没有用默认地图服务器
* Cross这边没有RemoteTransformMessagePool，Cross这边直接复用的地图服客户端那边的消息池