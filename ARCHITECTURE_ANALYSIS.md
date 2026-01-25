# Подробный разбор архитектуры и логики работы приложения DidaktikApp

## 📋 Содержание
1. [Общая архитектура](#общая-архитектура)
2. [Инициализация приложения и базы данных](#инициализация-приложения-и-базы-данных)
3. [Структура базы данных](#структура-базы-данных)
4. [Поток данных при запуске](#поток-данных-при-запуске)
5. [Игровой процесс и сохранение прогресса](#игровой-процесс-и-сохранение-прогресса)
6. [Обновление счетчика в реальном времени](#обновление-счетчика-в-реальном-времени)
7. [Диаграмма потока данных](#диаграмма-потока-данных)

---

## 🏗️ Общая архитектура

Приложение использует **MVVM (Model-View-ViewModel)** архитектуру с следующими компонентами:

- **View**: Jetpack Compose UI компоненты
- **ViewModel**: Управление состоянием и бизнес-логикой
- **Repository**: Слой абстракции для работы с данными
- **DAO (Data Access Object)**: Интерфейсы для работы с Room Database
- **Entity**: Модели данных базы данных
- **Dependency Injection**: Hilt для управления зависимостями

---

## 🚀 Инициализация приложения и базы данных

### 1. Точка входа: MainActivity

**Файл**: `MainActivity.kt`

```kotlin
@AndroidEntryPoint  // Hilt автоматически внедряет зависимости
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var database: BarakaldoDatabase  // База данных инжектируется через Hilt
}
```

**Процесс:**
1. Android система создает `MainActivity`
2. Hilt инициализирует зависимости (включая базу данных)
3. `onCreate()` настраивает UI через Jetpack Compose
4. Отображается `ScreenManager` (главный экран)

### 2. Инициализация базы данных: AppModule

**Файл**: `di/AppModule.kt`

```kotlin
@Provides
@Singleton
fun provideDatabase(@ApplicationContext context: Context): BarakaldoDatabase {
    val database = Room.databaseBuilder(
        context,
        BarakaldoDatabase::class.java,
        "barakaldo_db"
    )
        .addCallback(DatabaseInitializer())  // Callback для инициализации
        .fallbackToDestructiveMigration()
        .build()
    
    // Асинхронная инициализация данных
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    applicationScope.launch {
        DatabaseInitializer.initializeDatabase(database)
    }
    
    return database
}
```

**Что происходит:**
- Room создает базу данных SQLite с именем `barakaldo_db`
- При первом создании вызывается `DatabaseInitializer.onCreate()`
- Асинхронно запускается проверка и дополнение данных через DAO

### 3. Инициализация данных: DatabaseInitializer

**Файл**: `data/db/DatabaseInitializer.kt`

#### 3.1. SQL инициализация (onCreate callback)

При первом создании базы данных выполняется SQL напрямую:

```kotlin
override fun onCreate(db: SupportSQLiteDatabase) {
    super.onCreate(db)
    initializeWithSQL(db)  // Прямые SQL запросы для гарантии ID
}
```

**Создаются следующие данные:**

1. **21 изображение** (таблица `imagen`):
   - ID 1-7: Основные изображения мест
   - ID 8-14: Премии старые (premio antiguo)
   - ID 15-21: Премии новые (premio actual)

2. **7 мест** (таблица `lugar`):
   - ID 1: La Ermita de Santa Agueda
   - ID 2: La iglesia de San Vicente
   - ID 3: El Acertijo del Puente
   - ID 4: El Edificio Ilgner
   - ID 5: Rompecabezas
   - ID 6: El ferrocarril
   - ID 7: Palacio Munoa

3. **7 активностей** (таблица `actividad`):
   - ID 1: Puzzle (связана с lugar_id=1)
   - ID 2: Sopa de letras (связана с lugar_id=2)
   - ID 3: Sopa de letras (связана с lugar_id=3)
   - ID 4: Selección (связана с lugar_id=4)
   - ID 5: Puzzle (связана с lugar_id=5)
   - ID 6: Puzzle (связана с lugar_id=6)
   - ID 7: Clasificación (связана с lugar_id=7)

4. **1 пользователь** (таблица `persona`):
   - ID 1: username="testuser", tipo_persona="Usuario"

5. **1 запись Usuario** (таблица `usuario`):
   - persona_fk=1, nombre_completo_diploma="Usuario de Prueba"

#### 3.2. Дополнительная проверка (initializeDatabase)

После создания базы данных асинхронно проверяется, нужно ли дополнять данные:

```kotlin
suspend fun initializeDatabase(database: BarakaldoDatabase) {
    val actividades = contenidoDao.getAllActividadesIds()
    if (actividades.isNotEmpty()) {
        return  // База уже инициализирована
    }
    // Дополнительная инициализация через DAO (резервный метод)
}
```

---

## 🗄️ Структура базы данных

### Схема базы данных (BarakaldoDatabase.kt)

```kotlin
@Database(
    entities = [
        PersonaEntity::class,      // Пользователи (родительский класс)
        UsuarioEntity::class,      // Ученики (наследуется от Persona)
        ProfesorEntity::class,     // Преподаватели (наследуется от Persona)
        LugarEntity::class,        // Места на карте
        ActividadEntity::class,    // Игровые активности
        ImagenEntity::class,       // Изображения
        ProgresoUsuarioEntity::class  // Прогресс пользователя
    ],
    version = 1
)
```

### Связи между таблицами (Foreign Keys)

```
imagen (1) ──┐
             ├──> lugar (imagen_principal_fk)
             │
             ├──> actividad (premio_antigua_fk)
             └──> actividad (premio_actual_fk)

lugar (1) ──> actividad (lugar_fk)

persona (1) ──> usuario (persona_fk) [1:1]

actividad (1) ──> progreso_usuario (actividad_fk)
persona (1) ──> progreso_usuario (persona_fk)
```

### Ключевые таблицы

#### 1. `progreso_usuario` - Прогресс пользователя
```sql
CREATE TABLE progreso_usuario (
    progreso_id INTEGER PRIMARY KEY AUTOINCREMENT,
    actividad_fk INTEGER,      -- FK к actividad
    persona_fk INTEGER,        -- FK к persona
    completada INTEGER,        -- 0 = не завершена, 1 = завершена
    fecha_completado TEXT      -- Дата завершения
)
```

**Важно**: Эта таблица создается динамически при завершении игры, а не при инициализации.

---

## 📱 Поток данных при запуске

### Последовательность инициализации

```
1. MainActivity.onCreate()
   │
   ├─> Hilt инициализирует зависимости
   │   │
   │   └─> AppModule.provideDatabase()
   │       │
   │       ├─> Room.databaseBuilder() создает БД
   │       │   │
   │       │   └─> DatabaseInitializer.onCreate() [SQL инициализация]
   │       │       ├─> INSERT 21 imagen
   │       │       ├─> INSERT 7 lugar
   │       │       ├─> INSERT 7 actividad
   │       │       ├─> INSERT 1 persona (ID=1)
   │       │       └─> INSERT 1 usuario (persona_fk=1)
   │       │
   │       └─> CoroutineScope.launch { initializeDatabase() }
   │           └─> Проверка через DAO (резервный метод)
   │
   └─> setContent { ScreenManager() }
       │
       └─> CounterViewModel создается через hiltViewModel()
           │
           └─> count: StateFlow<Int> подписывается на Flow из БД
```

### ScreenManager - Главный экран

**Файл**: `ScreenManager.kt`

```kotlin
@Composable
fun ScreenManager(languageViewModel: LanguageViewModel) {
    val navController = rememberNavController()
    
    // ViewModel создается через Hilt
    val counterViewModel: CounterViewModel = hiltViewModel()
    
    // Подписка на StateFlow счетчика
    val count by counterViewModel.count.collectAsState()
    
    // TopBar отображает счетчик
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                counterViewModel = counterViewModel,
                onMenuClick = { /* открыть меню */ }
            )
        }
    ) {
        NavHost(navController, startDestination = "map") {
            composable("map") { MapScreen(navController, counterViewModel) }
            composable("activity1") { Activity1Screen(navController) }
            // ... другие экраны
        }
    }
}
```

---

## 🎮 Игровой процесс и сохранение прогресса

### Пример: Завершение первой игры (Activity 1)

#### 1. Игровой процесс (Game1ViewModel)

**Файл**: `viewmodels/Game1ViewModel.kt`

```kotlin
@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: ProgresoRepository
) : ViewModel() {
    
    private val currentUserId = 1      // ID пользователя
    private val currentActivityId = 1  // ID активности
    
    // Игровая логика...
    
    private fun validateQuizAnswer() {
        if (selectedQuizOption == 1) {  // Правильный ответ
            isRewardUnlocked = true
            saveProgressToDatabase()  // ⭐ Сохранение прогресса
        }
    }
    
    private fun saveProgressToDatabase() {
        viewModelScope.launch {
            repository.markActivityAsCompleted(
                actividadId = currentActivityId,  // 1
                personaId = currentUserId         // 1
            )
        }
    }
}
```

#### 2. Репозиторий (ProgresoRepository)

**Файл**: `data/repositories/ProgresoRepository.kt`

```kotlin
class ProgresoRepository @Inject constructor(
    private val progresoDao: ProgresoDao
) {
    suspend fun markActivityAsCompleted(actividadId: Int, personaId: Int) {
        val currentDate = System.currentTimeMillis().toString()
        progresoDao.upsertProgresoCompletado(actividadId, personaId, currentDate)
    }
}
```

#### 3. DAO - UPSERT операция (ProgresoDao)

**Файл**: `data/db/daos/ProgresoDao.kt`

```kotlin
@Transaction
suspend fun upsertProgresoCompletado(actividadId: Int, personaId: Int, date: String) {
    // 1. Проверка существования foreign keys
    val actividadExists = existsActividad(actividadId)  // Проверка actividad_id=1
    val personaExists = existsPersona(personaId)        // Проверка persona_id=1
    
    if (!actividadExists || !personaExists) {
        // Логирование ошибки и попытка UPDATE
        updateProgresoCompletado(actividadId, personaId, date)
        return
    }
    
    // 2. Проверка существования записи прогресса
    val exists = existsProgreso(actividadId, personaId)
    
    // 3. INSERT или UPDATE
    if (!exists) {
        // Создание новой записи с completada=1
        insertSingleProgreso(
            ProgresoUsuarioEntity(
                actividad_fk = actividadId,
                persona_fk = personaId,
                completada = 1,
                fecha_completado = date
            )
        )
    } else {
        // Обновление существующей записи
        updateProgresoCompletado(actividadId, personaId, date)
    }
}
```

**Что происходит в базе данных:**

```sql
-- Если запись не существует:
INSERT INTO progreso_usuario 
(actividad_fk, persona_fk, completada, fecha_completado)
VALUES (1, 1, 1, '1705756800000')

-- Если запись существует:
UPDATE progreso_usuario 
SET completada = 1, fecha_completado = '1705756800000'
WHERE actividad_fk = 1 AND persona_fk = 1
```

---

## 🔄 Обновление счетчика в реальном времени

### CounterViewModel - Управление счетчиком

**Файл**: `viewmodels/CounterViewModel.kt`

```kotlin
@HiltViewModel
class CounterViewModel @Inject constructor(
    private val progresoDao: ProgresoDao
) : ViewModel() {
    
    private val personaId = 1
    
    // StateFlow, который автоматически обновляется при изменении БД
    val count: StateFlow<Int> = progresoDao.getCountCompletados(personaId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
}
```

### Запрос к базе данных

**Файл**: `data/db/daos/ProgresoDao.kt`

```kotlin
@Query("""
    SELECT COUNT(*) 
    FROM progreso_usuario 
    WHERE persona_fk = :personaId AND completada = 1
""")
fun getCountCompletados(personaId: Int): Flow<Int>
```

**Как работает Flow:**
1. Room автоматически отслеживает изменения в таблице `progreso_usuario`
2. При любом INSERT/UPDATE/DELETE в этой таблице Flow эмитит новое значение
3. `StateFlow` преобразует Flow в состояние, доступное для Compose
4. UI автоматически перерисовывается при изменении значения

### TopBar - Отображение счетчика

**Файл**: `components/TopBar.kt`

```kotlin
@Composable
fun TopBar(
    navController: NavController,
    counterViewModel: CounterViewModel,
    onMenuClick: () -> Unit
) {
    // Подписка на StateFlow
    val currentCount by counterViewModel.count.collectAsState()
    
    BadgedBox(
        badge = {
            Badge {
                Text(currentCount.toString())  // ⭐ Отображает актуальное значение
            }
        }
    ) {
        Image(/* аватар Jolin */)
    }
}
```

### Полный цикл обновления

```
1. Игра завершена
   │
   └─> Game1ViewModel.saveProgressToDatabase()
       │
       └─> ProgresoRepository.markActivityAsCompleted()
           │
           └─> ProgresoDao.upsertProgresoCompletado()
               │
               └─> INSERT/UPDATE в progreso_usuario
                   │
                   └─> Room обнаруживает изменение
                       │
                       └─> Flow<Int> эмитит новое значение
                           │
                           └─> CounterViewModel.count обновляется
                               │
                               └─> TopBar.collectAsState() получает новое значение
                                   │
                                   └─> BadgedBox перерисовывается с новым числом
```

---

## 📊 Диаграмма потока данных

```
┌─────────────────────────────────────────────────────────────┐
│                    ИНИЦИАЛИЗАЦИЯ ПРИЛОЖЕНИЯ                 │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  MainActivity.onCreate()                                    │
│  └─> Hilt инициализирует зависимости                        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  AppModule.provideDatabase()                                │
│  ├─> Room.databaseBuilder()                                 │
│  │   └─> DatabaseInitializer.onCreate() [SQL]               │
│  │       ├─> INSERT 21 imagen                               │
│  │       ├─> INSERT 7 lugar                                 │
│  │       ├─> INSERT 7 actividad                             │
│  │       ├─> INSERT 1 persona (ID=1)                        │
│  │       └─> INSERT 1 usuario                               │
│  └─> CoroutineScope.launch { initializeDatabase() }         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  ScreenManager                                              │
│  ├─> CounterViewModel = hiltViewModel()                     │
│  │   └─> count: StateFlow<Int> = getCountCompletados(1)     │
│  │       └─> Flow наблюдает за progreso_usuario             │
│  └─> TopBar(counterViewModel)                               │
│      └─> collectAsState() → отображает count                │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    ИГРОВОЙ ПРОЦЕСС                          │
└─────────────────────────────────────────────────────────────┘
                              
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  Activity1Screen                                            │
│  └─> GameViewModel (через hiltViewModel)                    │
│      └─> Игровая логика...                                  │
│          └─> validateQuizAnswer()                           │
│              └─> saveProgressToDatabase()                   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  ProgresoRepository.markActivityAsCompleted(1, 1)           │
│  └─> progresoDao.upsertProgresoCompletado(1, 1, date)       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  ProgresoDao.upsertProgresoCompletado() [@Transaction]      │
│  ├─> existsActividad(1) → true                              │
│  ├─> existsPersona(1) → true                                │
│  ├─> existsProgreso(1, 1) → false                           │
│  └─> INSERT INTO progreso_usuario                           │
│      (actividad_fk=1, persona_fk=1, completada=1)           │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  Room обнаруживает изменение в progreso_usuario             │
│  └─> Flow<Int> эмитит новое значение: COUNT(*) = 1          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  CounterViewModel.count обновляется                         │
│  └─> StateFlow.value = 1                                    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  TopBar.collectAsState() получает новое значение            │
│  └─> BadgedBox перерисовывается: badge показывает "1"       │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔑 Ключевые особенности архитектуры

### 1. Реактивное программирование
- Использование **Flow** для автоматического обновления UI при изменении БД
- **StateFlow** для управления состоянием в Compose
- Нет необходимости вручную обновлять UI после сохранения

### 2. Транзакции
- `@Transaction` гарантирует атомарность операций INSERT/UPDATE
- Предотвращает race conditions при одновременных запросах

### 3. UPSERT паттерн
- Автоматическое создание записи, если её нет
- Обновление существующей записи
- Проверка foreign keys перед вставкой

### 4. Dependency Injection (Hilt)
- Все зависимости создаются автоматически
- Singleton для базы данных
- ViewModels создаются через `hiltViewModel()`

### 5. Инициализация данных
- SQL инициализация при первом создании БД
- Резервный метод через DAO для дополнения данных
- Проверка существования перед вставкой

---

## 📝 Резюме

Приложение использует современную архитектуру Android с:
- **Room Database** для локального хранения
- **Flow/StateFlow** для реактивного обновления UI
- **Hilt** для управления зависимостями
- **MVVM** для разделения ответственности
- **UPSERT** для надежного сохранения прогресса

Счетчик в BadgedBox обновляется автоматически благодаря реактивной архитектуре: при изменении данных в БД Flow автоматически уведомляет подписчиков, и UI перерисовывается без дополнительного кода.
