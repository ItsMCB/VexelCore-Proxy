# VexelCore-Proxy
A modular and lightweight essential features plugin for Minecraft proxies.

NOTE: This is primarily designed to fit the needs of the SouthHollow network.

# Current Features
We have quite a few!

## Broadcast
Send chat, title and action bar messages to the whole network or specific servers.
![](https://i.imgur.com/qANjVrO.png)

## Player Information
Get detailed information about a player's connection.
![](https://i.imgur.com/GwwL9vJ.png)
![](https://i.imgur.com/J8HcQtK.png)
![](https://i.imgur.com/XMvHyzc.png)

## Better Global List
Provides detailed information about the servers connected to your proxy.
![Minecraft Screenshot](https://i.imgur.com/5wnBRzJ.png)

## Custom Commands
Easily create commands that send messages or are aliases.

#### Discord Command Example
```toml
{ type = "message", newCommand = "discord", components = [ { content = "&7Click to join our &3Discord", hover = "&eClick if you're a nerd!", action = "OPEN_URL", actionValue = "https://discord.gg/V4ukMbe" } ] },
```


[![YT Video Thumbnail](https://img.youtube.com/vi/jknCGCBA-rw/sddefault.jpg)](https://www.youtube.com/watch?v=jknCGCBA-rw)


## Jump
Not sure where a player is? Use jump to be sent to the current server they're on!
![](https://i.imgur.com/Gw1gFbE.png)
![](https://i.imgur.com/6ftY29S.png)

## It's Super Customizable!
- Almost all messages can be changed from the config.toml
- All features can be disabled

### bStats
Plugin statistics

![bStats](https://bstats.org/signatures/velocity/VexelCoreProxy.svg)