package com.game.domain.player;

import com.game.sdk.proto.vo.StepBallVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class StepListData {
    Map<Integer, List<StepBallVO>> maps = Maps.newHashMap();

    public StepListData(){
        maps.put(StepType.Normal.ordinal(), Lists.newArrayList());
        maps.put(StepType.Give.ordinal(), Lists.newArrayList());
    }

    public Map<Integer, List<StepBallVO>> getMaps() {
        return maps;
    }

    public void setMaps(Map<Integer, List<StepBallVO>> maps) {
        this.maps = maps;
    }
}
