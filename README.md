# 1.JPlugin基本介绍

软件架构的本质在于模块拆分，这些经过拆分的模块经过某种契约协同满足应用软件的对外需求。在当今，软件的需求都是迭代产生的，易变的。所以，对软件本身来说，如何最大限度的应对变化，“预见未来”，甚至“随需应变”是对架构设计的极致目标。  

那么，什么样子的架构才能应对未来尽可能多的可能性呢？在客户端开发工具层面，Eclipse应该是业界最成功的案例。Eclipse最早提供的功能就是一个Java的IDE，但是基于Eclipse，可以扩展出几乎任何桌面式GUI应用；并且既有的Java开发环境也可以进行无限想象的功能扩展。Eclipse之所以能够支持如此“多样性的未来”，最重要的特性是Eclipse的插件式框架。

JPlugin借鉴Eclipse设计思想，引入到服务端（Server Side），希望能够做到服务端的Eclipse。JPlugin引入插件的思想，插件、扩展点、扩展等基本概念都有实现，插件的生命周期有完整的实现。插件式架构的核心就是，在可能变化的地方定义扩展点，让所有产生变化的地方都扩展来实现；同时，用插件在组织应用软件的模块，最大限度实现松耦合和依赖倒置；还有，由于可以在扩展点方便地管理所有扩展，这对系统的监控以及开发高性能的应用都提供了很多方便。

使用JPlugin，对于应对复杂的应用以及应用未来的变化应该说是非常好的选择；同时，对于一次性的或者较简单的应用来说，JPlugin也提供了现成的核心插件可供使用。这些基本插件提供了诸如MVC、交易、日志、数据持久化（提供了Mybatis和Hiberinate的集成器）、缓存、调度等功能，可以方便地快速开发应用。


* AAA
* BBB
