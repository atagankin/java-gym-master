# java-gym-master
Repository for homework project.

В классе TrainingSession реализованы методы
* getReducedStart
* getReducedEnd
 
Методы необходимы для получения универсальной проверки на пересечение тренировок одного тренера в рамках одного дня.

## Timetable
### addNewTrainingSession(TrainingSession trainingSession)
Перед добавлением тренировки в расписание проводим проверку на доступность слота. Проверка заключатся в следующих ограничениях
* Если в этот день у того же тренера уже есть тренировка, расписание которой пересекается с новой (которую необходимо добавить), то это вызывает ошибку и новая тренировка не добавляется.

Особенности реализации проверки:

1. Формируем ArrayList всех тренировок в этот день 
```java
List<TrainingSession> dailyList = trainingSlot.values().stream()
                                                    .flatMap(List::stream)
                                                    .toList();
```
где flatMap преобразовывает Stream<List<TrainingSession>> в просто Stream<TrainingSession>, а toList собирает все элементы в List. 
                                                    
2. На основе этого List проводим фильтрацию по тренеру
3. На отфильтрованном списке применяем оператор anyMatch, который вернет нам true в случае, если в оставшемся списке есть хотя бы одно пересечение по времени с новой тренировкой

```java
boolean isSlotAviable = dailyList.stream()
                            .filter(x -> x.getCoach().equals(coach))
                            .anyMatch(p -> {
                                if (p.getReducedStart() <= newEnd && p.getReducedEnd() >= newStart) {
                                    // Если есть пересечение отрезков дат, то блокируем добавление новой тренировки
                                    return false;
                                }
                                return true;
                            });
```
PS
от anyMatch пришлось отказаться в пользу noneMatch. Причина : если на вход после фильтрации мы получаем пустое множество, то anyMatch ВСЕГДА возвращает false, что неприемлемо для нашей функции.
Ну и дополнительно : обратная логика (когда возвращаем false для сравнения с результатом true) выглядит каким-то чудовищем.