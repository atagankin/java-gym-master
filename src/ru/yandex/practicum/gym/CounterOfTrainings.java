package ru.yandex.practicum.gym;

import java.util.Comparator;
import java.util.Objects;

public class CounterOfTrainings {
    private Coach coach;
    private int sessionsCnt;

    public CounterOfTrainings(Coach coach) {
        this.coach = coach;
        this.sessionsCnt = 0;
    }

    public CounterOfTrainings(Coach coach, int sessionsCnt) {
        this.coach = coach;
        this.sessionsCnt = sessionsCnt;
    }

    public void addSession() {
        this.sessionsCnt++;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        if (this == o) return true;
        CounterOfTrainings counter = (CounterOfTrainings) o;
        return Objects.equals(this.coach, counter.coach)
                && this.sessionsCnt == counter.sessionsCnt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.coach, this.sessionsCnt);
    }

    @Override
    public String toString() {
        return "( Тренер: " + this.coach + ", cnt = " + this.sessionsCnt + " )";
    }

    public static Comparator<CounterOfTrainings> counterOfTrainingsComparator() {
        return Comparator.comparingInt(x -> x.sessionsCnt);
    }
}
