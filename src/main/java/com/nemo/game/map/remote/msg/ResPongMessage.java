package com.nemo.game.map.remote.msg;

import com.nemo.game.map.remote.RemoteHostManager;
import com.nemo.game.server.AbstractMessage;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;

public class ResPongMessage extends AbstractMessage {

	@Override
	public void doAction() {
		RemoteHostManager.getInstance().updateHeart(session);
	}
	
	public ResPongMessage() {
		this.queueId = 0;
	}
	
	@Override
	public int getId() {
		return 82005;
	}

	@Override
	public boolean read(KryoInput buf) {
		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {
		return true;
	}
}
