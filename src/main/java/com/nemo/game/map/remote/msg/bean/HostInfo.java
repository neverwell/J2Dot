package com.nemo.game.map.remote.msg.bean;

import com.nemo.net.kryo.KryoBean;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;

public class HostInfo extends KryoBean {

	//主机id
	private int hostId;
	//主机ip
	private String hostIp;
	//主机端口
	private int hostPort;
	//连接序号
	private int index;
	//开服时间
	private long openTime;
	//合服时间
	private long combineTime;

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public int getHostPort() {
		return hostPort;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public long getCombineTime() {
		return combineTime;
	}

	public void setCombineTime(long combineTime) {
		this.combineTime = combineTime;
	}


	@Override
	public boolean read(KryoInput buf) {

		this.hostId = readInt(buf, false);
		this.hostIp = readString(buf);
		this.hostPort = readInt(buf, false);
		this.index = readInt(buf, false);
		this.openTime = readLong(buf);
		this.combineTime = readLong(buf);
		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {

		this.writeInt(buf, hostId, false);
		this.writeString(buf, hostIp);
		this.writeInt(buf, hostPort, false);
		this.writeInt(buf, index, false);
		this.writeLong(buf, openTime);
		this.writeLong(buf, combineTime);
		return true;
	}
}
