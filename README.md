# DeviceIdGenerator
> Try to generate an unique id for android device, even when app was uninstalled, the id won't change.

- Using hardware information like brand, manufacturer etc.
- Hashed by MD5
- MemoryCache and DiskCache this id so when reinstall, the id will restore too

<pre>
  String did = DidHelper.getInstance(context).getDid();
</pre>
