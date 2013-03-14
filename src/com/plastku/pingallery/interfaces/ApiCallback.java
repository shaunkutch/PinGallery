package com.plastku.pingallery.interfaces;

import com.plastku.pingallery.vo.ResultVO;

public interface ApiCallback {
	public void onSuccess(ResultVO result);
	public void onError(ResultVO result);
}
