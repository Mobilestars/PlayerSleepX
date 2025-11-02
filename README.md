# 🌙 PlayerSleepX

> 🛏️ **Control how many players must sleep to skip the night!**
> A simple, configurable plugin for Minecraft servers that makes multiplayer sleeping fair and flexible.

---

## ✨ Features

* ⚙️ **Customizable sleep system** — Define how many players need to sleep to skip the night.
* 📊 **Two modes:**

  * **Percentage** – e.g. 50% of players must sleep
  * **Amount** – e.g. exactly 3 players must sleep
* 💬 **Broadcast messages** when players go to bed or when the night is skipped.
* 🔄 **Reload & configure in-game** via `/playersleep` command.
* 💡 **Lightweight & fast** — no dependencies, instant setup.

---

## 🧩 Commands

| Command                    | Description             | Permission            |                         |                       |
| -------------------------- | ----------------------- | --------------------- | ----------------------- | --------------------- |
| `/playersleep`             | Shows help and usage    | `playersleep.command` |                         |                       |
| `/playersleep reload`      | Reloads the config file | `playersleep.command` |                         |                       |
| `/playersleep set <enabled | mode                    | value> <newValue>`    | Changes plugin settings | `playersleep.command` |

### 🛠️ Examples

```bash
/playersleep set mode percentage
/playersleep set value 50
/playersleep reload
```

---

## ⚙️ Configuration (`config.yml`)

```yaml
enabled: true
mode: percentage  # or "amount"
value: 50
```

* **enabled** → Enables or disables the plugin
* **mode** → Choose between `percentage` or `amount`
* **value** → Defines the percentage or amount required

---

## 🪄 Permissions

```yaml
playersleep.command:
  description: Allows use of the /playersleep command
  default: op
```

---

## 🧰 Installation

1. Download the latest `.jar` from [Modrinth](https://modrinth.com/project/playersleep).
2. Place it in your server’s `plugins/` folder.
3. Restart or reload your server.
4. Adjust the config in `plugins/PlayerSleepX/config.yml` if needed.

---

## 📸 Example Messages

```
[Sleep] Alex went to bed (2/3)
[Sleep] Enough players are sleeping! Skipping the night...
```

---

## 👨‍💻 Developer Info

* **Main class:** `de.scholle.playersleep.PlayerSleep`
* **Command alias:** `/psleep`
* **Plugin prefix:** `SleepX`
* **Load:** `STARTUP`

---

## 📄 License

**Apache License 2.0**
Copyright © Mobilestars

You are free to use, modify, and distribute this plugin under the terms of the Apache 2.0 license.
[Read more →](https://www.apache.org/licenses/LICENSE-2.0)

---

## 🌐 Links

* 🔗 [Modrinth Page](https://modrinth.com/project/playersleep)
* 💬 [Issues & Suggestions](https://modrinth.com/project/playersleep/issues)

---

> 💛 *Made with care by Mobilestars — helping your players sleep better!* 😴
