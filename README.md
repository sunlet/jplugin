# 1.JPlugin基本介绍

JPlugin是轻量级的应用框架，支持便捷地开发插件式的系统。插件式的架构是目前业界公认的最好的系统架构，可以应对未来的变化，并最大限度降低多个模块之间的耦合度。

JPlugin 2.5.X以后分为三部分：JPlugin-core, JPlugin-das , JPlugin-mvc

* JPlugin-core: 提供轻量级的插件插件式系统的核心机制。包括 kernel、service、log等少数几个核心插件。开发者可以单独基于JPlugin-core构造插件式系统。 
* JPlugin-das：提供与数据库访问相关的插件。jplugin-das基于jplugin-core，提供数据源管理能力，提供sqltemplate快速处理sql，也提供mybatis集成能力。
* JPlugin-mvc：提供MVC机制的Web开发机制以及Http服务的开发能力。jplugin-mvc基于jplugin-core，可以方便地开发WebController或者Web服务。

