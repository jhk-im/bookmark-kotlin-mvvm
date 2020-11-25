<h1 align="left">BookmarkSE (Kotlin - MVVM)</h1>

<p align="left">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=20"><img alt="API" src="https://img.shields.io/badge/API-20%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://jroomstudio.tistory.com/"><img alt="Medium" src="https://img.shields.io/badge/blog-tistory-green"/></a>
  <a href="https://github.com/jrooms"><img alt="Profile" src="https://img.shields.io/badge/github-jrooms-orange?logo=github&logoColor=white"/></a> 
</p>

<p align="left">
<img src="/readme/bookmark_main.png" width="250" height="500"/>
<img src="/readme/bookmark_delete.png" width="250" height="500"/>
<img src="/readme/bookmark_edit.png" width="250" height="500"/>
</p>

<p align="left">
<img src="/readme/gif/bookmark_01.gif" width="250" height="500"/>
<img src="/readme/gif/bookmark_04.gif" width="250" height="500"/>
<img src="/readme/gif/bookmark_03.gif" width="250" height="500"/>
</p>
</br>

## Architecture
![architecture](https://developer.android.com/codelabs/android-room-with-a-view-kotlin/img/a7da8f5ea91bac52.png)
</br>
</br>

## Blog Post
해당 프로젝트는 기존에 java로 만들었던 동일한 프로젝트를 kotlin으로 리팩토링 한 것입니다.   
kotlin과 MVVM 디자인 패턴 학습을 위해 작성 되었으며,  
해당 프로젝트를 분석하고 구현하면서 공부했던 주요 내용들을 블로그에 정리해 두었습니다.   
[MVVM 패턴](https://jroomstudio.tistory.com/24?category=386216)
</br>
</br>


## 사용기술 및 라이브러리
- Minimum SDK level 20
- [Kotlin](https://kotlinlang.org/) based
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- JetPack
  - LiveData
  - Lifecycle
  - ViewModel
  - Room Database
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository pattern
- [Glide](https://github.com/bumptech/glide)
- [Material-Components](https://github.com/material-components/material-components-android)
</br>
</br>


## Referenced Project
<p align="left">
  <a href="https://github.com/jrooms/architecture-samples/tree/todo-mvvm-live-kotlin"><img alt="Pokedex" src="/readme/architecture_samples.png"/></a>
</p>
</br>

```
Copyright (C) 2020 The Android Open Source Project

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements. See the NOTICE file distributed with this work for
additional information regarding copyright ownership. The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```
