package com.nemo.game.entity.sys;

import com.nemo.game.system.count.entity.Count;
import io.protostuff.Tag;

import java.util.HashMap;
import java.util.Map;

//全局计数
public class GlobalCount extends AbstractSysData {

	@Tag(1)
	private long id;

	@Tag(2)
	private Map<String, Count> countMap = new HashMap<>();

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	public Map<String, Count> getCountMap() {
		return countMap;
	}

	public void setCountMap(Map<String, Count> countMap) {
		this.countMap = countMap;
	}
}
