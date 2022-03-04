## Distance 原生Android开源项目

该项目的是《初学者入门后的进阶》系列的一部分内容
> Distance系列项目立志于快捷搭建私有IM系统

### 项目技术栈
  - Kotlin Coroutine
  - Kotlin Flow
  - Jetpack
  - Room
  - Paging3
  - MVVM架构
  - Custom View
  - ExoPlayer2
  - WebSocket-Client
  - Markdown
  - Volley
  - Coil
### 初学者应该预先了解的知识
  - Java 8
  - Kotlin
  - Json
  - Android 基础

### Features
- * [x] 注册登录
- * [ ] 基础会话
- * [ ] 创建会话
- * [ ] 账户头像
- * [x] 账户更新

### 项目结构
- main
  - java.com.unltm.distance
    - adapter
      - bottomsheet
        - BottomSheetAdapter.kt
        - ExpansionAdapter.kt
      - BottomSheetAdapter.kt
      - ExpansionAdapter.kt
    - base
      - collections
        - ExpansionType.kt
        - Items.kt
        - Launchers.kt
      - contracts
        - _Bitmaps.kt
        - _Colors.kt
        - _Compares.kt
        - _Contexts.kt
        - _Converters.kt
        - _Coroutines.kt
        - _DataStores.kt
        - _Requires.kt
        - _Singletons.kt
        - _SoundPools.kt
        - _Strings.kt
        - _Times.kt
        - _Unicodes.kt
        - _Widgets.kt
      - file
        - FileType.kt
        - FileUtils.kt
        - OnDownloadListener.kt
      - jvm
        - QRCodeConverter.kt
        - QRCodeUtil.kt
      - CrashHandler.kt
      - ProgramSplitter.kt
      - Result.kt
      - ServerException.kt
    - datasource
      - config
        - AccountConfig.kt
        - AuthConfig.kt
        - BaseConfig.kt
      - gson
        - GetLivePreview.kt
        - GetLiveState.kt
        - GetRealUrl.kt
      - impl
        - AccountDataSource.kt
        - AuthDataSource.kt
      - *IAccountDataSource.kt*
      - *IAuthDataSource.kt*
      - *LiveDataSource.kt*
      - *RequestResult.kt*
    - fragment
      - listbottomsheet
        - ListBottomSheet.kt
    - repository
      - AccountRepository.kt
    - room
      - converter
        - StringListConverter.kt
      - dao
        - UserDao.kt
        - UserRichDao.kt
      - entity
        - User.kt
        - UserRich.kt
      - MyDatabase.kt
    - storage
      - AccountStorage.kt
      - AuthStorage.kt
    - ui
      - account
        - exception
          - GetRichInfoException.kt
        - result
          - UploadHeadPicture.kt
        - AccountActivity.kt
        - AccountViewModel.kt
      - components
        - background
          - Gradual.java
          - RecordStatusDrawable.kt
          - StatusDrawable.kt
        - ceil
          - button
            - RadioButton.kt
            - ShutterButton.kt
          - ChatView.kt
          - LoadingProgress.kt
          - RatioRegulator.kt
          - Switch.kt
          - TextDrawable.kt
        - dialog
          - BitmapDialog.kt
          - DialogUtils.kt
          - LoadingDialog.kt
          - NetworkImageDialog.kt
          - ProgressDialog.kt
          - VoiceDialog.kt
        - layout
          - MaterialNavigationView.kt
          - SensorLayout.kt
        - photoview
          - <etc.>
        - wave
          - AXLineWaveDrawable.kt
          - AXLineWaveView.kt
          - AXLineWeavingState.kt
          - AXWaveDrawable.kt
          - AXWaveView.kt
          - AXWeavingState.kt
          - CubicBezierInterpolator.kt
        - HSVEvaluator.kt
      - conversation
        - exception
          - AccountNotExistException.kt
        - result
          - GetCurrentUser.kt
        - ConversationActivity.kt
        - ConversationViewModel.kt
      - edit
        - result
          - UpdateResult.kt
        - EditActivity.kt
        - EditViewModel.kt
      - live
        - player
          - PlayerActivity.kt
          - PlayerViewModel.kt
        - reco
          - editor
            - TextEditor.kt
            - TextEditorCallback.kt
          - result
            - GetSearchResult.kt
          - RecoActivity.kt
          - RecoAdapter.kt
          - RecoViewModel.kt
        - result
          - GetRealUrlResult.kt
        - GetRealUrlException.kt
        - LivePreview.kt
        - LiveRoomNotPlayingException.kt
        - UnsupportedLiveRoomException.kt
      - login
        - result
          - GetRichUserResult.kt
          - LoginResult.kt
          - SignResult.kt
        - LoginActivity.kt
        - LoginViewModel.kt
      - settings
        - components
          - Item.kt
          - CheckboxSetting.kt
          - EditTextSetting.kt
          - ProgressBarSetting.kt
          - SwitchSetting.kt
          - TextSetting.kt
          - ThemeSpanSetting.kt
        - Fork.kt
        - SettingsActivity.kt
    - *MainActivity.kt*
    - *MyApplication.kt*

点击[这里](https://github.com/thxbrop/Distance)获取该项目最新源代码

点击[这里](https://github.com/thxbrop/DistanceTomcat)获取该项目对应的后端代码

点击[这里]()获取此项目对应的MySQL数据库开源项目
