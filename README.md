# 1팀 SensorDashBoardApp
 
## 팀원 

<div align="center">
  <table style="font-weight : bold">
      <tr>
          <td align="center">
              <a href="https://github.com/tjrkdgnl">                 
                  <img alt="tjrkdgnl" src="https://avatars.githubusercontent.com/tjrkdgnl" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/gyurim">                 
                  <img alt="gyurim" src="https://avatars.githubusercontent.com/gyurim" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/014967 ">                 
                  <img alt="lsy524" src="https://avatars.githubusercontent.com/014967 " width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/gksgpd97 ">                 
                  <img alt="hoyahozz" src="https://avatars.githubusercontent.com/gksgpd97 " width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/INAH-PAK ">                 
                  <img alt="hoyahozz" src="https://avatars.githubusercontent.com/INAH-PAK " width="80" />            
              </a>
          </td>
      </tr>
      <tr>
          <td align="center">서강휘</td>
          <td align="center">박규림</td>
          <td align="center">김현국</td>
          <td align="center">한혜원</td>
          <td align="center"> 박인아</td>
      </tr>
  </table>
</div>


## 서강휘
- 담당한 일
  - Base Architecture 구조 설계
- 기여한 점
  - Clean Architecture 설계
  - Room 연동
  - Hilt 연동
  - Sensor 구현 
- 아쉬운 점
  - 자이로스코프에서 편향된 값을 세부적으로 조절하지 못한 점이 아쉽습니다. 


### Clean Architecture 

![image](https://user-images.githubusercontent.com/45396949/192689124-e2233d5e-1276-4051-bf28-1d72d4d63757.png)

-  #### Presentation
   - UI와 관련된 작업을 담당하는 Layer로 구성되어 있습니다. 
   - 대표적으로 Activity/ Fragment/ ViewModel이 해당 layer에 포함됩니다.

-  #### Domain
   - 비지니스 로직을 나타내는 Model이 해당 Layer에 포함됩니다. (SensorData / SensorType)
   - 비지니스 로직에서 수행되어져야 할 행동을 정의하고 이를 interface로 제공합니다. ( Repository / UseCase )

-  #### Data
   - 비지니스 로직에 필요한 데이터를 구성하는 Layer입니다. 여기서는 안드로이드 시스템에서 제공하는 Sensor를 다룹니다.
   - Domain에서 제공하는 인터페이스를 확장하는 클래스로 구성합니다. ( RepositoryImpl / UseCaseImpl )
   - Hilt Module을 구성하여 Presentation에서 확장된 클래스에 접근될 수 있도록 관계를 설정합니다. 

-  #### Hilt
   - 의존성에 필요한 객체들을 Module화 하고 관리하는 패키지입니다. 
   - 이로 인해 Presentation layer는 DIP에 의거하여 Data layer에서 구현한 Impl class들을 주입 받을 수 있도록 만들었습니다.
   - Room/ SensorManager / Coroutine 등 공통적으로 사용하는 부분에 대해서 Module를 생성했습니다. 


### UseCase

![image](https://user-images.githubusercontent.com/45396949/192695657-65445534-4e02-4959-b424-fdcc8f8e5de6.png)

![image](https://user-images.githubusercontent.com/45396949/192696169-7bdfe983-b8f7-46a8-a1bb-008881849fa0.png)

![image](https://user-images.githubusercontent.com/45396949/192696194-c1192d56-f637-4196-8bae-9626dfface3d.png)


- UseCase를 통해 Repository에서 접근할 수 있는 부분을 세부적으로 분리시켰습니다.  예를 들어 RoomUseCase는 Room기능만 존재하고 Repository로부터 Room과 관련된 기능만 접근할 수 있도록 제한합니다. 

- Repsitory만 사용했을 때보다 이점은 모든 Data 처리 기능을 내포한 Repository에 직접 접근하지 않기 때문에 실수를 줄일 수 있습니다. 


### Schema

![image](https://user-images.githubusercontent.com/45396949/192827795-4a976ec1-6531-4dfb-8881-ad2f76637b13.png)

- dateLong은 정렬의 용도로만 사용하기에 Entity에서만 사용해요. 실제 비지니스 모델에는 제공하지 않습니다.
- timer의 남은시간을 측정할 수 있도록 timer 프로퍼티를 추가했습니다.
- dataList는 kotlinx.Serialization으로 직렬화하여 저장하도록 했습니다.

![image](https://user-images.githubusercontent.com/45396949/192775270-dc7d221b-aede-4ba5-97a1-d3a0b10e5711.png)

- AxisDataEntity는 axis축으로만 구성되어져 있습니다.

### DAO

![image](https://user-images.githubusercontent.com/31344894/193222114-dee2ce42-8fbf-4d0a-9631-1424eb141a6a.png)


- 측정할 Sensor를 Room DB에 저장할 수 있도록 구현했습니다. 
- Flow를 통해 Room DB가 업데이트 될 시, 비동기 스트림으로 제공할 수 있도록 구현했습니다. 

### Mapper 

![image](https://user-images.githubusercontent.com/45396949/192693261-306941f8-e492-4b22-a0ed-8ec95abd164a.png)

- #### DTO
   - 여기서 DTO는 Sensor로부터 가져올 수 있는 데이터 class를 정의한 패키지입니다. 
   - DTO class들은 모두 비지니스 모델로 변환되어야합니다. 

- #### Model
   - 비지니스 모델이며 실제 UI에 보여지는 데이터들을 담은 Class입니다. 

 - #### Entity
   - Entity는 Room에 저장되는 객체를 의미합니다. 
   - 비지니스 모델은 Room 저장 시, Mapper를 통해 Entity로 변환하여 저장됩니다.
   - DB로부터 불러오는 Entity는 Mapper를 통해 비지니스 모델로 변환되어 제공됩니다. 

### Sensor

![image](https://user-images.githubusercontent.com/45396949/192956128-b9e1814a-7fa1-4406-b202-6741125c3bd7.png)

- Sensor의 구현은 data Layer의 repository 패키지에 구성되어 있습니다.
- 코루틴을 활용하여 Callback으로부터 Event를 처리하기 위해서 CallbackFlow를 사용합니다. 


### SensorScope

![image](https://user-images.githubusercontent.com/45396949/192694547-d6d48baf-1cb8-4716-a2aa-dac928e1a9a3.png)

- SensorScope는 CallbackFlow에서 channel에 event를 전송할 때 사용되는 CoroutineScope으로 사용됩니다.
- Hilt module 중 CoroutineModule에서 제공합니다.
- 하나의 스레드 Pool을 구성하여 Sensor Event를 전달하는 용도로만 사용합니다. 

![image](https://user-images.githubusercontent.com/45396949/192694803-11ba05e8-62d9-44e9-9d90-729b83acdecc.png)

- SensorScope의 정의입니다.
- Hilt로부터 제공되는 CoroutineSCope는 cancel을 제외하고 다른 Error가 발생하면 핸들링할 수없습니다. 따라서 ceh를 context에 포함한 Scope로 재구성하여 사용합니다. 
- ceh에서 잡힌 error는 ErrorFlow를 통해 방출됩니다. 

-------------------

## 김현국
- 담당한 일
    - Sensor Graph 구현
- 기여한 점
    - 데이터 graph 바인딩
    - Measure Activity 구현
- 아쉬운점
    - 그래프를 직접 구현하지 않고, library를 사용한 부분이 아쉽습니다.


### Measure Activity
<img src="https://user-images.githubusercontent.com/62296097/193047349-a8dc1079-f9a7-4a74-aaea-d56853189b06.gif" width="300" height="600" />

- RadioGroup을 사용하여 GyroScope와 가속도계를 지정할 수 있도록 하였습니다.
- 측정 버튼을 클릭시 RadioGroup을 선택할 수 없도록 하였습니다.
- 측정 버튼 클릭시, 선택한 Sensor Type으로 측정을 시작하며, 정지 버튼이 보이게 됩니다.
- 정지 버튼을 클릭시, 데이터 수집을 중단합니다.
- 측정 버튼을 다시 클릭 시 그래프를 초기화하며 재측정합니다.
- 저장버튼 클릭시, 측정한 데이터가 있을 경우, 성공 토스트를 출력하며, 측정한 데이터가 없을 경우, 측정한 데이터가 없다는 토스트를 출력합니다.


### 측정 시간 카운트 다운
```kotlin 
time = object : CountDownTimer(60000, 100) {
                override fun onTick(tick: Long) {
                    viewModel.measureTime = tick
                }

                override fun onFinish() {
                    viewModel.pressStop()
                    radioGroup[0].isEnabled = true
                    radioGroup[1].isEnabled = true
                }
            }.start()
```

```kotlin 
 fun saveSensorData() {
        viewModelScope.launch {
            var time = (60000 - measureTime) / 1000f
            val df = DecimalFormat("#.#")
            time = df.format(time).toFloat()
 ```

- 측정 시간을 저장하기 위해서, CountDownTimer를 사용했습니다.
- 최대 60초로 측정하며, countDonwInterval를 100ms로 지정하며 소수점 첫번째 단위까지 계산할 수 있도록 하였습니다.

### Acc & Gyro Data Collect
```kotlin
    measuredSensorData.collect sensorAxisData ->
        addSensorAxisData(sensorAxisData)
        addEntry(sensorAxisData.x.toDouble(), label = "x")
        addEntry(sensorAxisData.y.toDouble(), label = "y")
        addEntry(sensorAxisData.z.toDouble(), label = "z")
        binding.tvX.text = "x ${sensorAxisData.x.toDouble()}"
        binding.tvY.text = "y ${sensorAxisData.y.toDouble()}"
        binding.tvZ.text = "z ${sensorAxisData.z.toDouble()}"
    }
}    
```
- ViewModel에서 UseCase를 호출하여 data layer에서 측정되는 Sensor 데이터를 수집했습니다.

- 수집된 데이터를 저장하기 위해서 따로 List에 Sensor데이터를 저장했습니다.

- 수집된 센서 데이터들은 Graph에 바인드 되며, TextView의 텍스트를 업데이트 하도록 하였습니다.


-------------------


## 한혜원
- 담당한 일
    - 첫번째 목록 페이지 구현
- 기여한 점
    - 첫번째 목록 페이지 화면 구성
    - ItemTouchHelper를 이용해 swipe시 다시보기, 삭제 구현
    - 유저가 다시보기, 삭제를 위해 클릭하는 depth가 깊어지지 않도록 함
- 아쉬운 점
    - 아이템 삭제 시 다이얼로그를 띄워서 한 번 방어하는 게 더 나았을까? 
    - 아키텍처도 많이 손대보기


### 시연
https://user-images.githubusercontent.com/35549958/193300603-317d8c7b-6906-480d-9313-362711a8f34b.mp4


### RecyclerViewAdapter
![image](https://user-images.githubusercontent.com/35549958/193094023-a6c0f7e0-0cf6-4cbf-a15e-3ee284ca4fd6.png)
- Adapter는 ItemTouchHelper를 상속받는 SwipeController 클래스와 ItemTouchHelperListener 인터페이스를 이용해 소통
- Adapter 내부에 Interface를 추가하여 Activity에서 원하는 코드를 구현

### SwipController
![image](https://user-images.githubusercontent.com/35549958/193301647-ffdcefdd-c199-4380-9762-cec2883296b3.png)
- 왼쪽 오른쪽 스와이프 분기를 나눠서 리스너 동작


![image](https://user-images.githubusercontent.com/35549958/193302189-387b996e-f812-4fb0-9213-fb909fde5985.png)
- 스와이프된 크기와 아이템뷰 크기의 차이를 적절히 계산하여 뷰를 그려줌


### MainActivity
![image](https://user-images.githubusercontent.com/35549958/193094504-10e2ead3-986e-4d6f-a822-ff352432a3e2.png)
- Adapter로 부터 아이템의 id를 받아서 viewmodel에게 전달, 아이템 삭제  


-------------------

## 박규림

- 담당한 일
    - Paging Library를 RecylerView에 적용
- 기여한 점
    - Paging Library 구현
- 아쉬운 점
    - PagingSource를 직접 구현하지 않고 Room이 제공하는 PagingSource를 사용한 것이 아쉽습니다.
    - 추후 이전에 직접 구현한 PagingSource의 작동하지 않았던 원리를 찾아내도록 하겠습니다.

### 시연

https://user-images.githubusercontent.com/31344894/193221223-756f8725-4c2e-4d00-a77f-0c22cbc5a51e.mp4

### 용어 설명

- PagingSource: network, local datasource와 datasource에서 데이터를 검색하는 방법을 정의
- Pager: PagingData 인스턴스를 구성하는 반응형 스트림(Flow, …)을 생성
- PagingData: Paging된 데이터의 Container 역할이며 한 번에 작업할 수 있는 양 만큼 snapshot으로 만들어 제공함
- PagingDataAdapter: PagingData를 RecyclerView에 바인딩하기 위해 사용

### 상세 구현 설명

- Room으로부터 PagingSource 제공받기 위해 build.gradle 설정을 해줍니다. 
    
    `implementation("androidx.room:room-paging:2.4.3")`
    
- DAO에서 PagingSource<Int, SensorDataEntity>를 해줌으로써 PagingSource를 가져옵니다.
    - PagingSource를 가져와줌으로써 Room DB를 비동기적으로 바라보는 Flow는 필요없어집니다.
    
    ```kotlin
    // 필요없어진 쿼리문 
    // @Query("SELECT * FROM SensorDataEntity ORDER BY dateValue DESC")
    //    fun getSensorDataFlow(): Flow<List<SensorDataEntity>>
    
    @Query("SELECT * FROM SensorDataEntity ORDER BY dateValue DESC")
        fun getSensorDataPagingSource(): PagingSource<Int, SensorDataEntity>
    ```
    

- pagingSourceFactory에 앞서 전달받은 PagingSource를 설정해 Pager에게 넘겨줍니다.
    - Room으로부터 제공받은 PagingSource를 Pager에게 넘겨주면, Pager는 PagingSource를 가지고 값을 만들고 그것을 Flow에 방출시키는 구조입니다.
- PagingConfig 매개변수를 넘겨줌으로써 PagingSource에서 콘텐츠를 로드하는 방법에 대한 옵션을 설정해줍니다.
    
    ```kotlin
    // SensorRepositoryImpl.kt
    override fun getSensorDataPagerFlow(): Flow<PagingData<SensorData>> {
            return Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = false
                ),
                initialKey = 1,
                pagingSourceFactory = { localDataSource.getSensorDataPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map {
                    it.toModel(json)
            }
        }
    }
    ```
    

- UseCase에는 Pager로부터 제공하는 Flow를 넘기는 연산이 필요합니다.
    - sensorRepository.getSensorDataPagerFlow() 메서드로부터 Flow가 넘어옵니다.
    
    ```kotlin
    // RoomUseCaseImpl.kt
    override fun getSensorPagingDataFlow(): Flow<PagingData<SensorData>> {
            return sensorRepository.getSensorDataPagerFlow()
        }
    ```
    

- ViewModel에서는 UseCase로부터 데이터를 넘겨받습니다.
    
    ```kotlin
    val sensorsDataPagingFlow = roomUseCase.getSensorPagingDataFlow()
    ```
    

- MainActivity(Fragment)에서 collect를 통해 Flow 값을 제공받습니다.
    
    ```kotlin
    lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    sensorViewModel.sensorsDataPagingFlow.collectLatest { sensors ->
                        recyclerViewAdapter.submitData(sensors)
                    }
                }
            }
    ```


-------------------

## 박인아
- 담당한 일
    - 코루틴을 사용하여 타이머 구현
- 남은 작업
    - 메소드 기능 분리
- 아쉬운 점
    - 외부에서 타이머의 동작 상태를 알 수 있는 방법을 Flow로 구현하고 싶었음.
    
    
### 타이머 상태 정의


```kotlin 
sealed class CustomTimerState{
    object Start: CustomTimerState()
    object Stop: CustomTimerState()
}

```

    - 타이머의 상태는 Start, Stop 만 존재하기 때문에, sealed class로 정의하였습니다.

    
### 타이머 기능 구현   


```kotlin 
object CustomTimer {

    private var timerJob : Job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

```
    - 타이머를 여러번 동작시켜도 하나의 타이머를 동작시키기 위해 싱글톤으로 정의하였습니다.
    - 사용자가 원하는 시점에 타이머를 동작시키기위해 하나의 Job과 하나의 Coroutine Scope 으로 관리됩니다.



```kotlin 
    fun setTimerState(state: CustomTimerState, lastResumedTime : String? = null ) {
        val startDate = lastResumedTime?.let { SimpleDateFormat("HH:mm:ss").parse(it) }

        if ( lastResumedTime != null ) {
            if (startDate != null)  this.timerCount = startDate.time.toLong()
        } else {
            this.timerCount = MAX_TIME
        }

        when (state) {
            is CustomTimerState.Start ->  startTimerJob()
            is CustomTimerState.Stop -> stopTimerJob()
        }
    }

```
    - 타이머를 동작시키는 메소드 입니다. 사용자는 동작시간을 정할 수 있으며, 없다면 기본 60초로 셋팅됩니다.

```kotlin 
    private fun startTimerJob(){
        if (timerJob.isActive) timerJob.cancel()
        Log.i("CustomTimer","타이머 시작  "+ timerJob.key)

        timerJob = coroutineScope.launch {
            withContext(Dispatchers.IO) {
                this@CustomTimer.isActive = true
                while (timerCount >= 0) {
                    delay(1000L)
                    timerCount -= 1000L
                    formatTime = SimpleDateFormat(" HH:mm:ss").format(Date(timerCount))
                    Log.i("CustomTimer 경과시간 ", formatTime + "@@@@@@@@@" + timerJob.key)
                }
                this@CustomTimer.isActive = false
            }
        }


    }

```


    - 타이머 Start 구현부분 입니다. 1초간격으로 카운트다운되며, 진행시간을 저장합니다.
    - 불려진 이후 다시 불려진다면 초기 설정한 카운트다운 값으로 다시 진행됩니다.
    
<img width="625" alt="image" src="https://user-images.githubusercontent.com/95750706/193133589-65cbdd11-1eaa-4b09-b7f7-7858e6f39f3e.png">

```kotlin 
    private fun stopTimerJob():String{
        isActive = false
        Log.i("CustomTimer ","타이머 정지  " + timerJob.key)
        if (timerJob.isActive) timerJob.cancel()
        return SimpleDateFormat(" HH:mm:ss").format(Date(timerCount))
    }
```
    - 타이머 Stop 구현부분 입니다. 
    - 만일 타이머가 시작하기 전이나, 타이머가 종료된 이후에도 불려지는 경우 등 예상치 못한 이슈를 캐치하기 위해 타이머의 Active 상태를 검증합니다.





-------------------


## Convention 

### Branch Convention

``` issue-<issue Number>/<branch name>  ```

- e.g) ``` issue-#1/Base Architecture ``` 


### Commit convention

``` [prefix]: <commit content> ``` 

- e.g) ``` feat: DAO 개발완료 ```

- e.g) ``` fix: room crash 수정 ```

- e.g) ``` refactor: MVVM 아키텍처 구조 리팩토링 ```

### Issue Convention
``` [prefix] 작업할 내용 ```

- e.g) ``` [feat] base architecture 생성 ```
- e.g) ``` [fix] room crash 수정 ```
- e.g) ``` [refactor] Sensor구조 일부 수정 ```

- 브랜치를 생성하기 전, github issue를 생성해주세요.
- branch 명의 issue number는 해당 issue Number로 지정합니다. 

### PR Convention
``` [Issue-#number] PR 내용 ```

- e.g) ``` [Issue-#7] Timer 추가 ``` 


