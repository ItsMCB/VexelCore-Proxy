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

When creating a pull request, we ask that you follow the relevant template (if applicable).

By contributing to VexelCore, you agree to license your code under the [GNU General Public License version 3](https://github.com/Vexelosity/VexelCore-Proxy/blob/main/LICENSE).

### Developer Guide - WIP
VCP is modular by design. We allow users to disable all features they don't want and customize the majority of plugin messages.

### Creating a New Feature
New commands go under `me.itsmcb.vexelcoreproxy.commands`.

#### Configuration
We use [SpongePowered's Configurate](https://github.com/SpongePowered/Configurate) library to handle our configuration files.

All features must have a configuration section in `config.yml`. Each configuration section gets a class where default values can be set. Check out the contents of `me.itsmcb.config` to see how it's done.

#### New Feature Checklist
- [x] Create a configuration section for the feature in `config.yml`.
- [x] Add feature to the `loadFeatures()` method. It is preferably added to the handler.
- [x] Add feature to the `unloadFeatures()` method if it's not added to the handler.
- [x] Create feature documentation.