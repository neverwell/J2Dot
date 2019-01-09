package com.nemo.game.system.user.msg;

import com.nemo.game.server.AbstractMessage;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;


/**
 * 心跳请求
 */
public class ReqHeartMessage extends AbstractMessage {

	@Override
	public void doAction() {
//		UserManager.getInstance().clientHeart(session);
	}
	
	public ReqHeartMessage() {
		this.queueId = 1;
	}
	
	@Override
	public int getId() {
		return 1009;
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

