package kr.co.goalkeeper.api.model.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.GoalState;
import kr.co.goalkeeper.api.model.entity.Reward;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "itemType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "OneTime", value = OneTimeGoalResponse.class),
        @JsonSubTypes.Type(name = "ManyTime", value = ManyTimeGoalResponse.class)
})
public abstract class GoalResponse {
    protected long id;
    protected String title;
    protected CategoryType categoryType;
    protected String content;
    @Min(0)
    @Max(10000)
    protected int point;
    protected Reward reward;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected List<CertificationResponse> certifications;
    protected GoalState goalState;
}
