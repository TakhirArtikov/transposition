package com.example.transposition;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Test {
    public static class Note {
        public int octave;
        public int noteNumber;

        public Note(int octave, int noteNumber) {
            this.octave = octave;
            this.noteNumber = noteNumber;
        }

        @Override
        public String toString() {
            return "[" + octave + ", " + noteNumber + "]";
        }
    }

    private static final int MIN_OCTAVE = -3;
    private static final int MAX_OCTAVE = 5;
    private static final int NOTES_PER_OCTAVE = 12;

    public static boolean isNoteValid(Note note) {
        return MIN_OCTAVE <= note.octave && note.octave <= MAX_OCTAVE &&
                1 <= note.noteNumber && note.noteNumber <= NOTES_PER_OCTAVE;
    }

    public static Note transposeNote(Note note, int semitones) {


        int octaveStep = semitones / 12;
        int mod = semitones % 12;

        int newOctave = note.octave + octaveStep;
        int newNoteNumber = note.noteNumber + mod;

        while (newNoteNumber <= 0) {
            newOctave--;
            newNoteNumber += NOTES_PER_OCTAVE;
        }

        Note note1 = new Note(newOctave, newNoteNumber);
        if (!isNoteValid(note1)) {
            throw new UnsupportedOperationException("Out of range keyboard!");
        }
        return note1;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        String path= scanner.next();
        int semitones= scanner.nextInt();
        List<Note> musicalPiece = loadMusicalPieceFromJson(path);
        List<Note> newMusicalPiece = new ArrayList<>();
        for (Note note : musicalPiece) {
            newMusicalPiece.add(transposeNote(note, semitones));
        }


        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("./output.json"), newMusicalPiece);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Note> loadMusicalPieceFromJson(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<List<Integer>> jsonData = mapper.readValue(new File(path), List.class);

        List<Note> musicalPiece = jsonData.stream()
                .map(list -> new Note(list.get(0), list.get(1)))
                .collect(Collectors.toList());
        return musicalPiece;
    }
}

