# Cobblemon Detector

This is a Client-Side mod to get notified of desirable Pokemon near your player.

You will get a text-notification in your local chat plus a audible Notification.

You can change what Pokemon you want to get notified of by changing the config file in your minecraft folder `config/CobblemonDetector.json`.

The Notification will appear even if that Pokemon is owned by another player or you.
This information is Server-Side only 😢 .


# Config Examples

This is the default config file. It will be recreated when the file is not present.
```json
{
  "broadcastAllLegendaries": true,
  "broadcastAllMythics": true,
  "broadcastAllStarter": false,
  "broadcastAllowlist": [
    "Mew",
    "Mewtwo"
  ]
}
```

For when you _only_ want your favorite Electric-types.
```json
{
  "broadcastAllLegendaries": false,
  "broadcastAllMythics": false,
  "broadcastAllStarter": false,
  "broadcastAllowlist": [
    "Mareep",
    "Flaaffy",
    "Ampharos",
    "shinx",
    "luxio",
    "luxray",
  ]
}
```

To disable all notifications.
```json
{
  "broadcastAllLegendaries": false,
  "broadcastAllMythics": false,
  "broadcastAllStarter": false,
  "broadcastAllowlist": [
  ]
}
```
