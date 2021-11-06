## Contributing to VexelCoreProxy

If you're interested in helping improve VCP, here are several ways you can contribute to the project:

#### Translating
If you speak a language besides English, we'd appreciate your help in translating our [language](https://github.com/ItsMCB/VexelCore-Proxy/tree/main/src/main/java/me/itsmcb/vexelcoreproxy/config/language) files.

#### Developing
This project is mainly worked on by non-professionals for fun. Code improvements, bug fixes, and new features pull requests are appreciated!

#### Helping
If you're an experienced user of VexelCoreProxy and see someone who needs help (such as on our Discord), feel free to jump in and help.

--- 
### Submitting a PR

When creating a pull request, we ask that you follow any relevant templates (if applicable / available at the time of submission).

By contributing to VexelCore, you agree to license your code under the [GNU General Public License version 3](https://github.com/Vexelosity/VexelCore-Proxy/blob/main/LICENSE).

--- 

# Developer Guide - WIP
Check back here every so often for the most up-to-date information!

### Creating a Command
Commands go under `me.itsmcb.vexelcoreproxy.commands`. We typically extend Velocity's `SimpleCommand` class instead of the other command options as it's very easy to work with. Example:

```java
package me.itsmcb.vexelcoreproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.TabUtils;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.List;

public class FeatureName implements SimpleCommand {

    private final ProxyServer server;
    private final CommentedConfigurationNode config;
    private final CommentedConfigurationNode language;

    public FeatureName(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig().get();
        this.language = VCP.getLang().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.node("feature-name").node("permission").getString())) {
            ChatUtils.sendMsg(source, language.node("general").node("prefix").getString(), language.node("error").node("noPermission").getString());
            return;
        }
        if (source instanceof Player player) {
                // Code goes here
                return;
            }
            ChatUtils.sendMsg(source, language.node("error").node("invalidUsage").getString(), "/jump <player name>");
        } else {
            // If console support is not added, send the `canOnlyBeExecutedByAPlayer` error message.
            ChatUtils.sendMsg(source, language.node("error").node("canOnlyBeExecutedByAPlayer").getString());
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        // Warning: Tab handeling is likely to be redone soon. For now, each List<String> you add corresponds with the amount of args. Ex. `/featurename *space*` will show the results of `TabUtils.getAllPlayers(server)`.
        return TabUtils.returnTab(invocation.arguments(), TabUtils.getAllPlayers(server));
    }
}
```

### Registering a Command
Inside `me.itsmcb.vexelcoreproxy.utils.ConfigUtils`, add your feature to the `loadFeatures()` method. Example:
`instance.getFeatureHandler().registerFeature(new VCPFeature(instance, "featurename", new FeatureName(instance)), true);`

The feature handler will automatically unload and reload the feature when needed, so don't add it to the `unloadFeatures()` method.

If the feature is unable to use the typical registeration system, add it to the `loadFeatures()` **and** `unloadFeatures()` methods, but in the needed way.

### Command Configuration Options
We use [SpongePowered's Configurate](https://github.com/SpongePowered/Configurate) library to handle our configuration files. To allow users to get the most out of VCP, we requre each command feature configuration to have an `enabled` boolean option, a permission node option, and at least one alias (ex. /glist). Here's an example of what that'd look like in the `config.yml`:
```yaml
featurename:
    enabled: true
    permission: vcp.featurename
    aliases:
    - featurename
    - optional-additional-alias
```

Inside `me.itsmcb.vexelcoreproxy.config.main`, create a new class file for the feature. Inside it, follow this template:
```java
package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class FeatureName {

    private Boolean enabled = true;
    private String permission = "vcp.featurename";
    private List<String> aliases = List.of("featurename");

}
```
When the configuration file is loaded, the default value(s) will be set if the node(s) don't already exist. 

Make sure to include `@ConfigSerializable` above the class for classes that deal with Configurate. If other configuration options are desired, simply add the applicable data type and default value.

Finally, add it to the `me.itsmcb.vexelcoreproxy.config.main.MainConfig` class.
```java

package me.itsmcb.vexelcoreproxy.config.main;

import me.itsmcb.vexelcoreproxy.config.main.cc.CustomCommand;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class MainConfig {

    private String language = "en_US";

    Main main;

    Glist glist;

    Jump jump;

    Broadcast broadcast;

    PlayerInformation playerInformation;

    HelpOp helpop;

    CustomCommand customCommands;

    FeatureName featureName;

}
```

### Language File
Works in the same was as configuration. The en_US file can be found in `me.itsmcb.vexelcoreproxy.config.language.en_US`.

### Lastly, create feature documentation where possible.
