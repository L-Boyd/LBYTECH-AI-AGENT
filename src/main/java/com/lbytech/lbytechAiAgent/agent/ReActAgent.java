package com.lbytech.lbytechAiAgent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * Resoning and Acting 模式的代理抽象类
 * 实现了思考-行动的循环模式
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ReActAgent extends BaseAgent {

    /**
     * 思考方法，子类必须实现.
     * 思考过程中，根据当前状态和环境信息，判断是否需要执行行动
     *
     * @return 思考结果：是否需要执行行动
     */
    public abstract boolean think();

     /**
      * 行动方法，子类必须实现.
      * 行动过程中，根据思考结果，执行具体的行动
      *
      * @return 行动结果：执行的具体行动
      */
    public abstract String act();

    @Override
    public String step() {
        try {
            // 先思考
            boolean shouldAct = this.think();
            if (!shouldAct) {
                return "思考结果：不需要执行行动";
            }
            // 再行动
            String actResult = this.act();
            return "行动结果：" + actResult;
        } catch (Exception e) {
            e.printStackTrace();
            return "行动异常：" + e.getMessage();
        }
    }
}
