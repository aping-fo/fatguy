package com.game.domain.quest;

import com.game.util.RandomUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.game.sdk.proto.vo.QuestVO;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lucky on 2018/10/11.
 */
public class Room {
    private int id;
    private long startTime;
    private long robAnswerTime;
    private boolean isSingle;

    private Map<String, Fighter> roles = Maps.newConcurrentMap();
    private List<Integer> questionCategorys = Lists.newArrayList();
    private List<Integer> emptyIndexs = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8));
    private List<QuestVO> questions = Lists.newArrayList();
    private Map<Integer, Answer> answers = Maps.newHashMap();

    private final ReentrantLock lock = new ReentrantLock();

    private int currentIndex=-1;     //当前抢答位置
    private QuestVO currentQuest; //当前抢答题
    private List<QuestVO> currentQuestions = Lists.newArrayList(); //已经出的题目
    private String answerOpendid; //抢答玩家ID
    private String victoryOpenid; //胜利玩家ID
    private Map<String,Integer> faces=Maps.newConcurrentMap();//玩家表情

    public String getVictoryOpenid() {
        return victoryOpenid;
    }

    public void setVictoryOpenid(String victoryOpenid) {
        this.victoryOpenid = victoryOpenid;
    }

    public Map<Integer, Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, Answer> answers) {
        this.answers = answers;
    }

    public String getAnswerOpendid() {
        return answerOpendid;
    }

    public void setAnswerOpendid(String answerOpendid) {
        this.answerOpendid = answerOpendid;
    }

    public QuestVO getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(QuestVO currentQuest) {
        this.currentQuest = currentQuest;
    }

    public List<QuestVO> getCurrentQuestions() {
        return currentQuestions;
    }

    public void setCurrentQuestions(List<QuestVO> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getRobAnswerTime() {
        return robAnswerTime;
    }

    public void setRobAnswerTime(long robAnswerTime) {
        this.robAnswerTime = robAnswerTime;
    }

    public Room(int id) {
        this.id = id;
        this.startTime = System.currentTimeMillis();
        this.isSingle = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSingle() { return isSingle; }

    public void setSingle(boolean single) { isSingle = single; }

    public Map<String, Fighter> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Fighter> roles) {
        this.roles = roles;
    }

    public Fighter getOpponent(String openId){
        if(this.roles.size() != 2){
            return null;
        }

        Fighter opponent = null;
        for(Fighter fighter: this.roles.values()){
            if(!fighter.getOpenId().equals(openId)){
                opponent = fighter;
            }
        }
        return opponent;
    }

    public List<QuestVO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestVO> questions) {
        this.questions = questions;
    }

    public Map<String, Integer> getPlayerFace() {
        return faces;
    }

    public void setPlayerFace (Map<String,  Integer> faces) {
        this.faces = faces;
    }

    public int randomEmptyIndexs() {
        try {
            lock.lock();
            if(currentIndex == -1){
                if(emptyIndexs.size() > 0){
                    int idx = RandomUtil.randInt(emptyIndexs.size());
                    currentIndex = emptyIndexs.get(idx);
                    emptyIndexs.remove(idx);
                }
            }

            return currentIndex;
        } finally {
            lock.unlock();
        }
    }

    public String robAnswer(String openid) {
        try {
            lock.lock();
            if (answerOpendid == null) {
                answerOpendid = openid;
            }
            return answerOpendid;
        } finally {
            lock.unlock();
        }
    }

    public void cleanQuest() {
        try {
            lock.lock();
            currentIndex = -1;
            currentQuest = null;
            answerOpendid = null;
        } finally {
            lock.unlock();
        }
    }

    public List<Integer> getQuestionCategorys() {
        return questionCategorys;
    }

    public void setQuestionCategorys(List<Integer> questionCategorys) {
        this.questionCategorys = questionCategorys;
    }

    public List<Integer> getEmptyIndexs() {
        return emptyIndexs;
    }

    public void setEmptyIndexs(List<Integer> emptyIndexs) {
        this.emptyIndexs = emptyIndexs;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
