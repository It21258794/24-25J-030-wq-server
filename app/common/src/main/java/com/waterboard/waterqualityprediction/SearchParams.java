package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class SearchParams {
    private String query;
    private List<String> status = new ArrayList<>();
    private int page = 0;
    private int size = 10;
    private String sortDirection = "DESC";
    private String sortBy;

    public abstract Map<String, Object> getSearchableFieldsAndValues();

    public List<Criteria> getSearchableFieldCriteria() {
        List<Criteria> criteriaList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : getSearchableFieldsAndValues().entrySet()) {
            if (entry.getValue() != null) {
                if (entry.getValue() instanceof Collection) {
                    criteriaList.add(Criteria.where(entry.getKey()).in(entry.getValue()));
                } else {
                    criteriaList.add(Criteria.where(entry.getKey()).is(entry.getValue()));
                }
            }
        }
        return criteriaList;
    }

    public Criteria getSearchableFieldCriteria(boolean addStatus, String... querySearchableFields) {
        List<Criteria> criteriaList = getSearchableFieldCriteria();
        if (Strings.isNotEmpty(this.getQuery())) {
            List<Criteria> keySearchableCriteria = new ArrayList<>();
            for (String field : querySearchableFields) {
                keySearchableCriteria.add(Criteria.where(field).regex(this.getQuery(), "i"));
            }
            Criteria keyCriteria = new Criteria().orOperator(keySearchableCriteria.toArray(Criteria[]::new));
            criteriaList.add(keyCriteria);
        }

        if (!CollectionUtils.isEmpty(this.getStatus()) && addStatus) {
            criteriaList.add(Criteria.where("status").in(this.getStatus()));
        }

        if (!CollectionUtils.isEmpty(criteriaList)) {
            return new Criteria().andOperator(criteriaList.toArray(Criteria[]::new));
        }
        return new Criteria();
    }

    public Sort.Direction getSortDirection() {
        if (this.sortDirection.equals("ASC")) {
            return Sort.Direction.ASC;
        }
        return Sort.Direction.DESC;
    }
}

