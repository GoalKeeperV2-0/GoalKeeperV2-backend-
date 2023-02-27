package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.entity.UserCategoryPoint;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class PointState {
    private final int usablePoint;
    private final Map<CategoryType,Integer> categoryPoints;
    public PointState(User user){
        Set<UserCategoryPoint> userCategoryPoints = user.getUserCategoryPointSet();
        categoryPoints = new HashMap<>();
        userCategoryPoints.forEach(usp -> categoryPoints.put(usp.getCategory().getCategoryType(),usp.getPoint()));
        usablePoint = user.getPoint();
    }
}
