package com.nemo.game.system.chapter.entity;

import com.nemo.game.entity.sys.AbstractSysData;
import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterRankData extends AbstractSysData {
    @Tag(1)
    private long id;

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
