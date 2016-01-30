# 1.JPlugin基本介绍

软件架构的本质在于模块拆分，这些经过拆分的模块经过某种契约协同满足应用软件的对外需求。在当今，软件的需求都是迭代产生的，易变的。所以，对软件本身来说，如何最大限度的应对变化，“预见未来”，甚至“随需应变”是对架构设计的极致目标。  

那么，什么样子的架构才能应对未来尽可能多的可能性呢？在客户端开发工具层面，Eclipse应该是业界最成功的案例。Eclipse最早提供的功能就是一个Java的IDE，但是基于Eclipse，可以扩展出几乎任何桌面式GUI应用；并且既有的Java开发环境也可以进行无限想象的功能扩展。Eclipse之所以能够支持如此“多样性的未来”，是因为Eclipse采用了插件式框架，以及OSGI类加载机制。

JPlugin借鉴Eclipse设计思想，引入到服务端（Server Side），希望能够做到服务端的Eclipse。JPlugin引入插件的思想，插件、扩展点、扩展等基本概念都有实现，插件的生命周期有完整的实现。插件式架构的核心就是，在可能变化的地方定义扩展点，让所有产生变化的地方都扩展来实现；同时，用插件在组织应用软件的模块，最大限度实现松耦合和依赖倒置；还有，由于可以在扩展点方便地管理所有扩展，这对系统的监控以及开发高性能的应用都提供了很多方便。

使用JPlugin，对于应对复杂的应用以及应用未来的变化应该说是非常好的选择；同时，对于一次性的或者较简单的应用来说，JPlugin也提供了现成的核心插件可供使用。这些基本插件提供了诸如MVC、交易、日志、数据持久化（提供了Mybatis和Hiberinate的集成器）、缓存、调度等功能，可以方便地快速开发应用。

#[2.Maven工程中如何引用JPlugin](https://github.com/sunlet/jplugin/wiki/Maven%E5%B7%A5%E7%A8%8B%E4%B8%AD%E5%A6%82%E4%BD%95%E5%BC%95%E7%94%A8JPlugin)

#[3.相关配置文件](https://github.com/sunlet/jplugin/wiki/%E7%9B%B8%E5%85%B3%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)

#[4.Plugin与Plugin的注册](https://github.com/sunlet/jplugin/wiki/Plugin%E4%B8%8EPlugin%E7%9A%84%E6%B3%A8%E5%86%8C)

#[5.Web控制器](https://github.com/sunlet/jplugin/wiki/Web%E6%8E%A7%E5%88%B6%E5%99%A8)

#[6.开发和使用服务](https://github.com/sunlet/jplugin/wiki/%E5%BC%80%E5%8F%91%E5%92%8C%E4%BD%BF%E7%94%A8%E6%9C%8D%E5%8A%A1)

#[7.开发和使用业务规则服务](https://github.com/sunlet/jplugin/wiki/%E5%BC%80%E5%8F%91%E5%92%8C%E4%BD%BF%E7%94%A8%E4%B8%9A%E5%8A%A1%E8%A7%84%E5%88%99%E6%9C%8D%E5%8A%A1)

#[8.使用日志](https://github.com/sunlet/jplugin/wiki/%E4%BD%BF%E7%94%A8%E6%97%A5%E5%BF%97)

#[9.使用独立日志文件](https://github.com/sunlet/jplugin/wiki/%E4%BD%BF%E7%94%A8%E7%8B%AC%E7%AB%8B%E6%97%A5%E5%BF%97%E6%96%87%E4%BB%B6)

#[10.使用Mybatis](https://github.com/sunlet/jplugin/wiki/%E4%BD%BF%E7%94%A8Mybatis)

#11.使用Hiberinate(暂无文档）

#[12.发布和使用Restful服务](https://github.com/sunlet/jplugin/wiki/%E5%8F%91%E5%B8%83%E5%92%8C%E4%BD%BF%E7%94%A8Restful%E6%9C%8D%E5%8A%A1)

#[13.发布和使用远程服务](https://github.com/sunlet/jplugin/wiki/%E5%8F%91%E5%B8%83%E5%92%8C%E4%BD%BF%E7%94%A8%E8%BF%9C%E7%A8%8B%E6%9C%8D%E5%8A%A1)

#[14.使用HTTP请求过滤器](https://github.com/sunlet/jplugin/wiki/%E4%BD%BF%E7%94%A8HTTP%E8%AF%B7%E6%B1%82%E8%BF%87%E6%BB%A4%E5%99%A8)
