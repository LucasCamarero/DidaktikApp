# Рекомендации по улучшению и упрощению базы данных

## 📊 Текущая структура базы данных

### Существующие таблицы:
1. `persona` - Базовый класс пользователей
2. `usuario` - Ученики (наследуется от persona)
3. `profesor` - Преподаватели (наследуется от persona)
4. `imagen` - Изображения (21 запись)
5. `lugar` - Места на карте (7 записей)
6. `actividad` - Игровые активности (7 записей)
7. `progreso_usuario` - Прогресс пользователя (создается динамически)

---

## ❌ ЧТО МОЖНО УБРАТЬ (упрощение)

### 1. **Таблица `profesor` и связанная логика**
**Статус**: ❌ **НЕ ИСПОЛЬЗУЕТСЯ**

**Обоснование**:
- `ProfesorEntity` определена, но нигде не используется в коде
- `PersonaDao.insertProfesor()` не вызывается
- В приложении только один тип пользователя - `Usuario`

**Что убрать**:
- `ProfesorEntity.kt` - удалить файл
- Из `BarakaldoDatabase.kt` убрать `ProfesorEntity::class`
- Из `PersonaDao.kt` убрать `insertProfesor()`
- Из `PersonaEntity.tipo_persona` можно убрать значение "Profesor" (оставить только "Usuario")

**Эффект**: Упрощение схемы, меньше кода для поддержки

---

### 2. **Поле `tipo_actividad` в `ActividadEntity`**
**Статус**: ⚠️ **ВОЗМОЖНО ИЗБЫТОЧНО**

**Текущее значение**: "Puzzle", "Sopa de letras", "Selección", "Clasificación"

**Обоснование**:
- Не используется в логике приложения
- Все игры имеют одинаковую структуру (начало → игра → конец)
- Если в будущем будет один `EndOfActivityScreen`, тип активности не важен

**Что убрать**:
- Поле `tipo_actividad: String` из `ActividadEntity`
- Обновить SQL инициализацию в `DatabaseInitializer`

**Эффект**: Меньше данных, проще структура

---

### 3. **Поле `archivo_actividad` в `ActividadEntity`**
**Статус**: ⚠️ **ВОЗМОЖНО ИЗБЫТОЧНО**

**Текущее значение**: "activity1", "activity2", и т.д.

**Обоснование**:
- Навигация происходит по `actividad_id`, а не по имени файла
- В `ScreenManager` используются жестко заданные маршруты: `"activity1"`, `"activity2"`
- Значение дублирует `actividad_id`

**Что убрать**:
- Поле `archivo_actividad: String` из `ActividadEntity`
- Обновить SQL инициализацию

**Эффект**: Меньше данных, нет дублирования

---

### 4. **Поле `descripcion` в `LugarEntity`**
**Статус**: ❌ **НЕ ИСПОЛЬЗУЕТСЯ**

**Текущее значение**: "Lugar 1", "Lugar 2", и т.д.

**Обоснование**:
- Не используется в UI
- `nombre` уже содержит полное название места
- В `MapScreen` координаты хранятся статически в `MapPoint`

**Что убрать**:
- Поле `descripcion: String` из `LugarEntity`
- Обновить SQL инициализацию

**Эффект**: Меньше данных

---

### 5. **Поле `descripcion_corta` в `ImagenEntity`**
**Статус**: ⚠️ **ВОЗМОЖНО ИЗБЫТОЧНО**

**Текущее значение**: Nullable поле с описаниями типа "Lugar 1", "Premio antiguo 1"

**Обоснование**:
- Не используется в UI
- `path_archivo` уже идентифицирует изображение
- `tipo_uso` описывает назначение изображения

**Что убрать**:
- Поле `descripcion_corta: String?` из `ImagenEntity`
- Обновить SQL инициализацию

**Эффект**: Меньше данных, проще структура

---

### 6. **Метод `getRutaProgresoCompletado()` в `ProgresoDao`**
**Статус**: ⚠️ **НЕ ИСПОЛЬЗУЕТСЯ В UI**

**Обоснование**:
- Определен в `ProgresoRepository.getRutaProgreso()`
- НО: `MapScreen` использует только `counterViewModel.count` (количество завершенных)
- Сложный JOIN запрос не используется

**Что убрать**:
- Метод `getRutaProgresoCompletado()` из `ProgresoDao`
- Метод `getRutaProgreso()` из `ProgresoRepository`
- Класс `ProgresoRutaJoin` (DTO для этого запроса)

**Эффект**: Меньше кода, проще DAO

---

### 7. **Метод `initializeProgreso()` в `ProgresoRepository`**
**Статус**: ❌ **НЕ ВЫЗЫВАЕТСЯ**

**Обоснование**:
- Определен, но нигде не вызывается
- Прогресс создается динамически через `upsertProgresoCompletado()`
- Не нужна предварительная инициализация

**Что убрать**:
- Метод `initializeProgreso()` из `ProgresoRepository`
- Метод `insertInitialProgreso()` из `ProgresoDao` (если используется только здесь)

**Эффект**: Меньше кода, проще логика

---

### 8. **Поле `coordenadas` в `LugarEntity`**
**Статус**: ⚠️ **ДУБЛИРУЕТСЯ**

**Текущее значение**: "43.2992,-2.9884" (строка)

**Обоснование**:
- В `MapScreen` координаты хранятся статически в `MapPoint` с точными значениями
- Координаты в БД не используются
- Если координаты нужны, лучше хранить отдельно `lat` и `lng` как REAL

**Что убрать**:
- Поле `coordenadas: String` из `LugarEntity`
- Или заменить на `lat: Double` и `lng: Double` (если планируется использовать)

**Эффект**: Убрать дублирование или улучшить структуру

---

## ✅ ЧТО МОЖНО ДОБАВИТЬ (улучшение)

### 1. **Упростить структуру изображений для одного EndOfActivityScreen**

**Текущая проблема**:
- 21 изображение: 7 основных + 7 старых премий + 7 новых премий
- Если будет один `EndOfActivityScreen`, не нужны отдельные пути для старых/новых премий

**Предложение**:
- **Вариант А (упрощение)**: Убрать `premio_antigua_fk` и `premio_actual_fk` из `ActividadEntity`
  - Оставить только `premio_fk: Int` (одно изображение премии)
  - `EndOfActivityScreen` будет показывать одно изображение
  - Уменьшить количество изображений с 21 до 14 (7 основных + 7 премий)

- **Вариант Б (гибкость)**: Оставить два поля, но упростить логику
  - Если `EndOfActivityScreen` будет показывать оба изображения, структура остается
  - Но можно убрать `tipo_uso` из `ImagenEntity`, так как назначение определяется через FK

---

### 2. **Добавить индекс на `progreso_usuario`**

**Текущая проблема**:
- Запросы `getCountCompletados()` и `existsProgreso()` выполняются часто
- Нет индексов для оптимизации

**Предложение**:
```kotlin
@Entity(
    tableName = "progreso_usuario",
    indices = [
        Index(value = ["actividad_fk", "persona_fk"], unique = true),
        Index(value = ["persona_fk", "completada"])
    ]
)
```

**Эффект**: Быстрее запросы, гарантия уникальности пары (actividad_fk, persona_fk)

---

### 3. **Использовать Boolean вместо Int для `completada`**

**Текущая проблема**:
- `completada: Int` (0 или 1) - не семантично

**Предложение**:
```kotlin
val completada: Boolean  // true = завершена, false = не завершена
```

**Эффект**: Более понятный код, но потребуется миграция БД

**Альтернатива**: Оставить Int, но добавить константы:
```kotlin
companion object {
    const val NO_COMPLETADA = 0
    const val COMPLETADA = 1
}
```

---

### 4. **Использовать Long для `fecha_completado` вместо String**

**Текущая проблема**:
- `fecha_completado: String?` хранит timestamp как строку
- Неудобно для сортировки и фильтрации

**Предложение**:
```kotlin
val fecha_completado: Long?  // Timestamp в миллисекундах
```

**Эффект**: Легче работать с датами, быстрее сортировка

---

### 5. **Добавить поле `orden` в `ActividadEntity`**

**Текущая проблема**:
- Порядок активностей определяется только по `actividad_id`
- Если нужно изменить порядок, придется менять ID

**Предложение**:
```kotlin
val orden: Int  // Порядок отображения на карте (1, 2, 3, ...)
```

**Эффект**: Гибкость в изменении порядка без изменения ID

---

### 6. **Упростить проверки в `upsertProgresoCompletado()`**

**Текущая проблема**:
- Много проверок `existsActividad()` и `existsPersona()`
- Эти проверки нужны только для отладки

**Предложение**:
- Убрать проверки `existsActividad()` и `existsPersona()` из production кода
- Оставить только в debug режиме или убрать совсем
- Foreign Key constraints уже гарантируют целостность

**Эффект**: Меньше запросов к БД, быстрее выполнение

---

## 📋 Итоговая упрощенная структура

### Таблицы, которые остаются:

1. **`persona`** (упрощенная)
   - `persona_id` (PK)
   - `username`
   - `password_hash`
   - ~~`tipo_persona`~~ (можно убрать, если только Usuario)

2. **`usuario`**
   - `persona_fk` (PK, FK)
   - `nombre_completo_diploma`

3. **`imagen`** (упрощенная)
   - `imagen_id` (PK)
   - `path_archivo`
   - ~~`descripcion_corta`~~ (убрать)
   - ~~`tipo_uso`~~ (можно убрать, если не используется)

4. **`lugar`** (упрощенная)
   - `lugar_id` (PK)
   - `nombre`
   - ~~`descripcion`~~ (убрать)
   - ~~`coordenadas`~~ (убрать или заменить на lat/lng)
   - `imagen_principal_fk` (FK)

5. **`actividad`** (упрощенная)
   - `actividad_id` (PK)
   - ~~`tipo_actividad`~~ (убрать)
   - ~~`archivo_actividad`~~ (убрать)
   - `lugar_fk` (FK)
   - `premio_antigua_fk` (FK) - или упростить до `premio_fk`
   - `premio_actual_fk` (FK) - или убрать
   - `orden: Int` (добавить)

6. **`progreso_usuario`** (улучшенная)
   - `progreso_id` (PK)
   - `actividad_fk` (FK, с индексом)
   - `persona_fk` (FK, с индексом)
   - `completada: Boolean` (или Int с константами)
   - `fecha_completado: Long?` (вместо String)

### Таблицы, которые убираются:

- ❌ **`profesor`** - не используется

---

## 🎯 Приоритеты упрощения

### Высокий приоритет (быстро, безопасно):
1. ✅ Убрать `ProfesorEntity` и связанный код
2. ✅ Убрать `getRutaProgresoCompletado()` (не используется)
3. ✅ Убрать `initializeProgreso()` (не вызывается)
4. ✅ Убрать `descripcion` из `LugarEntity`
5. ✅ Убрать `descripcion_corta` из `ImagenEntity`

### Средний приоритет (требует проверки):
1. ⚠️ Убрать `tipo_actividad` из `ActividadEntity`
2. ⚠️ Убрать `archivo_actividad` из `ActividadEntity`
3. ⚠️ Упростить структуру изображений (один premio_fk вместо двух)
4. ⚠️ Убрать `coordenadas` из `LugarEntity` (если не используется)

### Низкий приоритет (улучшения):
1. 💡 Добавить индексы на `progreso_usuario`
2. 💡 Изменить `completada` на Boolean
3. 💡 Изменить `fecha_completado` на Long
4. 💡 Добавить `orden` в `ActividadEntity`
5. 💡 Упростить проверки в `upsertProgresoCompletado()`

---

## 📝 Рекомендации с учетом одного EndOfActivityScreen

Если все игры будут использовать один `EndOfActivityScreen`:

1. **Упростить `ActividadEntity`**:
   ```kotlin
   data class ActividadEntity(
       val actividad_id: Int = 0,
       val lugar_fk: Int,
       val premio_fk: Int,  // Одно изображение премии вместо двух
       val orden: Int       // Порядок на карте
   )
   ```

2. **Уменьшить количество изображений**:
   - 7 основных изображений мест
   - 7 изображений премий
   - Итого: 14 вместо 21

3. **Упростить `EndOfActivityScreen`**:
   - Получать `premio_fk` из `ActividadEntity`
   - Показывать одно изображение премии
   - Не нужна логика выбора между старым/новым изображением

---

## 🔄 Миграция базы данных

При внесении изменений потребуется:

1. **Увеличить версию БД** в `BarakaldoDatabase.kt`:
   ```kotlin
   version = 2  // было 1
   ```

2. **Создать миграцию** или использовать `fallbackToDestructiveMigration()` (для разработки)

3. **Обновить `DatabaseInitializer`** с новыми SQL запросами

---

## ✅ Итоговые преимущества упрощения

1. **Меньше кода**: Убрать ~200-300 строк неиспользуемого кода
2. **Проще поддержка**: Меньше таблиц и полей = меньше багов
3. **Быстрее запросы**: Меньше JOIN'ов и полей
4. **Понятнее структура**: Только то, что реально используется
5. **Легче расширение**: Проще добавлять новые функции
