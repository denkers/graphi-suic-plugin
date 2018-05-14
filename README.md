<img src="preview/Logo.png" align="left" />

# graphi-suic Plugin

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

graphi-suic is a research driven plugin for [Graphi](https://github.com/kyleruss/graphi)  
The plugin simulates a social-network with an agent-based model and measures self-evaluation among agents  
using a centrality-based measure to quantify interpersonal loss in a social relations change  
The plugin runs the model on large random networks and simulates death and relation disconnection through  
node deletion and rewiring methods then the plugin evaluates self-evaluation over a period of time to examine a persons 
drop in self-evaluation in reaction to social relation changes in the network  
The work was motivated by suicide behaviour research whereby we looked to observe to what extent suicide intention cascades in a network after a negative social change i.e. death/suicide or the disconnection of interpersonal ties



### [View Research Paper](https://dl.acm.org/citation.cfm?id=3091191)
Russell, K., Jiamou, L., Li, L. (2017). What Becomes of the Broken Hearted? An Agent-Based Approach to Self-Evaluation, Interpersonal Loss, and Suicide Ideation. _Proceedings of the 16th Conference on Autonomous Agents and MultiAgent Systems_, pp. 436-445

## Getting started

### Prerequisites
- JDK 1.8+ 
- Maven 3.3+
- [Graphi](https://github.com/kyleruss/graphi.git) up to version 1.7.4

### Installation
- Clone the graphi-suic-plugin repository
```
git clone https://github.com/kyleruss/graphi-suic-plugin.git
```

- Build the plugin 
```
mvn package
```

- Import the plugin in Graphi
From the menu select `Plugins` and open the plugin to your `graphi-suic-plugin.jar`

## License
graphi-suic-plugin is available under the MIT License  
See [LICENSE](LICENSE) for more details
