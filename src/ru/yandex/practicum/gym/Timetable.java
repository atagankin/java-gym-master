package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;
    private final Map<DayOfWeek, Set<TrainingSession>> timetableCache;

    public Timetable() {
        timetable = new HashMap<>();
        timetableCache = new HashMap<>();
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
     * @return
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
        }
    }

    /* непонятно, что возвращать */
    public Set<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        return timetableCache.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        if (timetable.get(dayOfWeek).containsKey(timeOfDay)) {
            return timetable.get(dayOfWeek).get(timeOfDay);
        }
        return null;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, CounterOfTrainings> coachMap = new HashMap<>();

        for (Set<TrainingSession> trainingsSet: timetableCache.values()) {
            for (TrainingSession t: trainingsSet) {
                Coach coach = t.getCoach();
                if (!coachMap.containsKey(coach)) {
                    CounterOfTrainings counter = new CounterOfTrainings(coach);
                    coachMap.put(coach, counter);
                }
                coachMap.get(coach).addSession();
            }
        }

        List<CounterOfTrainings> coachStatistic = new ArrayList<>(coachMap.values());

        coachStatistic.sort(CounterOfTrainings.counterOfTrainingsComparator().reversed());
        return coachStatistic;
    }
}
