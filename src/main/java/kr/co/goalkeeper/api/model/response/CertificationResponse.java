package kr.co.goalkeeper.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import kr.co.goalkeeper.api.model.entity.goal.CertificationState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "itemType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "OneTime", value = OneTimeGoalResponse.class),
        @JsonSubTypes.Type(name = "ManyTime", value = ManyTimeGoalResponse.class)
})
public abstract class CertificationResponse {
    protected long id;
    protected String content;
    protected String picture;
    protected CertificationState state;
    protected LocalDate date;
    protected int successCount;
    protected int failCount;
    protected List<RelatedCertificationResponse> relatedCertifications;
    @Getter
    @AllArgsConstructor
    static class RelatedCertificationResponse {
        private long id;
        @JsonProperty("date")
        private LocalDate certDate;
        @JsonProperty("state")
        private CertificationState certificationState;
    }
}
