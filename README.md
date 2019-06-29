# 使用说明

# 使用方法


```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

### 添加依赖

[![](https://jitpack.io/v/yzhg0854/YzhgDemo.svg)](https://jitpack.io/#yzhg0854/YzhgDemo)

```
dependencies {
	implementation 'com.github.yzhg0854:YzhgDemo:1.0.6'
}
```



### base包下
    
 - BasicActivity  activity基类 可以直接继承此类
 - BasicFragment Fragment基类
 - MVP
    - MVPBaseActivity 需要使用MVP模式的时候继承此类
    - base 此包下是关于MVP的封装
    - MVPBaseFragment 需要使用MVP模式继承此类
 
### http包下
 - 此包下封装了一个Retrofit+OkHttp+Rxjava的网络请求
 - 详细用法如下
 - 
### utils
 - ActivityManager 用于Activity保存和退出
 - CountDemoTime 倒计时控件
 - CustomDialog 自定义的dialog
 - GlideUtil Glider进一步封装
 - JsonUtil Json封装
 - LogUtils 日志输出封装
 - PermissionTools 权限封装
 - RxJavaUtils RxJava线程切换封装
 - SDCaedOperation SD卡的操作
 - SpannableStringUtils SpannableString封装
 - SpUtils SP的封装
 - TheadUtils 主线程与子线程的切换
 - Toast Toast的封装适配Android 8版本出现的关闭通知后无法弹起Toast
 - Tools 主要工具类 使用前必须初始化






