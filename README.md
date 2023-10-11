# ArithCard

### 项目背景

大三协议设计原理，课程设计作业

Netty+JavaFX，暂无springboot，估计再迭代几个版本就有了

### 项目介绍

> 自定义了一套卡牌对战规则，规则如下：

- **牌型**
  
  数字牌：`1`、`2`、`3`…`13`，每张牌各**六张**

运算牌：`+`、`-`**六张**，`*`、`/`、`%`**四张**，`&`、`|`、`^`三张，`<<`、`>>`**两张**

- **玩法**
  
  凑24点：将手牌分为数字牌(1-13)，和运算牌，利用现有牌进行运算获得新的点数(超过24自动取余)，当得到24点时便可以**攻击/防御。**
  
  pvp：回合制游戏，每轮结束后对玩家状态进行运算，决定玩家扣除的血量；另外设置伤害制造器，固定回合对玩家造成部分伤害；直至一方玩家血量为0，游戏结束。

> 自定义协议

![image](https://github.com/LuoZero-World/ArithCard/assets/99077678/ef2f01ea-261a-4120-91c4-cbbe9332d1ff)

还有勉勉强强的卡牌UI，在`src/main/img`路径下就可以看到

### 部署

**服务端**

先把服务端拉起来：启动类路径：`com.llq.server.CardServer`

默认服务器IP：127.0.0.1	端口18000

然后拉客户端

**客户端**

启动类路径：`com.llq.client.CardClientStartUp`

目前**只能登陆**，不能注册，其中账号信息以`Map`形式存储于内存中，其在项目中的位置`com.llq.server.service.UserService`

客户端登录，进入游戏大厅：可创建对局，或加入现有对局(用JavaFX写的界面就这么样了，美化界面太搞了...orz)

<img width="448" alt="1697032199588" src="https://github.com/LuoZero-World/ArithCard/assets/99077678/937f4147-bdfe-409e-8904-6380eaec4682"/>

<img width="449" alt="1697032220669" src="https://github.com/LuoZero-World/ArithCard/assets/99077678/84e77a99-8193-4968-a871-4c48893934bf"/>


### 联系
QQ：1276602269

