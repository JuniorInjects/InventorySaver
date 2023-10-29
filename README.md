# InventorySaver
InventorySaver is an Minestom Extension to save/load the inventory data of a player.

### Requirements
- [x] [Minestom](https://github.com/Minestom/Minestom)

### Usage
When a player joins your server, you want to set his old inventory using the `#deserializ` method. If the player had no inventory data befor, you can set him some items.
```java
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            if(!new InventorySave(event.getPlayer(), "FOLDER_NAME").deserializ()) {
                //set items if player had no inventory data.
            }
        });
```
When the player leaves the server, you only need to use the `#serialize` method.
```java
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            new InventorySave(event.getPlayer(), "FOLDER_NAME").serialize();
        });
```
You can also use the methods in other events or functions.
