# 解决什么问题？

### 1. 快速创建 StatefulWidget、 StatelessWidget

![img.png](images/img1.png)

```dart

import 'package:flutter/material.dart';

///
/// @date: 2024/08/22 20:54
/// @author: TangRen
/// @remark:
///

class UserLogin extends StatelessWidget {

  const UserLogin({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(appBar: AppBar(), body: Container());
  }  
}

```
### 2.左边栏预览图片、icon
支持预览：jpeg、png、webp、bmp、svg

![img.png](images/img2.png)

#### 2.2 生成图片声明常量类
![img.png](images/img4.png)


### 3.下划线分隔的蛇形命名快速转驼峰 cameCase
![img.png](images/img3.png)