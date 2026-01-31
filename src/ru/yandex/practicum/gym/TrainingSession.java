package ru.yandex.practicum.gym;

import java.util.Comparator;

public class TrainingSession {

    //группа
    private Group group;
    //тренер
    private Coach coach;
    //день недели
    private DayOfWeek dayOfWeek;
    //время начала занятия
    private TimeOfDay timeOfDay;

    public TrainingSession(Group group, Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        this.group = group;
        this.coach = coach;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public Group getGroup() {
        return group;
    }

    public Coach getCoach() {
        return coach;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    /**
     * Возвращает int : количество минут с 00:00 до времени старта тренировки
     * @return
     */
    public int getReducedStart() {
        return timeOfDay.getHours()*60 + timeOfDay.getMinutes();
    }

    /**
     * Возвращает int : количество минут с 00:00 до времени окончания тренировки
     * @return
     */
    public int getReducedEnd() {
        return timeOfDay.getHours()*60 + timeOfDay.getMinutes() + group.getDuration();
    }

    public static Comparator<TrainingSession> trainingSessionTimeComparator() {
        return Comparator.comparingInt(TrainingSession::getReducedStart);
    }

    @Override
    public String toString() {
        return "( Тренировка. Тренер = " + coach.getSurname() + ", группа = " + group.getTitle() + ", дата = "
                + dayOfWeek + ", время = " + timeOfDay + ", dtbeg = " + this.getReducedStart() + ", dtend = "
                + this.getReducedEnd()+ ")";
    }
}
