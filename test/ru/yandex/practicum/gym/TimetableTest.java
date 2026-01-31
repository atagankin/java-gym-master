package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        //Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TrainingSession[] controlTrainings = {thursdayChildTrainingSession, thursdayAdultTrainingSession};
        TrainingSession[] ckeckTrainings = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)
                .toArray(new TrainingSession[timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size()]);
        Assertions.assertArrayEquals(controlTrainings, ckeckTrainings);
        // Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
    }

    @Test
    void testCheckAviabilitySlotPerCase1False() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(nestedTraining);
        boolean aviability = timetable.isCoachFree(DayOfWeek.MONDAY, coach, (12*60), (14*60));
        Assertions.assertFalse(aviability);
    }

    @Test
    void testCheckAviabilitySlotPerCase1NotAdded() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdults = new Group("Йога для взрослых", Age.ADULT, 120);

        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        TrainingSession ckeckTraining = new TrainingSession(groupAdults, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));

        timetable.addNewTrainingSession(nestedTraining);
        timetable.addNewTrainingSession(ckeckTraining);

        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
    }

    @Test
    void testCheckAviabilitySlotPerCase2True() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(nestedTraining);
        boolean aviability = timetable.isCoachFree(DayOfWeek.MONDAY, coach, (9*60), (10*60));
        Assertions.assertTrue(aviability);
    }

    @Test
    void testCheckAviabilitySlotPerCaseLeftSideTrue() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(nestedTraining);
        boolean aviability = timetable.isCoachFree(DayOfWeek.MONDAY, coach, (14*60), (15*60));
        Assertions.assertTrue(aviability);
    }

    @Test
    void testCheckAviabilitySlotPerCaseRightSideFalse() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(nestedTraining);
        boolean aviability = timetable.isCoachFree(DayOfWeek.MONDAY, coach, (12*60), (13*60));
        Assertions.assertFalse(aviability);
    }

    @Test
    void testCheckAviabilitySlotPerCasePartLeftSideFalse() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(nestedTraining);
        boolean aviability = timetable.isCoachFree(DayOfWeek.MONDAY, coach, (12*60 + 30), (14*60));
        Assertions.assertFalse(aviability);
    }

    @Test
    void testCheckAviabilitySlotPerCasePartRightSideFalse() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(nestedTraining);
        boolean aviability = timetable.isCoachFree(DayOfWeek.MONDAY, coach, (12*60 + 30), (13*60 + 30));
        Assertions.assertFalse(aviability);
    }

    @Test
    void testCheckAviabilitySlotPerCaseFullOverrideFalse() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 240);
        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(nestedTraining);
        boolean aviability = timetable.isCoachFree(DayOfWeek.MONDAY, coach, (11*60), (12*60));
        Assertions.assertFalse(aviability);
    }

    @Test
    void testGetTrainingSessionsCntForEmptyTimetable() {
        Timetable timetable = new Timetable();
        Assertions.assertEquals(0, timetable.getCountByCoaches().size());
    }

    @Test
    void testGetTrainingSessionsCntForOneSession() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession nestedTraining = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(nestedTraining);

        Assertions.assertEquals(1, timetable.getCountByCoaches().size());
    }

    @Test
    void testGetTrainingSessionsCntForTwoSessionsOneDay() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession Training1 = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        TrainingSession Training2 = new TrainingSession(groupChilds, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));

        timetable.addNewTrainingSession(Training1);
        timetable.addNewTrainingSession(Training2);



        CounterOfTrainings[] controlStatistic = {new CounterOfTrainings(coach, 2)};
        CounterOfTrainings[] checkList = timetable.getCountByCoaches().toArray(new CounterOfTrainings[1]);

        Assertions.assertArrayEquals(controlStatistic, checkList);
    }

    @Test
    void testGetTrainingSessionsCntForMultiSession() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        Group groupChilds = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession Training1 = new TrainingSession(groupChilds, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        TrainingSession Training2 = new TrainingSession(groupChilds, coach1,
                DayOfWeek.SUNDAY, new TimeOfDay(10, 0));

        TrainingSession Training3 = new TrainingSession(groupChilds, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(Training1);
        timetable.addNewTrainingSession(Training2);
        timetable.addNewTrainingSession(Training3);

        CounterOfTrainings[] controlGroup = {new CounterOfTrainings(coach1, 2), new CounterOfTrainings(coach2, 1)};
        CounterOfTrainings[] checkGroup = timetable.getCountByCoaches().toArray(new CounterOfTrainings[2]);

        Assertions.assertArrayEquals(controlGroup, checkGroup,
                () -> "\nМассивы строк не совпадают. Ожидалось: " + Arrays.toString(controlGroup)
                        + "\nПолучили: " + Arrays.toString(checkGroup)
        );

    }

}
