package com.neverwell.markbook.processor;

import com.neverwell.markbook.data.NoteData;

import java.util.List;

public interface SourceNoteData {
    String getFileName();

    String getTopic();

    List<NoteData> getNoteList();
}
