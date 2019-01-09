package com.nemo.game.map.remote.msg;

import com.nemo.game.server.AbstractMessage;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;

import java.util.ArrayList;
import java.util.List;

//向远程地图服同步当前在跨服中的玩家
public class ReqPlayerListMessage extends AbstractMessage {

	@Override
	public void doAction() {

	}

	public ReqPlayerListMessage() {
		this.queueId = 0;
	}

	@Override
	public int getId() {
		return 82006;
	}

	/**
	 * 主机id
	 */
	private int hostId;
	/**
	 * 玩家列表
	 */
	private List<Long> PlayerIdList = new ArrayList<>();

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public List<Long> getPlayerIdList() {
		return PlayerIdList;
	}

	public void setPlayerIdList(List<Long> PlayerIdList) {
		this.PlayerIdList = PlayerIdList;
	}

	@Override
	public boolean read(KryoInput buf) {

		this.hostId = readInt(buf, false);
		int PlayerIdListLength = readShort(buf);
		for (int PlayerIdListI = 0; PlayerIdListI < PlayerIdListLength; PlayerIdListI++) {
			this.PlayerIdList.add(this.readLong(buf));
		}
		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {

		this.writeInt(buf, hostId, false);
		writeShort(buf, this.PlayerIdList.size());
		for (int PlayerIdListI = 0; PlayerIdListI < this.PlayerIdList.size(); PlayerIdListI++) {
			this.writeLong(buf, this.PlayerIdList.get(PlayerIdListI));
		}
		return true;
	}
}
