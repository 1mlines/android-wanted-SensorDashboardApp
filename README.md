## Members

<div align="center">
<table style="font-weight : bold">
  <tr>
    <td align="center">
      <a href="[https://github.com/KimHance](https://github.com/KimHance)">
        <img alt="김현수" src="https://avatars.githubusercontent.com/KimHance" width="80" />
      </a>
    </td>
    <td align="center">
      <a href="[https://github.com/dudwls901](https://github.com/dudwls901)">
        <img alt="김영진" src="https://avatars.githubusercontent.com/dudwls901" width="80" />
      </a>
    </td>
    <td align="center">
      <a href="[https://github.com/yforyuri](https://github.com/yforyuri)">
        <img alt="노유리" src="https://avatars.githubusercontent.com/yforyuri" width="80" />
      </a>
    </td>
    <td align="center">
      <a href="[https://github.com/YellowC-137](https://github.com/YellowC-137)">
        <img alt="황준성" src="https://avatars.githubusercontent.com/YellowC-137" width="80" />
      </a>
    </td>
    <td align="center">
      <a href="[https://github.com/hoyahozz](https://github.com/hoyahozz)">
        <img alt="김정호" src="https://avatars.githubusercontent.com/hoyahozz" width="80" />
      </a>
    </td>
</tr>

<tr>
<td align="center">김현수</td>
<td align="center">김영진</td>
<td align="center">노유리</td>
<td align="center">황준성</td>
<td align="center">김정호</td>
</tr>
</table>
</div>

## Summary

- 가속도(acc) 3축 + 각속도(gyro) 3축 데이터를 다루는 서비스
- SensorManager를 이용해 가속도, 각속도 데이터를 수집합니다.
- Room을 이용해 로컬 저장소에 가속도, 각속도 데이터를 저장합니다.
- 저장된 데이터들은 Paging을 이용하여 일정량씩 목록에 나열합니다.
- 저장된 데이터를 불러와서 그래프로 표현합니다.

## 기술 스택

![image](https://user-images.githubusercontent.com/85336456/193163175-570e23d8-eabb-4332-9f41-c6e4379e2d67.png)

## Structure

```kotlin
├── common
│   ├── base
│   ├── constant
│   ├── di
│   ├── extension
│   └── util
├── data
│   ├── local
│   │   ├── dao
│   │   ├── datasource
│   │   ├── datasourceimpl
│   │   └── entity
│   ├── paging
│   └── repositoryimpl
├── domain
│   ├── model
│   ├── repository
│   └── usecase
└── presentation
    ├── adapter
    ├── view
    │   ├── sensor_history_list
    │   ├── sensor_history_measure
    │   ├── sensor_history_play
    │   └── sensor_history_show
    └── viewmodel
```

# 김영진

## 맡은 역할

- Base 설계
    - Library Dependency 셋팅
    - CleanArchitecture
    - MVVM Design Pattern
    - Hilt Module 셋팅
- 화면 이동 구현
    - Jetpack Navigation이용하여 Fragment 스택 관리
    - ToolBar 구현

## Base 설계

### 1) Library Dependency 셋팅

- 사용할 기술들에 대한 Dependency 셋팅
- project gradle에 변수 선언하여 버전 관리 (Groovy)

```kotlin
//build.gradle (project)
ext {
        hiltVersion = '2.42'
}

//build.gralde (app)
dependencies {
    //Hilt
    implementation "com.google.dagger:hilt-android:$hiltVersion"
    kapt "com.google.dagger:hilt-android-compiler:$hiltVersion"
}
```

### 2) CleanArchitecture

- 요구사항에 따라 필요한 Room(Dao, DataBase), UseCase, Repository, View, ViewModel 등을 정의하여 팀원들이 기능 개발에 집중할 수 있도록 Base 구축
- Data
    - 실제 Data와 로컬 저장소가 상호작용 합니다.
    - Mapper를 이용해 Entity를 Domain의 Model로 변환하고, 도메인에 정의한 Repository의 구현체를 포함합니다.
- Domain
    - Data와 Presentation의 중간 계층입니다.
    - UseCase에서 비즈니스 로직을 직관적인 이름으로 정의합니다.
    - UI에 사용될 Data를 Model로 정의합니다.
- Presentation
    - UI 계층으로, 사용자와 상호작용하며 Android 프레임워크와 가까운 계층입니다.
    - MVVM 디자인 패턴을 지향합니다
    - ViewController에서 Android FrameWork 관련 작업을 수행합니다.
    - ViewModel에서 UI 데이터를 관리하고, Domain의 UseCase를 사용하여 Data 계층과 소통합니다.
    - DataBinding을 이용해 화면에 데이터를 표시합니다.
- common
    - base, constant, di, extension, util 등 위 계층들 외 코드들로 구성되어있습니다.

### 3) Hilt Module 셋팅

- DataBase, DataSource, Repository, Sensor에 대한 의존성 주입 모듈 구현
- DataSourceModule

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindSensorHistoryLocalDataSource(
        sensorHistoryLocalDataSourceImpl: SensorHistoryLocalDataSourceImpl,
    ): SensorHistoryLocalDataSource
}
```

- SensorModule

```kotlin
@Module
@InstallIn(ActivityComponent::class)
object SensorModule {

    @ActivityScoped
    @Provides
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager =
        context.getSystemService(SENSOR_SERVICE) as SensorManager

}
```

## 화면 이동 구현

### 1) Jetpack Navigation이용하여 Fragment 스택 관리

- Navigation, BindingAdapter를 이용하여 화면 이동, action에 대한 base를 정의하였습니다.

```kotlin
@BindingAdapter("app:navigateTo")
fun navigateTo(view: View, viewName: ViewName) {
    view.setOnClickListener {
        val action = when (viewName) {
            ViewName.MEASURE -> SensorHistoryListFragmentDirections.actionSensorHistoryListFragmentToSensorHistoryMeasureFragment()
            //todo PLAY, SHOW navArgs
            ViewName.PLAY -> SensorHistoryListFragmentDirections.actionSensorHistoryListFragmentToSensorHistoryPlayFragment()
            ViewName.SHOW -> SensorHistoryListFragmentDirections.actionSensorHistoryListFragmentToSensorHistoryShowFragment()
        }
        view.findNavController().navigate(action)
    }
}

@BindingAdapter("app:navigateUp")
fun navigateUp(view: View, dummy: Any?) {
    view.setOnClickListener {
        view.findNavController().navigateUp()
    }
}
```

### 2) ToolBar 구현

<img width="520" alt="Untitled (12)" src="https://user-images.githubusercontent.com/66052467/193164213-5a3cca0c-4208-473f-b695-7030011bead1.png">

## 아쉬운점

핵심 기능에 기여하지 못한 점은 아쉬웠으나, 요구사항, 팀원들과 협의한 내용 등을 바탕으로 아키텍처를 설계하고 기능 개발을 위한 Base를 구축하는 과정도 재밌었습니다.

또한, Repository의 Label, Issue Template, PR Template, Projects를 이용한 칸반 보드 등을 셋팅하는 것도 좋은 경험이었습니다.

# 노유리

## 맡은 역할

## 기능 설명

## 아쉬운점

# 황준성

## 맡은 역할

- `SensorHistoryMeasureFragment`, `SensorHistoryMeasureViewModel` 구현
    - Timer 구현
    - `ACCELEROMETER, Gyroscope`센서 설정
    - RoomDB에 센서 데이터 저장

## 기능 설명

- `SensorHistoryMeasureFragment`, `SensorHistoryMeasureViewModel` 설정

센서와 타이머를 설정 및 구현하고, 요구사항에 맞춰 버튼 및 텍스트 클릭시 센서와 타이머의 작동여부를 결정했습니다.

- Timer 구현

간단하게 0.1초 간격으로 시간을 증가시켜 최대 60초 까지만 진행되는 타이머입니다.

60초가 되면 타이머가 정지하고 센서매니저의 센서리스너를 unregister 시켜 센서를 정지시킵니다.

![image](https://user-images.githubusercontent.com/85336456/193163238-20c3b93d-62d4-4918-ac19-a3eea2dccd19.png)

![image](https://user-images.githubusercontent.com/85336456/193163250-632e688f-58ed-45b8-9272-fd15f7dc6fc9.png)

- `ACCELEROMETER, GYROSCOPE`센서 설정

센서가 0.1초 (****100000 마이크로초****) 마다 측정이 되어야 해서 이벤트 리스너에 이를 넣어주었고, 이벤트 리스너 생성시에 2개의 센서 분기를 나누고 센서의 값이 변할때 마다 0.1초 주기로 이를 감지하여 측정 리스트에 x,y,z 값을 넣어주게 했습니다.

![image](https://user-images.githubusercontent.com/85336456/193163260-3dc38381-0d90-4592-966c-bcad95407ccb.png)

![image](https://user-images.githubusercontent.com/85336456/193163267-8a719b5b-f696-453d-b171-754b75d7440c.png)

![image](https://user-images.githubusercontent.com/85336456/193163276-5d9efad6-0e84-4e07-be4a-64ed49c145b7.png)

- RoomDB에 센서 데이터 저장

저장 텍스트 클릭시 센서타입,현재날짜,측정시간,좌표값들을 RoomDB에 저장하고 측정용 데이터 들을 초기화 시키고 마찬가지로 센서를 정지시킵니다.

![image](https://user-images.githubusercontent.com/85336456/193163360-3c2aac7f-c66b-4cb6-8cfb-a2bf9cf5f7f8.png)

![image](https://user-images.githubusercontent.com/85336456/193163393-b01a02e9-ca92-480d-ba9f-5a3748e0150f.png)

![ezgif-3-a9fab2f49b](https://user-images.githubusercontent.com/85336456/193163435-c12a55ed-a2a1-4581-a65e-30a4cdc73b3f.gif)


## 아쉬운 점

- 중간중간 구현사항에서 깜빡한 부분들이 있어 이를 완벽하게 구현하지 못한것 같아 아쉽습니다.
- 완성도에 좀 더 초점을 맞춰서 코드의 재사용성이나 가독성등을 고려하지않은체 좀 중구난방으로 코드를 작성한것 같아 아쉽습니다.
- 아직 MVVM+AAC 의 완벽한 이해가 안된것 같아 아쉽습니다.

# 김현수

## 맡은 역할

- 센서 변동 값 그래프 표현
- 센서 등록 및 데이터 관리

## 기능 설명

### 센서 → 그래프 과정

```kotlin
@Inject
lateinit var sensorManager: SensorManager
```

센서 매니저는 DI를 통해 사용합니다.

```kotlin
private fun setSensorType(sensorType: Int) {
    sensorManager.unregisterListener(sensorEventListener)
    sensor = sensorManager.getDefaultSensor(sensorType)
}
```

센서 종류를 선택하면 센서의 타입을 정합니다.

```kotlin
if(::sensor.isInitialized) {
    ... 
    registerSensorListener(sensorEventListener, sensor)
    ...
```

이후 측정 버튼을 클릭하면 리스너를 등록하여 측정을 시작합니다.

```kotlin
// Fragment
sensorHistoryMeasureViewModel.updateCurrentMeasureValue(
	event.values[X],
	event.values[Y],
	event.values[Z]
)

// ViewModel
private val _currentMeasureValue = MutableStateFlow(MeasureValue())
val currentMeasureValue = _currentMeasureValue.asStateFlow()

fun updateCurrentMeasureValue(x: Float, y: Float, z: Float) {
	viewModelScope.launch {
		_currentMeasureValue.update {
			_currentMeasureValue.value.copy(x, y, z)
		}
	}
}
```

센서값이 측정될 때마다 측정된 값을 _currentMeasureValue에 업데이트 합니다.

```kotlin
viewLifecycleOwner.lifecycleScope.launch{
	viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
		sensorHistoryMeasureViewModel.currentMeasureValue.collect(){measureValue->
				sensorHistoryMeasureViewModel.addEntry(
					measureValue.x,
					measureValue.y,
					measureValue.z
        )
        binding.chartView.apply{
					notifyDataSetChanged()
             invalidate()
				}
	...
```

측정된 값을 collect하여 그래프에 표기합니다.

## 아쉬운점

- 시간이 거듭될 수록 점점 스파게티 코드가 된 것 같다.
- 센서의 측정 주기 정확도에 있어서 약간의 오차가 있어서 아쉽다.
- 캔버스를 통해 직접 그리려고 하였으나 아쉽게 라이브러리를 사용하게 되었다.
- 데이터 처리를 깔끔하게 할 수 있었는데 그러지 못한 것 같다.

# 김정호

## 맡은 역할

- 요구사항의 세 번째 페이지를 구현하였습니다.
    - 측정 결과를 애니메이션과 함께 그래프에 출력하도록 구현하였습니다.
    - 측정 결과를 단순 그래프에 출력하도록 구현하였습니다.

## 기능 설명

![Play](https://user-images.githubusercontent.com/85336456/193163851-35c445e8-3655-4aca-9fc9-4754186c5d76.gif)

- 측정 결과를 애니메이션과 함께 그래프에 출력합니다.
- 한 화면에 표현되는 측정 결과량을 20개로 제한하였습니다.
- 시작 버튼을 누르면 타이머가 작동됩니다.
- 재생, 정지 상황에 맞게 아이콘이 업데이트 됩니다.
- 애니메이션 출력이 종료되면 타이머가 멈춤과 동시에 아이콘 역시 재생 아이콘으로 변경됩니다.

![image](https://user-images.githubusercontent.com/85336456/193163880-75fbdb51-eadf-4673-abb2-90591822f92f.png)

- 측정 결과를 한 번에 그래프에 출력합니다.

## 아쉬운점

- 익숙하지 않은 요구사항이라 학습에 시간을 오래 소요했습니다.
- 코드 퀄리티를 신경쓰지 못한 것이 아쉽습니다.
- 학습할 시간이 없는 상황으로 인해 라이브러리 대신 커스텀 뷰를 사용하지 못한 점이 아쉽습니다.
- 그러나 새로운 기술을 익히는 과정이 즐거웠습니다.
