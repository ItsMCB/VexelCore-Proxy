# About
**VexelCoreProxy** is a modular and lightweight essential features plugin for Minecraft proxies.  Our goal is to provide the necessary features networks need on their proxy software.

NOTE: This is primarily designed to fit the needs of the SouthHollow network.

# Current Features
We have quite a few!

## Broadcast
Send chat, title and action bar messages to the whole network or specific servers.

Usage: `/broadcast <title/actionbar/chat> <server name/all> <message>`
![](https://i.imgur.com/6U0ibKx.png)

## Player Information
Get detailed information about a player's connection.

Usage: `/playerinformation <online player name>`
![](https://i.imgur.com/GwwL9vJ.png)
![](https://i.imgur.com/J8HcQtK.png)
![](https://i.imgur.com/XMvHyzc.png)

## Better Global List
Provides detailed information about the servers connected to your proxy.

Usage: `/glist [-all]`
![Minecraft Screenshot](https://i.imgur.com/5wnBRzJ.png)

## Custom Commands
Easily create commands that send messages or are aliases.

Usage: Edit the configuration file. CC's can be previewed in-game with `/vcp cc list`

#### Discord Command Example
```yaml
custom-commands:
  enabled: true
  send-messages:
    -   new-command: discord
        components:
          -   content: '&7Click to join our &3Discord'
              hover: '&7Click me'
              action: 'OPEN_URL'
              action-value: 'https://discord.gg/V4ukMbe'
```

[![YT Video Thumbnail](https://img.youtube.com/vi/jknCGCBA-rw/sddefault.jpg)](https://www.youtube.com/watch?v=jknCGCBA-rw)

## Better HelpOp
Allows players to alert online staff that they need help.

Usage: `/helpop <message to staff>`
![Player View](https://i.imgur.com/PSys5Wk.png)
![Staff View](https://i.imgur.com/5mq0K54.png)

## Jump
Not sure where a player is? Use jump to be sent to the current server they're on!

Usage: `/jump <online player name>`
![](https://i.imgur.com/Gw1gFbE.png)
![](https://i.imgur.com/6ftY29S.png)

## It's Super Customizable!
- All features can be enabled or disabled in the main configuration file or in-game!
- Most messages can be changed in the language file!

### bStats
Plugin statistics

![bStats](https://bstats.org/signatures/velocity/VexelCoreProxy.svg)
