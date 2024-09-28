package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    FilmController filmController;

    @Test
    void filmShouldValidateAndCreate() {
        Film film = new Film("Fight Club", "The unnamed Narrator, " +
                "who struggles with insomnia and dissatisfaction with his job and lifestyle, " +
                "finds temporary solace in support groups. As his insomnia worsens, " +
                "he discovers that expressions of emotional vulnerability help him sleep, " +
                "leading him to join multiple groups for people facing emotionally distressing problems, " +
                "despite his expressions being fraudulent. " +
                "His efforts are thwarted when Marla Singer, another impostor, joins the same groups. " +
                "The Narrator cannot present his fabricated struggles as genuine, " +
                "or divert his attention from her presence as an impostor, causing his sleeplessness to return. " +
                "He arranges for them to attend different sessions to regain his ability to sleep and, " +
                "under certain circumstances, to exchange contact information, to which she reluctantly agrees.",
                LocalDate.of(1999, 9, 10), 139, 1);
        assertEquals(film, filmController.create(film));
    }

    @Test
    void filmShouldValidateAndUpdate() {
        Film film1 = new Film("Fight Club", "The unnamed Narrator, " +
                "who struggles with insomnia and dissatisfaction with his job and lifestyle, " +
                "finds temporary solace in support groups. As his insomnia worsens, " +
                "he discovers that expressions of emotional vulnerability help him sleep, " +
                "leading him to join multiple groups for people facing emotionally distressing problems, " +
                "despite his expressions being fraudulent. " +
                "His efforts are thwarted when Marla Singer, another impostor, joins the same groups. " +
                "The Narrator cannot present his fabricated struggles as genuine, " +
                "or divert his attention from her presence as an impostor, causing his sleeplessness to return. " +
                "He arranges for them to attend different sessions to regain his ability to sleep and, " +
                "under certain circumstances, to exchange contact information, to which she reluctantly agrees.",
                LocalDate.of(1999, 9, 10), 139, 1);
        Film film2 = new Film("American Psycho", "In 1987, " +
                "investment banker Patrick Bateman spends most of his time dining at " +
                "popular restaurants while keeping up appearances for his fiancée, Evelyn Williams, " +
                "as well as his circle of wealthy associates, most of whom he hates. At a business meeting, " +
                "Bateman and his associates flaunt their business cards, obsessing over their designs. " +
                "Enraged by the superiority of his colleague Paul Allen's card, " +
                "Bateman finds a homeless man in an alley at night and kills him. Bateman and Allen, " +
                "who mistakes Bateman for another co-worker, make plans for dinner after a Christmas party. " +
                "Bateman resents Allen for his affluent lifestyle and ability to obtain reservations at Dorsia, " +
                "a highly exclusive restaurant which Bateman cannot get into. Bateman gets Allen drunk, " +
                "lures him to his apartment and kills him violently. " +
                "Bateman disposes of the body and goes into Allen's apartment to record an outgoing message on " +
                "his answering machine, claiming that Allen has gone to London.",
                LocalDate.of(2000, 1, 21), 102, 1);
        filmController.create(film1);
        assertEquals(film2, filmController.update(film2));
    }
}