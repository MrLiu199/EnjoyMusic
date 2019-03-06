# EnjoyMusic
一个比较简单的音乐播放器，具有播放/暂停、下一首、搜索本地歌曲、搜索和下载网络歌曲、添加到播放列表、从播放列表移除等基本功能。

该播放器主要参考了**波尼音乐**项目( https://github.com/wangchenyan/ponymusic )，该项目结构清晰，功能完善，非常适合学习，非常感谢作者。

### 下载
[下载地址](./assets/app-debug.apk) (最低版本：Android 4.2 Jelly Bean, API 17)

### APP主要截图
- 这里录了个小视频
<video src="/assets/enjoy_music_03-06-3.webm" controls="controls">
your browser does not support the video tag
</video>
<img src="/assets/Screenshot_1551875303.png" alt="screenshot" title="本地音乐播放" width="270" height="460" />  <img src="/assets/Screenshot_1551876634.png" alt="screenshot" title="本地音乐搜索" width="270" height="460" /> 
<img src="/assets/Screenshot_1551876653.png" alt="screenshot" title="播放列表" width="270" height="460" />  <img src="/assets/Screenshot_1551876679.png" alt="screenshot" title="从播放列表移除" width="270" height="460" /> 
<img src="/assets/Screenshot_1551876701.png" alt="screenshot" title="搜索网络歌曲" width="270" height="460" />  <img src="/assets/Screenshot_1551876719.png" alt="screenshot" title="下载网络歌曲" width="270" height="460" />

### 特点
#### 功能
- 播放/暂停、下一首
- 搜索本地歌曲、搜索和下载网络歌曲
- 添加到播放列表、从播放列表移除
#### 学习内容
- 后台播放使用Service
- 网络访问使用okhttp，下载使用DownloadManager
- 下载完成的广播使用BroadcastReceiver接收
- 使用SQLite存储播放列表
- 网络歌曲搜索和下载使用百度音乐API
- 本地歌曲使用内置MediaStore提供的数据
