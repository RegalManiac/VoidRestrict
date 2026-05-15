# VoidRestrict

A lightweight plugin for **Folia (1.21.11)** servers that completely blocks malicious player actions below bedrock level. Perfect for anarchy and survival servers to prevent cheaters from exploiting height limits.

---

## Features

* **Protection:** Blocks breaking/placing blocks, pouring liquids, and placing exploit-prone objects (end crystals, beds), even if a cheater attempts to reach upper blocks while standing deep in the void.
* **Void PvP Prevention:** Disables all types of combat (melee, bows, tridents, splash/lingering potions) if either the attacker or the victim is below bedrock.
* **Vanilla-Friendly:** Players in the void can still safely use fireworks, fly with Elytra, eat food, and drink potions.
* **Real-time Updates:** Any changes made via commands or the configuration file apply instantly without requiring a server restart.

---

## Commands & Permissions

**Main Command:** `/voidrestrict` (Alias: `/vr`)  
**Permission:** `voidrestrict.admin`

* `/vr reload` — Reload the configuration from `config.yml`.
* `/vr toggle <place|break|attack>` — Instantly enable or disable a specific restriction.
* `/vr world <overworld|nether>` — Toggle the plugin's functionality for a specific world.

---
