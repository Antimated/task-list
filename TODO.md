# TODO's

## (Possible) tasks (all possible ideas that cross my mind and are feasible with built-in events)

- Tasks where a player dies: e.g. Die for a first time. `ActorDeath event`?
- Tasks where a player removes a friend: e.g. I don't like friends `RemoveFriend` event
- Check if a set of achievement diaries is completed? `client.getVarbitValue(Varbits.DIARY_KARAMJA_EASY)`
- Kill all barrows brothers? `client.getVarbitValue(Varbits.BARROWS_KILLED_AHRIM)`
- Get kingdom approval to 100%: `client.getVarbitValue(Varbits.KINGDOM_APPROVAL) == 127`
- Slayer points: `client.getVarbitValue(Varbits.SLAYER_POINTS) >= points`
- Slayer task streak: `client.getVarbitValue(Varbits.SLAYER_TASK_STREAK) >= streak`
- NMZ reward points: `client.getVarpValue(VarPlayer.NMZ_REWARD_POINTS) >= points`

## Unnamed vars
- VarBit 4371: Opening of a skill guide. Ranges from 1 to 23 (1 = Attack, 23 = Hunter)
- VarBit 4372: Clicking on a subsection in the skill guide. Ranges from 0 to length of subsections -1 (zero based)