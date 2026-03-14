package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;
    private final Map<DayOfWeek, Set<TrainingSession>> timetableCache;
    private final HashMap<Coach, Integer> coachesCounter;

    public Timetable() {
        timetable = new HashMap<>();
        timetableCache = new HashMap<>();
        coachesCounter = new HashMap<>();
        // Инициализируем все дни недели
        for (DayOfWeek day: DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
            timetableCache.put(day, new TreeSet<>(TrainingSession.trainingSessionTimeComparator()));
        }
    }

    /**
     * Функция проверяем доступность слота по времени в заданный день по тренеру
     * @param dayOfWeek - день недели (Enum)
     * @param coach - тренер
     * @param trgtStart - время начала окна для проверки в формате количества минут от 00:00
     * @param trgtEnd - время окончания окна для проверки в формате количества минут от 00:00
     * @return true для свободного слота и false - для занятого
     */
    public boolean isCoachFree(DayOfWeek dayOfWeek, Coach coach, int trgtStart, int trgtEnd) {
        boolean isSlotAviable = true;

        if (timetable.containsKey(dayOfWeek)) {
            Map<TimeOfDay, List<TrainingSession>> dailyTimeTable = timetable.get(dayOfWeek);

            if (!dailyTimeTable.isEmpty()) {
                List<TrainingSession> dailyList = dailyTimeTable.values().stream()
                        .flatMap(List::stream)
                        .toList();

                isSlotAviable = dailyList.stream()
                        .filter(x -> x.getCoach().equals(coach))
                        .noneMatch(p -> p.getReducedStart() <= trgtEnd && p.getReducedEnd() > trgtStart);
            }
        }
        return isSlotAviable;
    }

    // Также нельзя пересекаться с неокончившейся тренировкой этого тренера
    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        DayOfWeek inputDay = trainingSession.getDayOfWeek();
        TimeOfDay inputTime = trainingSession.getTimeOfDay();
        Coach coach = trainingSession.getCoach();
        int newStart = trainingSession.getReducedStart();
        int newEnd = trainingSession.getReducedEnd();

        Map<TimeOfDay, List<TrainingSession>> trainingSlot = timetable.get(inputDay);
        // Проверим доступность тренера
        boolean isCoachSlotAviable = isCoachFree(inputDay, coach, newStart, newEnd);

        if (isCoachSlotAviable) {
            if (trainingSlot.containsKey(inputTime)) {
                trainingSlot.get(inputTime).add(trainingSession);
                timetableCache.get(inputDay).add(trainingSession);
            } else {
                ArrayList<TrainingSession> sessionsList = new ArrayList<>();
                sessionsList.add(trainingSession);
                trainingSlot.put(inputTime, sessionsList);
                timetable.put(inputDay, trainingSlot);
                timetableCache.get(inputDay).add(trainingSession);
            }
            coachesCounter.put(coach, coachesCounter.getOrDefault(coach, 0) + 1);
        }
    }

    /* непонятно, что возвращать */
    public Set<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        return timetableCache.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        return timetable.get(dayOfWeek).getOrDefault(timeOfDay, null);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        List<CounterOfTrainings> coachStatistic = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachesCounter.entrySet()) {
            CounterOfTrainings counter = new CounterOfTrainings(entry.getKey(), entry.getValue());
            coachStatistic.add(counter);
        }
        coachStatistic.sort(CounterOfTrainings.counterOfTrainingsComparator().reversed());
        return coachStatistic;
    }
}
