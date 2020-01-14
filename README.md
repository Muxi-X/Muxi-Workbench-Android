## 木犀工作台
---
**木犀团队**  
开发中...  

注意：
因为GFW原因，build.gradle有两个版本（仓库分为国外和阿里），请自行创建
### 阿里源：
```
buildscript {
    repositories {
        maven { url'https://maven.aliyun.com/repository/public/' }

        maven { url'https://maven.aliyun.com/repository/google/' }

        maven { url'https://maven.aliyun.com/repository/jcenter/' }

        maven { url'https://maven.aliyun.com/repository/central/' }

    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.0'
        
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        maven { url"https://raw.githubusercontent.com/HyphenateInc/Hyphenate-SDK-Android/master/repository" }

        maven { url'https://maven.aliyun.com/repository/public/' }

        maven { url'https://maven.aliyun.com/repository/google/' }

        maven { url'https://maven.aliyun.com/repository/jcenter/' }

        maven { url'https://maven.aliyun.com/repository/central/' }


        
    }
}

```
### 一般
```

buildscript {
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.0'
        
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        
    }
}

```
