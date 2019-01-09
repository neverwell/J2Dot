package com.nemo.game.map.msg;

import com.nemo.game.map.MapManager;
import com.nemo.game.server.AbstractMessage;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;


/**
 * <p>玩家登录地图</p>
 * <p>Created by MessageUtil</p>
 *
 * @author : admin
 */

public class ReqLoginMapMessage extends AbstractMessage {

	@Override
	public void doAction() {
//		MapManager.getInstance().loginMap(this.session.getRole().getId());
	}
	
	public ReqLoginMapMessage() {
		this.queueId = 0;
	}
	
	@Override
	public int getId() {
		return 67012;
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
