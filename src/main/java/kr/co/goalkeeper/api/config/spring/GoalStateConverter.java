package kr.co.goalkeeper.api.config.spring;

import kr.co.goalkeeper.api.model.entity.goal.GoalState;
import org.springframework.core.convert.converter.Converter;

public class GoalStateConverter implements Converter<String, GoalState> {
    @Override
    public GoalState convert(String source) {
        return GoalState.valueOf(source.toUpperCase());
    }
}
