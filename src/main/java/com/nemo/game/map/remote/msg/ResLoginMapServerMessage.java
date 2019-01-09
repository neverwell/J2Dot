package com.nemo.game.map.remote.msg;

import com.nemo.game.map.remote.RemoteHostManager;
import com.nemo.game.map.remote.msg.bean.HostInfo;
import com.nemo.game.server.AbstractMessage;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;

//返回登录远程后的消息
public class ResLoginMapServerMessage extends AbstractMessage {

	@Override
	public void doAction() {
		RemoteHostManager.getInstance().register(this.session, this.host);
	}
	
	public ResLoginMapServerMessage() {
		this.queueId = 0;
	}
	
	@Override
	public int getId() {
		return 82002;
	}
	
	/**
	 * 主机信息
	 */
	private HostInfo host;

	public HostInfo getHost() {
		return host;
	}

	public void setHost(HostInfo host) {
		this.host = host;
	}

	
	@Override
	public boolean read(KryoInput buf) {

		if (readByte(buf) != 0) {
			HostInfo hostInfo = new HostInfo();
			hostInfo.read(buf);
			this.host = hostInfo;
		}
		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {

		this.writeBean(buf, host);
		return true;
	}
}
